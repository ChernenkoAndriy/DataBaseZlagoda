package com.example.demo.views.components.StoreProductPageComponents;

import com.example.demo.views.events.UpdateEvent;
import com.example.demo.views.repositories.database_entities.StoreProduct;
import com.example.demo.views.services.StoreProductService;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;

import java.util.UUID;


public class MakeSaleProductForm extends Dialog {
    private final IntegerField amountField = new IntegerField("Enter how many to move to sale");
    private final ComboBox<StoreProduct> selector = new ComboBox<>("Select where to add");
    private final com.vaadin.flow.component.button.Button saveButton = new com.vaadin.flow.component.button.Button("Save");
    private final com.vaadin.flow.component.button.Button cancelButton = new Button("Cancel");
    private final StoreProductService service;

    public MakeSaleProductForm(StoreProductService service) {
        this.service = service;
        selector.setItems(service.getAllWithSale());
        selector.setItemLabelGenerator(StoreProduct::getProduct);
        configureUI();
    }

    private void configureUI() {
        amountField.setMin(1);
        amountField.setStepButtonsVisible(true);

        saveButton.addClickListener(e -> {try {
            validateAndSave();
        } catch (IllegalArgumentException ex){
            Notification.show(ex.getMessage());
            amountField.clear();
        }});
        cancelButton.addClickListener(e -> close());

        HorizontalLayout buttons = new HorizontalLayout(saveButton, cancelButton);
        buttons.setWidth("100%");
        buttons.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        FormLayout formLayout = new FormLayout();
        formLayout.add(selector, amountField);

        this.add(formLayout, buttons);
        this.setWidth("400px");
        selector.addValueChangeListener(e -> {
            if (e != null)
                amountField.setValue(1);
            amountField.setMax(selector.getValue().getProducts_number());
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
        UUID id = selectedProduct.getId();
        UUID promid = selectedProduct.getUPC_prom();
        service.moveProducts(id, promid, amount);
        fireEvent(new UpdateStoreProductEvent(this));
        close();
    }

    public void addUpdateListener(ComponentEventListener<UpdateStoreProductEvent> listener) {
        addListener(UpdateStoreProductEvent.class, listener);
    }

    public static class UpdateStoreProductEvent extends UpdateEvent<MakeSaleProductForm> {
        public UpdateStoreProductEvent(MakeSaleProductForm storeProductToolbar) {
            super(storeProductToolbar);
        }
    }

    public void updateList(){
        selector.setItems(service.getAllWithSale());
    }
}
