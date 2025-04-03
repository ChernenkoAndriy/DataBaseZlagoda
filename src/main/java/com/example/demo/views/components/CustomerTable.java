package com.example.demo.views.components;

import com.example.demo.views.viewmanagers.MCService;
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
import database_manegment.database_entities.CustomerCard;
import jakarta.validation.ConstraintViolationException;

public class CustomerTable extends Grid<CustomerCard> {
    private MCService mcService;
    private Dialog editCustomerDialog;

    public CustomerTable(ListDataProvider<CustomerCard> dataProvider, MCService mcService) {
        this.mcService = mcService;
        setItems(dataProvider);
        configureColumns();
      // mcService.fillTheTable();
        dataProvider.refreshAll();

        addRowMenu();
    }

    private void configureColumns() {
        Grid.Column<CustomerCard> surname = addColumn(CustomerCard::getCust_surname).setHeader("Surname");
        Grid.Column<CustomerCard> name = addColumn(CustomerCard::getCust_name).setHeader("Name");
        Grid.Column<CustomerCard> patronymic= addColumn(CustomerCard::getCust_patronymic).setHeader("Patronymic");
        Grid.Column<CustomerCard> phoneColumn = addColumn(CustomerCard::getPhone_number).setHeader("Phone");
        Grid.Column<CustomerCard> city = addColumn(CustomerCard::getCity).setHeader("City");
        Grid.Column<CustomerCard> streetColumn = addColumn(CustomerCard::getStreet).setHeader("Street");
        Grid.Column<CustomerCard> zip_code = addColumn(CustomerCard::getZip_code).setHeader("Zip code");
        Grid.Column<CustomerCard> percent = addColumn(CustomerCard::getPercent).setHeader("Percent");

        phoneColumn.setWidth("10%");
        streetColumn.setWidth("10%");
    }

    private void addRowMenu() {
        GridContextMenu<CustomerCard> menu = new GridContextMenu<>(this);
        menu.addItem("Edit", event -> event.getItem().ifPresent(this::openEditDialog));
        menu.addItem("Delete", event -> event.getItem().ifPresent(this::openDeleteDialog));
    }

    private void openEditDialog(CustomerCard customerCard) {
        editCustomerDialog = new Dialog();
        editCustomerDialog.setWidth("70%");
        editCustomerDialog.setModal(true);
 //       editEmployeeDialog.add(new EditEmployeeForm(MEService, editEmployeeDialog, employee));
        editCustomerDialog.open();
    }

    private void openDeleteDialog(CustomerCard customerCard) {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("You sure you want to delete this customer?");
        dialog.setText("This action can't be undone.");
        dialog.setCancelable(true);
        dialog.setConfirmText("Delete");
        dialog.setConfirmButtonTheme("error primary");

        dialog.addConfirmListener(ev -> {
            try {
             //   MEService.deleteEmployee(employee);
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
