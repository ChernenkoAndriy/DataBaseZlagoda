package com.example.demo.views.components.CheckPageComponents;

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
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.binder.Binder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class CheckForm extends Dialog {
    private final Binder<Check> binder = new Binder<>(Check.class);
    private ComboBox<Employee> cashierPhone;
    private ComboBox<CustomerCard> customerPhone;
    private ComboBox<StoreProduct> storeProductChooser;
    private Check check;
    private Button addProductButton = new Button("Add product");
    private final Button deleteButton = new Button("Delete");
    private final Button closeButton = new Button("Cancel");
    private final Button saveButton = new Button("Save");

    private final CheckFormBody checkFormBody;

    private CustomerService customerService;
    private StoreProductService storeProductService;
    private CheckService checkService;

    private BigDecimalField priceField = new BigDecimalField("Total Price");
    private BigDecimalField vat = new BigDecimalField("Vat");
    private NumberField promPercent = new NumberField("Sale");
    private DateTimePicker timeOfPrinting;

    public CheckForm(CustomerService customerService, StoreProductService storeProductService, CheckService checkService) {
        this.customerService = customerService;
        this.storeProductService = storeProductService;
        this.checkService = checkService;
        this.checkFormBody = new CheckFormBody(checkService);
        configureLayout();
        configureBinder();
        configureData(customerService.getAllEntities(), storeProductService.getAllEntitiesWithPromNull());
        configureLogic();

    }

    private void configureBinder() {
        binder.forField(cashierPhone).asRequired("Cashier is required").bind(Check::getCashier, Check::setCashier);
        binder.forField(customerPhone).withNullRepresentation(new CustomerCard()).bind(Check::getCustomer, Check::setCustomer);
        binder.forField(timeOfPrinting)
                .asRequired("Date and time is required")
                .bind(Check::getPrint_date, Check::setPrint_date);
        binder.forField(vat).bind(Check::getVat, Check::setVat);
        binder.forField(priceField).bind(Check::getSum_total, Check::setSum_total);
    }

    private void configureLogic() {
        saveButton.addClickShortcut(Key.ENTER);
        closeButton.addClickShortcut(Key.ESCAPE);
        binder.addStatusChangeListener(e -> {
            if(checkFormBody.getCheckItems() != null) {
                saveButton.setEnabled(binder.isValid() && !checkFormBody.getCheckItems().isEmpty());
            }else{
                saveButton.setEnabled(false);
            }
        });
        saveButton.addClickListener(event -> validateAndSave());
        deleteButton.addClickListener(event -> fireEvent(new DeleteCheckEvent(this, binder.getBean())));
        closeButton.addClickListener(event -> fireEvent(new CloseCheckEvent(this)));

        checkFormBody.addUpdateListener(e -> updatePrice(e.getDelta()));
        addProductButton.addClickListener(e -> {
                    StoreProduct sp = storeProductChooser.getValue();
                    if(checkFormBody.addProduct(sp)){
                fireEvent(new CheckFormBody.UpdateTotalPriceEvent(checkFormBody, sp.getSelling_price()));
                        updatePrice(sp.getSelling_price());
            }
                }
        );

        customerPhone.addValueChangeListener(e -> {
            if (customerPhone.getValue() != null) {
                promPercent.setValue(customerPhone.getValue().getPercent() / 100.0);
            } else {
                promPercent.setValue(0.0);
            }
        });

    }

    public void setCheck(Check check) {
        if (check != null) {
            if (check.getCheck_number()!=null) {
                customerPhone.setReadOnly(true);
                cashierPhone.setReadOnly(true);
                binder.setBean(check);
            } else {
                customerPhone.setReadOnly(false);
                binder.setBean(check);
                timeOfPrinting.setValue(LocalDateTime.now());
            }
        }
        checkFormBody.setCheck(check);
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            Check currentCheck = binder.getBean();
            currentCheck.setGoods(checkFormBody.getCheckItems());
            fireEvent(new SaveCheckEvent(this, currentCheck));
        }
    }

    private void configureData(List<CustomerCard> customers, List<StoreProduct> storeProducts) {
        customerPhone.setItems(customers);
        storeProductChooser.setItems(storeProducts);
        storeProductChooser.setItemLabelGenerator(StoreProduct::getProduct);
        cashierPhone.setItems(new Employee());
        cashierPhone.setReadOnly(true);
        customerPhone.setItemLabelGenerator(c -> {
            if (c == null) return "";
            return (c.getPhoneNumber() != null ? c.getPhoneNumber() : "") + " " +
                    (c.getCustSurname() != null ? c.getCustSurname() : "") + " " +
                    (c.getCustName() != null ? c.getCustName() : "");
        });

        cashierPhone.setItemLabelGenerator(e -> {
            if (e == null) return "";
            return (e.getPhone_number() != null ? e.getPhone_number() : "") + " " +
                    (e.getEmpl_surname() != null ? e.getEmpl_surname() : "") + " " +
                    (e.getEmpl_name() != null ? e.getEmpl_name() : "");
        });

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

    public void setEditable(boolean b) {
        saveButton.setEnabled(b);
        timeOfPrinting.setEnabled(b);
        cashierPhone.setEnabled(b);
        customerPhone.setEnabled(b);
        promPercent.setReadOnly(true);
        addProductButton.setEnabled(b);
        storeProductChooser.setEnabled(b);
        priceField.setReadOnly(true);
        vat.setReadOnly(true);
        checkFormBody.setEnabled(b);
        deleteButton.setEnabled(!b);
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

    public static class DeleteCheckEvent extends DeleteEvent<CheckForm, Check> {
        public DeleteCheckEvent(CheckForm checkForm, Check check) {
            super(checkForm, check);
        }
    }

    private void updatePrice(BigDecimal delta) {
        if (priceField.getValue() == null) {
            priceField.setValue(new BigDecimal(0));
        }
        priceField.setValue(priceField.getValue().add(delta));
        vat.setValue(priceField.getValue().multiply(new BigDecimal("0.2")));
    }

    private void configureLayout() {
        this.setWidth("70%");
        cashierPhone = new ComboBox<>("Cashier Phone");
        cashierPhone.setWidth("50%");
        customerPhone = new ComboBox<>("Customer Phone");
        customerPhone.setWidth("50%");
        storeProductChooser = new ComboBox<>();
        addProductButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        timeOfPrinting = new DateTimePicker("Enter date and time of printing");
        timeOfPrinting.setMax(LocalDateTime.now().plusMinutes(4));
        timeOfPrinting.setMin(LocalDateTime.now().minusYears(100));
        FormLayout formLayout = new FormLayout();
        formLayout.setWidthFull();
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 2));
        formLayout.add(cashierPhone, customerPhone);
        HorizontalLayout searchLayout = new HorizontalLayout(storeProductChooser, addProductButton, timeOfPrinting);
        searchLayout.setAlignItems(FlexComponent.Alignment.BASELINE);
        priceField.setLabel("Total Price");
        vat.setLabel("Vat");
        promPercent.setLabel("Prom");
        FormLayout priceLayout = new FormLayout();
        priceLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));
        priceLayout.add(priceField, vat, promPercent);
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
}
