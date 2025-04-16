package com.example.demo.views.components.CheckPageComponents;

import com.example.demo.views.repositories.StoreProductRepository;
import com.example.demo.views.repositories.database_entities.*;
import com.example.demo.views.services.CustomerService;
import com.example.demo.views.services.StoreProductService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.binder.Binder;

import java.math.BigDecimal;
import java.util.List;

public class CheckForm extends Dialog {
    private final Binder<Check> binder = new Binder<>(Check.class);
    private ComboBox<Employee> cashierPhone;
    private ComboBox<CustomerCard> customerPhone;
    private ComboBox<Store_Product> storeProductChooser;
    private Check check;
    private Button addProductButton = new Button("Add product");
    private final Button deleteButton = new Button("Delete");
    private final Button closeButton = new Button("Cancel");
    private final Button saveButton = new Button("Save");

    private final CheckFormBody checkFormBody;

    private CustomerService customerService;
    private StoreProductService storeProductService;

    private NumberField priceField = new NumberField("Initial Price");
    private NumberField finalPriceField = new NumberField("Final Price");
    private NumberField vat = new NumberField("Vat");
    private NumberField promPercent = new NumberField("Sale");

    public CheckForm(CustomerService customerService, StoreProductService storeProductService) {
        this.customerService = customerService;
        this.storeProductService = storeProductService;
        this.checkFormBody = new CheckFormBody();
        configureLayout();
        configureBinder();
        configureLogic();
        this.setWidth("70%");
        configureData(customerService.getAllEntities(), storeProductService.getAllEntities());
    }

    private void configureBinder() {
        binder.forField(cashierPhone).bind(Check::getCashier, null);
        binder.forField(customerPhone).bind(Check::getCustomer, null);
    }

    private void configureLayout() {
        cashierPhone = new ComboBox<>("Cashier Phone");
        cashierPhone.setWidth("50%");
        customerPhone = new ComboBox<>("Customer Phone");
        customerPhone.setWidth("50%");
        customerPhone.setReadOnly(false);
        storeProductChooser = new ComboBox<>();
        addProductButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        FormLayout formLayout = new FormLayout();
        formLayout.setWidthFull();
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 2));
        formLayout.add(cashierPhone, customerPhone);
        HorizontalLayout searchLayout = new HorizontalLayout(storeProductChooser, addProductButton);
        priceField.setLabel("Ціна");
        priceField.setReadOnly(false);
        vat.setLabel("ПДВ");
        vat.setReadOnly(false);
        promPercent.setLabel("Знижка");
        promPercent.setReadOnly(false);
        finalPriceField.setLabel("Фінальна ціна");
        finalPriceField.setReadOnly(false);
        FormLayout priceLayout = new FormLayout();
        priceLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));
        priceLayout.add(priceField, vat, promPercent, finalPriceField);
        saveButton.addThemeName("primary");
        deleteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        saveButton.setWidth("33%");
        deleteButton.setWidth("33%");
        closeButton.setWidth("33%");
        HorizontalLayout buttons = new HorizontalLayout(saveButton, deleteButton, closeButton);
        buttons.setWidth("100%");
        buttons.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        priceLayout.setWidth("25%");
        this.add(formLayout, searchLayout, checkFormBody, priceLayout, buttons);
    }

    private void configureLogic() {
        saveButton.addClickShortcut(Key.ENTER);
        closeButton.addClickShortcut(Key.ESCAPE);
        binder.addStatusChangeListener(e -> saveButton.setEnabled(binder.isValid()));
        saveButton.addClickListener(event -> validateAndSave());
        deleteButton.addClickListener(event -> this.close());
        closeButton.addClickListener(event -> this.close());
        addProductButton.addClickListener(event -> {
            Store_Product selectedProduct = storeProductChooser.getValue();
            if (selectedProduct != null) {
                checkFormBody.addProduct(selectedProduct);
            }
        });
        this.addListener(CheckFormLine.UpdateCheckSum.class, e -> updatePrice());
    }


    public void setCheck(Check e) {
        this.check = e;
        binder.setBean(e);
        checkFormBody.setCheck(e);
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            Check currentCheck = binder.getBean();
            this.close();
        }
    }

    private void configureData(List<CustomerCard> customers, List<Store_Product> storeProducts) {
        customerPhone.setItems(customers);
        customerPhone.setItemLabelGenerator(c ->
                c.getPhoneNumber() + " " + c.getCustSurname() + " " + c.getCustName());

        storeProductChooser.setItems(storeProducts);
        storeProductChooser.setItemLabelGenerator(sp ->
                sp.getProduct() + (sp.isPromotional_product() ? " prom" : ""));

        cashierPhone.setItems(new Employee());
        cashierPhone.setReadOnly(true);
        cashierPhone.setItemLabelGenerator(e ->
                e.getPhone_number() + " " + e.getEmpl_surname() + " " + e.getEmpl_name());
    }

    private void setCashier(Employee employee) {
        cashierPhone.setItems(employee);
    }

    private void updatePrice() {
        BigDecimal sumTotal = check.getSum_total();
        this.priceField.setValue(sumTotal.doubleValue());

        double percent = check.getCustomer().getPercent() / 100.0;
        this.promPercent.setValue(percent);

        BigDecimal vatValue = check.getVat();
        this.vat.setValue(vatValue.doubleValue());

        double finalPrice = sumTotal.doubleValue() * 1.2 * (1 - percent);
        this.finalPriceField.setValue(finalPrice);
    }
}
