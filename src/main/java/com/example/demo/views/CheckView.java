package com.example.demo.views;

import com.example.demo.views.components.CheckPageComponents.CheckForm;
import com.example.demo.views.components.CheckPageComponents.CheckTable;
import com.example.demo.views.components.CheckPageComponents.CheckToolbar;
import com.example.demo.views.components.EmployeePageComponents.EmployeeForm;
import com.example.demo.views.repositories.StoreProductRepository;
import com.example.demo.views.repositories.database_entities.Check;
import com.example.demo.views.services.CheckService;
import com.example.demo.views.services.CustomerService;
import com.example.demo.views.services.StoreProductService;
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
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.annotation.Scope;

@Route(value = "checks", layout = ManagerLayout.class)
@SpringComponent
@Scope("prototype")
@PageTitle("Checks | ZLAGODA")
public class CheckView extends AppLayout {
    protected CheckTable table;
    protected CheckToolbar bar;
    protected CheckService checkService;
    protected CustomerService customerService;
    protected StoreProductService storeProductService;
    protected CheckForm checkForm;
    public CheckView(CheckService checkService, CustomerService customerService, StoreProductService storeProductService) {
        this.checkForm = new CheckForm(customerService, storeProductService);
        this.checkService = checkService;
        this.customerService=customerService;
        this.storeProductService=storeProductService;
        this.table = new CheckTable(checkService);
        this.bar = new CheckToolbar();
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
     //   checkForm.addSaveListener(this::saveEmployee);
      //  checkForm.addDeleteListener(this::deleteEmployee);
     //   checkForm.addCloseListener(e -> closeEditor());
        bar.getAddButton().addClickListener(e -> addEmployee());
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
    private void saveEmployee(EmployeeForm.SaveEmployeeEvent event) {
        try {
            Employee e = event.getEmployee();
            if (e.getId() == null) {
     //           service.addEntity(e);
            } else {
    //            service.updateEntity(e);
            }
            updateList();
            closeEditor();
        }catch (ConstraintViolationException e){
        }
    }
    private void deleteCheck(EmployeeForm.DeleteEmployeeEvent event) {
        try {
  //          service.deleteEntity(event.getEmployee().getId());
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
    private void addEmployee(){
        table.asSingleSelect().clear();
        editCheck(new Check());
    }
}
