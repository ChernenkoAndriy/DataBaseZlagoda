package com.example.demo.views.components.StoreProductPageComponents;

import com.example.demo.views.events.UpdateEvent;
import com.example.demo.repositories.database_entities.StoreProduct;
import com.example.demo.services.StoreProductService;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;

public class AddProductsForm extends Dialog {
    private final IntegerField amountField = new IntegerField("Enter how many to add");
    private final ComboBox<StoreProduct> selector = new ComboBox<>("Select where to add");
    private final BigDecimalField price = new BigDecimalField("Set new price");
    private final Button saveButton = new Button("Save");
    private final Button cancelButton = new Button("Cancel");
    private final StoreProductService service;

    public AddProductsForm(StoreProductService service) {
        this.service = service;
        updateList();
        selector.setItemLabelGenerator(StoreProduct::getProduct);
        configureUI();
        addListener();
    }

    private void configureUI() {
        amountField.setMin(1);
        amountField.setMax(10000);
        amountField.setStepButtonsVisible(true);

        saveButton.addClickListener(e -> {
            try {
                validateAndSave();
            } catch (IllegalArgumentException ex) {
            amountField.setInvalid(true);
            amountField.setErrorMessage("Illegal amount");
            }
        });
        cancelButton.addClickListener(e -> close());

        HorizontalLayout buttons = new HorizontalLayout(saveButton, cancelButton);
        buttons.setWidth("100%");
        buttons.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        FormLayout formLayout = new FormLayout();
        formLayout.add(selector, amountField, price);

        this.add(formLayout, buttons);
        this.setWidth("400px");
    }

    private void addListener() {
        selector.addValueChangeListener(e -> {
            price.setValue(selector.getValue().getSelling_price());
        });
    }

    private void validateAndSave() {
        StoreProduct selectedProduct = selector.getValue();
        Integer amount = amountField.getValue();

        if (selectedProduct == null) {
            Notification.show("Please select a product.");
            return;
        }

        if (amount == null || amount <= 0) {
            Notification.show("Please enter a valid amount greater than 0.");
            return;
        }

        if (price.getValue() == null || price.getValue().doubleValue() <= 0) {
            Notification.show("Please enter a valid price greater than 0.");
            return;
        }
        selectedProduct.setProducts_number(selectedProduct.getProducts_number() + amount);
        selectedProduct.setSelling_price(price.getValue());
        service.updateEntity(selectedProduct);
        fireEvent(new UpdateStoreProductEvent(this));
        close();
    }

    public void addUpdateListener(ComponentEventListener<UpdateStoreProductEvent> listener) {
        addListener(UpdateStoreProductEvent.class, listener);
    }

    public static class UpdateStoreProductEvent extends UpdateEvent<AddProductsForm> {
        public UpdateStoreProductEvent(AddProductsForm storeProductToolbar) {
            super(storeProductToolbar);
        }
    }
    public void updateList(){
        selector.setItems(service.getAllWithoutSale());
    }
}
