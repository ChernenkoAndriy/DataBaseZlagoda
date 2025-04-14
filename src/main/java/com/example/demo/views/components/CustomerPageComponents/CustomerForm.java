package com.example.demo.views.components.CustomerPageComponents;

import com.example.demo.views.repositories.database_entities.CustomerCard;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.notification.Notification;


public class CustomerForm extends FormLayout {

    private CustomerCard customer;

    private final TextField surname = new TextField("Surname");
    private final TextField name = new TextField("Name");
    private final TextField patronymic = new TextField("Patronymic");
    private final TextField phone = new TextField("Phone");
    private final TextField city = new TextField("City");
    private final TextField street = new TextField("Street");
    private final TextField zip = new TextField("Zip code");
    private final NumberField percent = new NumberField("Percent");

    private final Button save = new Button("Save");
    private final Button delete = new Button("Delete");
    private final Button close = new Button("Cancel");

    public CustomerForm() {
        add(surname, name, patronymic, phone, city, street, zip, percent, createButtonsLayout());
        setVisible(false);
    }

    private Component createButtonsLayout() {
        save.addClickListener(e -> fireEvent(new SaveCustomerEvent(this, customer)));
        delete.addClickListener(e -> fireEvent(new DeleteCustomerEvent(this, customer)));
        close.addClickListener(e -> fireEvent(new CloseCustomerEvent(this)));

        return new HorizontalLayout(save, delete, close);
    }

    public void setCustomer(CustomerCard c) {
        this.customer = c;
        if (c != null) {
            surname.setValue(c.getCustSurname() != null ? c.getCustSurname() : "");
            name.setValue(c.getCustName() != null ? c.getCustName() : "");
            patronymic.setValue(c.getCustPatronymic() != null ? c.getCustPatronymic() : "");
            phone.setValue(c.getPhoneNumber() != null ? c.getPhoneNumber() : "");
            city.setValue(c.getCity() != null ? c.getCity() : "");
            street.setValue(c.getStreet() != null ? c.getStreet() : "");
            zip.setValue(c.getZipCode() != null ? c.getZipCode() : "");
            percent.setValue((double) c.getPercent());
        }
        setVisible(c != null);
    }

    public void close() {
        setVisible(false);
    }

    public static class SaveCustomerEvent extends ComponentEvent<CustomerForm> {
        private final CustomerCard customer;
        public SaveCustomerEvent(CustomerForm source, CustomerCard customer) {
            super(source, false);
            this.customer = customer;
        }
        public CustomerCard getCustomer() {
            return customer;
        }
    }

    public static class DeleteCustomerEvent extends ComponentEvent<CustomerForm> {
        private final CustomerCard customer;
        public DeleteCustomerEvent(CustomerForm source, CustomerCard customer) {
            super(source, false);
            this.customer = customer;
        }
        public CustomerCard getCustomer() {
            return customer;
        }
    }

    public static class CloseCustomerEvent extends ComponentEvent<CustomerForm> {
        public CloseCustomerEvent(CustomerForm source) {
            super(source, false);
        }
    }

    public Registration addSaveListener(ComponentEventListener<SaveCustomerEvent> listener) {
        return addListener(SaveCustomerEvent.class, listener);
    }

    public Registration addDeleteListener(ComponentEventListener<DeleteCustomerEvent> listener) {
        return addListener(DeleteCustomerEvent.class, listener);
    }

    public Registration addCloseListener(ComponentEventListener<CloseCustomerEvent> listener) {
        return addListener(CloseCustomerEvent.class, listener);
    }
    public void setInvalidNumber() {
        Notification.show("Invalid phone number");
    }
    public void open() {
        this.setVisible(true);
    }

}
