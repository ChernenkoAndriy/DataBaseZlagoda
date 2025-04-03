package com.example.demo.views.components;

import com.example.demo.views.viewmanagers.MCService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;

public class CustomerForm extends VerticalLayout {
    protected MCService mcService;
    protected final TextField nameField = new TextField("Name");
    protected final TextField surnameField = new TextField("Surname");
    protected final TextField patronymic = new TextField("Patronymic");
    protected final TextField phoneField = new TextField("Phone Number");
    protected final TextField cityField = new TextField("City");
    protected final TextField streetField = new TextField("Street");
    protected final TextField zipcode = new TextField("Zip Code");
    private final NumberField discountField = new NumberField("Discount Percentage");
    protected final Button saveButton = new Button("Save");

    public CustomerForm(MCService mcService) {
        this.mcService = mcService;
        setJustifyContentMode(JustifyContentMode.CENTER);
        customizeElements();

        // Додаємо форму зі всіма полями
        FormLayout formLayout = new FormLayout(nameField, surnameField, patronymic, phoneField, cityField, streetField, zipcode, discountField);
        add(formLayout, saveButton);
    }

    private void customizeElements() {
        // Налаштовуємо поля для вводу
        configureTextField(nameField, "Name", 1, 50, true);
        configureTextField(surnameField, "Surname", 1, 50, true);
        configureTextField(patronymic, "Patronymic", 0, 50, false);
        configureTextField(cityField, "City", 1, 50, true);
        configureTextField(streetField, "Street", 1, 50, true);
        configureTextField(zipcode, "Zip Code", 5, 9, true);

        // Налаштовуємо номер телефону
        phoneField.setRequiredIndicatorVisible(true);
        phoneField.setPattern("\\d{12}");
        phoneField.setAllowedCharPattern("[0-9+]");
        phoneField.setMaxLength(13);
        phoneField.setI18n(new TextField.TextFieldI18n()
                .setRequiredErrorMessage("Phone number is required")
                .setPatternErrorMessage("Invalid phone number format"));

        // Налаштовуємо кнопку Save
        saveButton.setWidth("100%");
        saveButton.addThemeName("primary");

        // Налаштовуємо поле для знижки
        discountField.setMin(0);
        discountField.setMax(100);
        discountField.setStep(1);
        discountField.setSuffixComponent(new Span("%"));

        // Додаємо деякі візуальні налаштування для суфікса
        discountField.setWidth("100%");
        discountField.setRequiredIndicatorVisible(true);

        // Налаштовуємо поле з поштовим індексом
        zipcode.setAllowedCharPattern("[0-9]");
    }

    private void configureTextField(TextField field, String label, int minLength, int maxLength, boolean required) {
        field.setLabel(label);
        field.setMaxLength(maxLength);
        if (required) {
            field.setRequiredIndicatorVisible(true);
        }
        field.setI18n(new TextField.TextFieldI18n()
                .setRequiredErrorMessage(label + " is required")
                .setMaxLengthErrorMessage("Maximum length is " + maxLength + " characters"));
    }
}
