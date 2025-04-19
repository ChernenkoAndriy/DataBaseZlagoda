package com.example.demo.views.components.CheckPageComponents;

import com.example.demo.views.components.EmployeePageComponents.EmployeeForm;
import com.example.demo.views.events.CloseEvent;
import com.example.demo.views.events.DeleteEvent;
import com.example.demo.views.events.SaveEvent;
import com.example.demo.views.repositories.database_entities.*;
import com.example.demo.views.services.CheckService;
import com.example.demo.views.services.CustomerService;
import com.example.demo.views.services.StoreProductService;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
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
    private CheckService checkService;

    private NumberField priceField = new NumberField("Initial Price");
    private NumberField finalPriceField = new NumberField("Final Price");
    private NumberField vat = new NumberField("Vat");
    private NumberField promPercent = new NumberField("Sale");
    private DateTimePicker timeOfPrinting;

    public CheckForm(CustomerService customerService, StoreProductService storeProductService, CheckService checkService) {
        this.customerService = customerService;
        this.storeProductService = storeProductService;
        this.checkService = checkService;
        this.checkFormBody = new CheckFormBody(checkService);
        configureLayout();
        configureBinder();
        configureLogic();
        this.setWidth("70%");
        configureData(customerService.getAllEntities(), storeProductService.getAllEntities());

    }

    private void configureBinder() {
        binder.forField(cashierPhone).bind(Check::getCashier, Check::setCashier);
        binder.forField(customerPhone).bind(Check::getCustomer, Check::setCustomer);
        binder.forField(timeOfPrinting).bind(Check::getPrint_date, Check::setPrint_date);
    }

    private void configureLayout() {
        cashierPhone = new ComboBox<>("Cashier Phone");
        cashierPhone.setWidth("50%");
        customerPhone = new ComboBox<>("Customer Phone");
        customerPhone.setWidth("50%");
        customerPhone.setReadOnly(false);
        storeProductChooser = new ComboBox<>();
        addProductButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        timeOfPrinting = new DateTimePicker("Enter date and time of printing");
        FormLayout formLayout = new FormLayout();
        formLayout.setWidthFull();
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 2));
        formLayout.add(cashierPhone, customerPhone);
        HorizontalLayout searchLayout = new HorizontalLayout(storeProductChooser, addProductButton, timeOfPrinting);
        searchLayout.setAlignItems(FlexComponent.Alignment.BASELINE);
        priceField.setLabel("Total Price");
        priceField.setReadOnly(false);
        vat.setLabel("Vat");
        vat.setReadOnly(false);
        promPercent.setLabel("Prom");
        promPercent.setReadOnly(false);
        finalPriceField.setLabel("Final Price");
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
        saveButton.addClickListener(event -> fireEvent(new SaveCheckEvent(this, binder.getBean())));
        deleteButton.addClickListener(event  -> fireEvent(new DeleteCheckEvent(this, binder.getBean())));
        closeButton.addClickListener(event -> fireEvent(new CloseCheckEvent(this)));
        checkFormBody.addUpdateListener(e -> updatePrice(e.getDelta()));
        addProductButton.addClickListener(e -> {
            Store_Product sp = storeProductChooser.getValue();
            checkFormBody.addProduct(sp);
            updatePrice(sp.getSelling_price());
                }
        );
    }


    public void setCheck(Check check) {
        if(check == null){
            customerPhone.clear();
            cashierPhone.clear();
            binder.setBean(null);
            checkFormBody.setCheck(check);
        }else {
            this.check = check;
            if (check.getGoods().isEmpty()) {
                customerPhone.setReadOnly(false);
                customerPhone.addValueChangeListener(e -> updatePrice(BigDecimal.ZERO));
            }
            binder.setBean(check);
            checkFormBody.setCheck(check);
            updatePrice(BigDecimal.ZERO);
        }
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

    private void updatePrice(BigDecimal delta) {
        if (delta == null || check == null) {
            return;
        }

        BigDecimal sumTotal = check.getSum_total();
        if (sumTotal == null) {
            sumTotal = BigDecimal.ZERO;
        }
        sumTotal = sumTotal.add(delta);
        check.setSum_total(sumTotal);

        if (priceField != null) {
            priceField.setValue(sumTotal.doubleValue());
        }

        double percent = 0.0;
        if (check.getCustomer() != null) {
            percent = check.getCustomer().getPercent() / 100.0;
        }
        if (promPercent != null) {
            promPercent.setValue(percent);
        }
        if(customerPhone.getValue()!=null){
            promPercent.setValue(customerPhone.getValue().getPercent()/100.0);
        }

        BigDecimal vatValue = check.getVat();
        if (vatValue == null) {
            vatValue = BigDecimal.valueOf(priceField.getValue()).multiply(BigDecimal.valueOf(0.2));
        }
        if (vat != null) {
            vat.setValue(vatValue.doubleValue());
        }

        double finalPrice = sumTotal.doubleValue() * 1.2 * (1 - percent);
        if (finalPriceField != null) {
            finalPriceField.setValue(finalPrice);
        }
    }

    public void addSaveListener(ComponentEventListener<SaveCheckEvent> listener) {
        addListener(SaveCheckEvent.class, listener);
    }

    public void addDeleteListener(ComponentEventListener<DeleteCheckEvent> listener) {
        addListener(DeleteCheckEvent.class, listener);
    }

    public void addCloseListener(ComponentEventListener<CloseCheckEvent> listener) {
        addListener(CloseCheckEvent.class, listener);
    }

    public static class CloseCheckEvent extends CloseEvent<CheckForm> {
        public CloseCheckEvent(CheckForm checkForm) {
            super(checkForm);
        }
    }
    public static class SaveCheckEvent extends SaveEvent<CheckForm, Check> {
        public SaveCheckEvent(CheckForm checkForm, Check check) {
            super(checkForm, check);
        }
    }

    public static class DeleteCheckEvent extends DeleteEvent<CheckForm, Check>{
        public DeleteCheckEvent(CheckForm checkForm, Check check) {
            super(checkForm, check);
        }
    }
}
