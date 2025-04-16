//package com.example.demo.views;
//
//import com.example.demo.views.components.CustomerPageComponents.CustomerForm;
//import com.example.demo.views.components.CustomerPageComponents.CustomerToolbar;
//import com.example.demo.views.components.CustomerPageComponents.CustomerTable;
//import com.example.demo.views.services.CustomerService;
//import com.example.demo.views.repositories.database_entities.CustomerCard;
//import com.vaadin.flow.component.Text;
//import com.vaadin.flow.component.applayout.AppLayout;
//import com.vaadin.flow.component.button.Button;
//import com.vaadin.flow.component.button.ButtonVariant;
//import com.vaadin.flow.component.html.Div;
//import com.vaadin.flow.component.icon.Icon;
//import com.vaadin.flow.component.notification.Notification;
//import com.vaadin.flow.component.notification.NotificationVariant;
//import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
//import com.vaadin.flow.component.orderedlayout.VerticalLayout;
//import com.vaadin.flow.router.PageTitle;
//import com.vaadin.flow.router.Route;
//import com.vaadin.flow.spring.annotation.SpringComponent;
//import org.springframework.context.annotation.Scope;
//import com.vaadin.flow.component.orderedlayout.FlexComponent;
//
//
//import jakarta.validation.ConstraintViolationException;
//
//@Route(value = "customers", layout = ManagerLayout.class)
//@SpringComponent
//@Scope("prototype")
//@PageTitle("Customers | ZLAGODA")
//public class ManagerCustomerView extends AppLayout {
//
//    protected CustomerTable table;
//    protected CustomerToolbar bar;
//    protected CustomerService service;
//    protected CustomerForm customerForm;
//
//    public ManagerCustomerView(CustomerService service) {
//        this.service = service;
//        this.customerForm = new CustomerForm();
//        this.table = new CustomerTable(service);
//        this.bar = new CustomerToolbar();
//
//        configureContent();
//    }
//
//    private void configureContent() {
//        bar.setWidth("100%");
//        table.setMinWidth("130%");
//        table.asSingleSelect().addValueChangeListener(event ->
//                editCustomer(event.getValue()));
//
//        VerticalLayout tableContainer = new VerticalLayout(table);
//        tableContainer.setSizeFull();
//        tableContainer.setPadding(false);
//        tableContainer.getStyle().set("overflow", "auto");
//
//        VerticalLayout content = new VerticalLayout(bar, tableContainer);
//        content.setSizeFull();
//        setContent(content);
//
//        customerForm.addSaveListener(this::saveCustomer);
//        customerForm.addDeleteListener(this::deleteCustomer);
//        customerForm.addCloseListener(e -> closeEditor());
//
//        bar.getAddButton().addClickListener(e -> addCustomer());
//        bar.addUpdateListener(e -> updateList());
//    }
//
//    private void closeEditor() {
//        customerForm.setCustomer(null);
//        customerForm.close();
//    }
//
//    private void updateList() {
//        table.setItems(bar.getAllByFilters(service));
//    }
//
//    private void saveCustomer(CustomerForm.SaveCustomerEvent event) {
//        try {
//            CustomerCard c = event.getCustomer();
//            if (c.getId() == null) {
//                service.save(c);
//            } else {
//                service.save(c);
//            }
//            updateList();
//            closeEditor();
//        } catch (ConstraintViolationException e) {
//            customerForm.setInvalidNumber();
//        }
//    }
//
//    private void deleteCustomer(CustomerForm.DeleteCustomerEvent event) {
//        try {
//            service.delete(event.getCustomer());
//            updateList();
//            closeEditor();
//        } catch (ConstraintViolationException e) {
//            showErrorNotification(e.getMessage());
//        }
//    }
//
//    private void editCustomer(CustomerCard c) {
//        if (c == null) {
//            closeEditor();
//        } else {
//            customerForm.setCustomer(c);
//            customerForm.open();
//            addClassName("editing");
//        }
//    }
//
//    private void addCustomer() {
//        table.asSingleSelect().clear();
//        editCustomer(new CustomerCard());
//    }
//
//    private void showErrorNotification(String message) {
//        Notification notification = new Notification();
//        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
//        Div text = new Div(new Text(message));
//        Button closeButton = new Button(new Icon("lumo", "cross"));
//        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
//        closeButton.setAriaLabel("Close");
//        closeButton.addClickListener(event -> notification.close());
//        HorizontalLayout layout = new HorizontalLayout(text, closeButton);
//        layout.setAlignItems(FlexComponent.Alignment.CENTER);
//        notification.setDuration(7000);
//        notification.add(layout);
//        notification.open();
//    }
//}
