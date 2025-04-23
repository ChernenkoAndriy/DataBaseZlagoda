package com.example.demo.views;

import com.example.demo.views.components.CheckPageComponents.CheckForm;
import com.example.demo.views.components.CheckPageComponents.CheckTable;
import com.example.demo.views.components.CheckPageComponents.CheckToolbar;
import com.example.demo.views.components.EmployeePageComponents.EmployeeForm;
import com.example.demo.views.repositories.database_entities.Check;
import com.example.demo.views.services.*;
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
import com.example.demo.views.repositories.database_entities.Employee;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.Objects;

@Route(value = "checks", layout = MainLayout.class)
@SpringComponent
@PermitAll
@Scope("prototype")
@PageTitle("Checks | ZLAGODA")
public class CheckView extends AppLayout {
    protected CheckTable table;
    protected CheckToolbar bar;
    protected CheckService checkService;
    protected CustomerService customerService;
    protected StoreProductService storeProductService;
    protected EmployeeService employeeService;
    protected CheckForm checkForm;
    private MyUserDetails user;
    public CheckView(CheckService checkService, CustomerService customerService, StoreProductService storeProductService, EmployeeService employeeService) {
        this.checkForm = new CheckForm(customerService, storeProductService, checkService);
        this.checkService = checkService;
        this.customerService=customerService;
        this.storeProductService=storeProductService;
        this.table = new CheckTable(checkService);
        this.bar = new CheckToolbar();
        this.employeeService = employeeService;
        this.user = MyUserDetailsService.getCurrentUser();
        if(Objects.equals(user.getRole(), "Manager")){
            checkForm.setEditable(false);
        }else if(Objects.equals(user.getRole(), "Cashier")){
            checkForm.setEditable(true);
        }
        configureContent();
    }
    private void configureContent() {
        bar.setWidth("100%");
        table.setMinWidth("130%");
        table.asSingleSelect().addValueChangeListener(event ->
                editCheck(event.getValue()));
        VerticalLayout tableContainer = new VerticalLayout(table);
        tableContainer.setSizeFull();
        tableContainer.setPadding(false);
        tableContainer.getStyle().set("overflow", "auto");
        VerticalLayout content = new VerticalLayout(bar, tableContainer);
        content.setSizeFull();
        setContent(content);
        checkForm.addSaveListener(e -> saveCheck(e));
        checkForm.addDeleteListener(this::deleteCheck);
        checkForm.addCloseListener(e -> closeEditor());
        bar.getAddButton().addClickListener(e -> addCheck());
        bar.addUpdateListener(e -> updateList());
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
    private void closeEditor() {
        checkForm.setCheck(null);
        checkForm.close();
    }
    private void updateList() {
        table.setItems(bar.getAllByfilters(checkService));
    }
    private void saveCheck(CheckForm.SaveCheckEvent event) {
        try {
            Check e = event.getEntity();
            if (e.getId() == null) {
                checkService.addEntity(e);
            } else {
                checkService.updateEntity(e);
            }
            updateList();
            closeEditor();
        }catch (ConstraintViolationException e){

        }
    }
    private void deleteCheck(CheckForm.DeleteCheckEvent event) {
        try {
            checkService.deleteEntity(event.getEntity().getId());
            updateList();
            closeEditor();
        } catch (ConstraintViolationException er) {
            showErrorNotification(er.getMessage());
        }
    }
    private void editCheck(Check e) {
        if (e == null) {
            closeEditor();
        } else {
            checkService.setGoodsForCheck(e);
            checkForm.setCheck(e);
            checkForm.open();
            addClassName("editing");
        }
    }
    private void addCheck(){
        table.asSingleSelect().clear();
        Check check = new Check();
        check.setGoods(new ArrayList<>());
        check.setCashier(employeeService.findById(user.getId()));
        editCheck(check);
    }
}
