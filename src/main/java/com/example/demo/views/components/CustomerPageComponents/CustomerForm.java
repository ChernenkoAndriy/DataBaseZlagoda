package com.example.demo.views.components.CustomerPageComponents;

import com.example.demo.views.events.CloseEvent;
import com.example.demo.views.events.DeleteEvent;
import com.example.demo.views.events.SaveEvent;
import com.example.demo.repositories.database_entities.CustomerCard;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.StringLengthValidator;

public class CustomerForm extends Dialog {
    private TextField surname = new TextField("Surname");
    private TextField name = new TextField("Name");
    private TextField patronymic = new TextField("Patronymic");
    private TextField phoneNumber = new TextField("Phone Number");
    private IntegerField percent = new IntegerField("Discount percent");

    // Address fields
    protected TextField cityField = new TextField("City");
    protected TextField streetField = new TextField("Street");
    protected TextField zipcode = new TextField("Zip Code");

    private Button saveButton = new Button("Save");
    private Button deleteButton = new Button("Delete");
    private Button closeButton = new Button("Cancel");

    private Binder<CustomerCard> binder = new Binder<>(CustomerCard.class);

    public CustomerForm() {
        configureUI();
        configureBinder();
        addListeners();
    }

    private void configureUI() {
        // Create FormLayout and set number of columns
        FormLayout formLayout = new FormLayout();
        this.setWidth("70%");
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("600px", 2)
        );
        saveButton.addThemeName("primary");
        deleteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                ButtonVariant.LUMO_ERROR);
        saveButton.addClickShortcut(Key.ENTER);
        closeButton.addClickShortcut(Key.ESCAPE);

        saveButton.setWidth("33%");
        deleteButton.setWidth("33%");
        closeButton.setWidth("33%");
        formLayout.add(surname, name, patronymic, phoneNumber, percent, cityField, streetField, zipcode);
        HorizontalLayout buttonsLayout = new HorizontalLayout(saveButton, deleteButton, closeButton);
        add(formLayout, buttonsLayout);
    }

    private void configureBinder() {
        // Surname: required, 1–50 chars
        binder.forField(surname)
                .asRequired("Surname is required")
                .withValidator(new StringLengthValidator("Surname must be between 1 and 50 characters", 1, 50))
                .bind(CustomerCard::getCustSurname, CustomerCard::setCustSurname);

        // Name: required, 1–50 chars
        binder.forField(name)
                .asRequired("Name is required")
                .withValidator(new StringLengthValidator("Name must be between 1 and 50 characters", 1, 50))
                .bind(CustomerCard::getCustName, CustomerCard::setCustName);

        // Patronymic: optional, max 50 chars
        binder.forField(patronymic)
                .withNullRepresentation("")
                .withValidator(value -> value == null || value.length() <= 50,
                        "Patronymic must be less than 50 characters")
                .bind(CustomerCard::getCustPatronymic, CustomerCard::setCustPatronymic);

        // Phone number: required, format +XXXXXXXXXXXX (12 digits)
        binder.forField(phoneNumber)
                .asRequired("Phone number is required")
                .withValidator(phone -> phone.matches("\\+?\\d{12}"),
                        "Invalid format. Use +XXXXXXXXXXXX")
                .bind(CustomerCard::getPhoneNumber, CustomerCard::setPhoneNumber);

        // Percent: optional, 0–100
        binder.forField(percent)
                .asRequired("Percent is required")
                .withValidator(p -> p == null || (p >= 0 && p <= 100),
                        "Percent must be between 0 and 100")
                .bind(CustomerCard::getPercent, CustomerCard::setPercent);

        // City: optional, 2–50 chars
        binder.forField(cityField)
                .withNullRepresentation("")
                .withValidator(city -> city == null || (city.length() >= 2 && city.length() <= 50),
                        "City must be between 2 and 50 characters")
                .withValidator(city -> city == null || city.matches("^[a-zA-Z\\s'-]+$"),
                        "City must contain only English letters")
                .bind(CustomerCard::getCity, CustomerCard::setCity);


        // Street: optional, 2–100 chars
        binder.forField(streetField)
                .withNullRepresentation("")
                .withValidator(street -> street == null || (street.length() >= 2 && street.length() <= 100),
                        "Street must be between 2 and 100 characters")
                .bind(CustomerCard::getStreet, CustomerCard::setStreet);

        // Zipcode: optional, must match 9 digits
        binder.forField(zipcode)
                .withNullRepresentation("")
                .withValidator(zip -> zip == null || zip.matches("\\d{9}"),
                        "Invalid zip code format. Must be 9 digits.")
                .bind(CustomerCard::getZipCode, CustomerCard::setZipCode);
    }


    private void addListeners() {
        binder.addStatusChangeListener(e -> saveButton.setEnabled(binder.isValid()));
        saveButton.addClickListener(e -> validateAndSave());
        deleteButton.addClickListener(e -> {
           fireEvent(new DeleteCustomerEvent(this, binder.getBean()));
        });
        closeButton.addClickListener(e -> {
           fireEvent(new CloseCustomerEvent(this));
        });
        saveButton.addClickShortcut(Key.ENTER);
        closeButton.addClickShortcut(Key.ESCAPE);
    }

    private void validateAndSave() {
        if(binder.isValid()) {
            fireEvent(new SaveCustomerEvent(this, binder.getBean()));
        }
    }

    public void setCustomerCard(CustomerCard customerCard) {
        binder.setBean(customerCard);
    }

    public void addDeleteListener(ComponentEventListener<DeleteCustomerEvent> listener) {
        addListener(DeleteCustomerEvent.class, listener);
    }

    public void addSaveListener(ComponentEventListener<SaveCustomerEvent> listener) {
        addListener(SaveCustomerEvent.class, listener);
    }

    public void addCloseListener(ComponentEventListener<CloseCustomerEvent> listener) {
        addListener(CloseCustomerEvent.class, listener);
    }

    public void setPhoneAlert() {
        phoneNumber.setInvalid(true);
        phoneNumber.setErrorMessage("Such phone already exists");
    }

    public void setDeleteButton(boolean b) {
        deleteButton.setEnabled(b);
    }

    public static class CloseCustomerEvent extends CloseEvent<CustomerForm> {
        public CloseCustomerEvent(CustomerForm customerForm) {
            super(customerForm);
        }
    }

    public static class SaveCustomerEvent extends SaveEvent<CustomerForm, CustomerCard> {
        public SaveCustomerEvent(CustomerForm customerForm, CustomerCard e) {
            super(customerForm, e);
        }
    }

    public static class DeleteCustomerEvent extends DeleteEvent<CustomerForm, CustomerCard> {
        public DeleteCustomerEvent(CustomerForm customerForm, CustomerCard e) {
            super(customerForm, e);
        }
    }
}
