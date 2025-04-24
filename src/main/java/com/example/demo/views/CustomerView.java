package com.example.demo.views;

import com.example.demo.views.components.CustomerPageComponents.CustomerForm;
import com.example.demo.views.components.CustomerPageComponents.CustomerTable;
import com.example.demo.views.components.CustomerPageComponents.CustomerToolbar;
import com.example.demo.views.repositories.database_entities.AuthorizationData;
import com.example.demo.views.repositories.database_entities.CustomerCard;
import com.example.demo.views.services.AuthorizationService;
import com.example.demo.views.services.CustomerService;
import com.example.demo.views.services.MyUserDetails;
import com.example.demo.views.services.MyUserDetailsService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.annotation.Scope;

import java.util.Objects;

@Route(value = "customers", layout = MainLayout.class)
@SpringComponent
@PermitAll
@Scope("prototype")
@PageTitle("Customers | ZLAGODA")
public class CustomerView extends AppLayout {

    protected CustomerTable table;
    protected CustomerToolbar bar;
    protected CustomerService service;
    protected CustomerForm customerForm;
    protected MyUserDetails user;

    public CustomerView(CustomerService service) {
        this.service = service;
        this.customerForm = new CustomerForm();
        this.user = MyUserDetailsService.getCurrentUser();
        if (Objects.equals(user.getRole(), "Manager")) {
            customerForm.setDeleteButton(true);
        }else if(Objects.equals(user.getRole(), "Cashier")){
            customerForm.setDeleteButton(false);
        }
        this.table = new CustomerTable(service);
        this.bar = new CustomerToolbar();

        configureContent();
    }

    private void configureContent() {
        bar.setWidth("100%");
        table.setMinWidth("100%");
        table.asSingleSelect().addValueChangeListener(event ->
                editCustomer(event.getValue()));

        VerticalLayout tableContainer = new VerticalLayout(table);
        tableContainer.setSizeFull();
        tableContainer.setPadding(false);
        tableContainer.getStyle().set("overflow", "auto");

        VerticalLayout content = new VerticalLayout(bar, tableContainer);
        content.setSizeFull();
        setContent(content);

        customerForm.addSaveListener(this::saveCustomer);
        customerForm.addDeleteListener(this::deleteCustomer);
        customerForm.addCloseListener(e -> closeEditor());

        bar.getAddButton().addClickListener(e -> addCustomer());
        bar.addUpdateListener(e -> updateList());
    }

    private void closeEditor() {
        customerForm.setCustomerCard(null);
        customerForm.close();
    }

    private void updateList() {
        table.setItems(bar.getAllByfilters(service));
    }

    private void saveCustomer(CustomerForm.SaveCustomerEvent event) {
        try {
            CustomerCard c = event.getEntity();
            if (c.getId() == null) {
                service.addEntity(c);
            } else {
                service.updateEntity(c);
            }
            updateList();
            closeEditor();
        } catch (ConstraintViolationException e) {
            customerForm.setPhoneAlert();
        }
    }

    private void deleteCustomer(CustomerForm.DeleteCustomerEvent event) {
        try {
            service.deleteEntity(event.getEntity().getId());
            updateList();
            closeEditor();
        } catch (ConstraintViolationException e) {
            showErrorNotification(e.getMessage());
        }
    }

    private void editCustomer(CustomerCard c) {
        if (c == null) {
            closeEditor();
        } else {
            customerForm.setCustomerCard(c);
            customerForm.open();
            addClassName("editing");
        }
    }

    private void addCustomer() {
        table.asSingleSelect().clear();
        editCustomer(new CustomerCard());
    }

    private void showErrorNotification(String message) {
        Notification notification = new Notification();
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        Div text = new Div(new Text(message));
        Button closeButton = new Button(new Icon("lumo", "cross"));
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        closeButton.setAriaLabel("Close");
        closeButton.addClickListener(event -> notification.close());
        HorizontalLayout layout = new HorizontalLayout(text, closeButton);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        notification.setDuration(7000);
        notification.add(layout);
        notification.open();
    }
}
