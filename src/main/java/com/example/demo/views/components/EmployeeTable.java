package com.example.demo.views.components;

import com.example.demo.views.viewmanagers.MEService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import database_manegment.database_entities.Employee;
import jakarta.validation.ConstraintViolationException;

public class EmployeeTable extends Grid<Employee> {
    private MEService MEService;
    private Dialog editEmployeeDialog;

    public EmployeeTable(ListDataProvider<Employee> dataProvider, MEService MEService) {
        this.MEService = MEService;
        setItems(dataProvider);
        configureColumns();
        MEService.fillTheTable();
        dataProvider.refreshAll();

        addRowMenu();
    }

    private void configureColumns() {
        addColumn(Employee::getEmpl_surname).setHeader("Surname").setSortable(true);
        addColumn(Employee::getEmpl_name).setHeader("Name").setSortable(true);
        addColumn(Employee::getEmpl_patronymic).setHeader("Patronymic").setSortable(true);
        addColumn(Employee::getEmpl_role).setHeader("Role").setSortable(true);
        addColumn(Employee::getDate_of_birth).setHeader("Birthdate").setSortable(true);
        addColumn(Employee::getDate_of_start).setHeader("Date of start");
        addColumn(Employee::getCity).setHeader("City");

        Grid.Column<Employee> streetColumn = addColumn(Employee::getStreet).setHeader("Street");
        Grid.Column<Employee> phoneColumn = addColumn(Employee::getPhone_number).setHeader("Phone");

        addColumn(Employee::getSalary).setHeader("Salary");
        addColumn(Employee::getZip_code).setHeader("Zip");

        phoneColumn.setWidth("10%");
        streetColumn.setWidth("10%");
    }

    private void addRowMenu() {
        GridContextMenu<Employee> menu = new GridContextMenu<>(this);

        menu.addItem("Edit", event -> event.getItem().ifPresent(this::openEditDialog));
        menu.addItem("Delete", event -> event.getItem().ifPresent(this::openDeleteDialog));
    }

    private void openEditDialog(Employee employee) {
        editEmployeeDialog = new Dialog();
        editEmployeeDialog.setWidth("70%");
        editEmployeeDialog.setModal(true);
        editEmployeeDialog.add(new EditEmployeeForm(MEService, editEmployeeDialog, employee));
        editEmployeeDialog.open();
    }

    private void openDeleteDialog(Employee employee) {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("You sure you want to delete this employee?");
        dialog.setText("This action can't be undone.");
        dialog.setCancelable(true);
        dialog.setConfirmText("Delete");
        dialog.setConfirmButtonTheme("error primary");

        dialog.addConfirmListener(ev -> {
            try {
                MEService.deleteEmployee(employee);
            } catch (ConstraintViolationException e) {
                showErrorNotification(e.getMessage());
            }
        });

        dialog.open();
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

        notification.add(layout);
        notification.open();
    }
}
