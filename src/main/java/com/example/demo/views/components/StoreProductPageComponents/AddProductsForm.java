package com.example.demo.views.components.StoreProductPageComponents;

import com.example.demo.views.events.UpdateEvent;
import com.example.demo.views.repositories.database_entities.StoreProduct;
import com.example.demo.views.services.StoreProductService;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;

public class AddProductsForm extends Dialog {
    private final IntegerField amountField = new IntegerField("Enter how many to add");
    private final ComboBox<StoreProduct> selector = new ComboBox<>("Select where to add");
    private final Button saveButton = new Button("Save");
    private final Button cancelButton = new Button("Cancel");
    private final StoreProductService service;

    public AddProductsForm(StoreProductService service) {
        this.service = service;
        selector.setItems(service.getAllEntities());
        selector.setItemLabelGenerator(StoreProduct::getProduct);
        configureUI();
    }

    private void configureUI() {
        amountField.setMin(1);
        amountField.setMax(10000);
        amountField.setStepButtonsVisible(true);

        saveButton.addClickListener(e -> validateAndSave());
        cancelButton.addClickListener(e -> close());

        HorizontalLayout buttons = new HorizontalLayout(saveButton, cancelButton);
        buttons.setWidth("100%");
        buttons.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        FormLayout formLayout = new FormLayout();
        formLayout.add(selector, amountField);

        this.add(formLayout, buttons);
        this.setWidth("400px");
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

        selectedProduct.setProducts_number(selectedProduct.getProducts_number() + amount);
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
}
