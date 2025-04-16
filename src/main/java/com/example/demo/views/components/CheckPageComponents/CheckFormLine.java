package com.example.demo.views.components.CheckPageComponents;

import com.example.demo.views.events.DeleteEvent;
import com.example.demo.views.repositories.database_entities.CheckEntry;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class CheckFormLine extends HorizontalLayout {

    public CheckEntry getCheckEntry() {
        return checkEntry;
    }

    private CheckEntry checkEntry;
    private TextField productNameField;
    private NumberField productPriceField;
    private NumberField totalProductPriceField;
    private NumberField amountField;
    private BigDecimal unitPrice;
    private Button deleteButton;

    public CheckFormLine(CheckEntry checkEntry) {
        this.checkEntry = checkEntry;

        this.productNameField = new TextField("");
        productNameField.setValue(String.valueOf(checkEntry.getProductName()));
        productNameField.setReadOnly(true);

        this.amountField = new NumberField("Amount");
        this.amountField.setValue((double) checkEntry.getAmountOfProducts());

        this.productPriceField = new NumberField("Unit price");
        unitPrice = checkEntry.getProduct_selling_price();
        this.productPriceField.setValue(unitPrice.setScale(2, RoundingMode.HALF_UP).doubleValue());
        productPriceField.setReadOnly(true);

        this.totalProductPriceField = new NumberField("Total");
        BigDecimal total = unitPrice.multiply(BigDecimal.valueOf(checkEntry.getAmountOfProducts()));
        this.totalProductPriceField.setValue(total.setScale(2, RoundingMode.HALF_UP).doubleValue());
        totalProductPriceField.setReadOnly(true);

        configureUI();
        configureListeners();
    }

    private void configureUI() {
        this.setWidthFull();
        this.setAlignItems(Alignment.BASELINE);

        productNameField.setWidthFull();
        amountField.setWidth("120px");
        productPriceField.setWidth("120px");
        totalProductPriceField.setWidth("140px");

        amountField.setStepButtonsVisible(true);
        amountField.setMin(1);

        this.setFlexGrow(2, productNameField);
        this.setFlexGrow(1, amountField, productPriceField, totalProductPriceField);

        deleteButton = new Button("Delete");
        deleteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);

        this.add(productNameField, amountField, productPriceField, totalProductPriceField, deleteButton);
    }

    private void configureListeners() {
        deleteButton.addClickListener(e -> fireEvent(new DeleteCheckEntry(this)));
        amountField.addValueChangeListener(event -> {
            Double newAmount = event.getValue();
            if (newAmount != null) {
                int oldAmount = checkEntry.getAmountOfProducts();
                BigDecimal oldTotal = unitPrice.multiply(BigDecimal.valueOf(oldAmount));
                BigDecimal newTotal = unitPrice.multiply(BigDecimal.valueOf(newAmount));
                totalProductPriceField.setValue(newTotal.setScale(2, RoundingMode.HALF_UP).doubleValue());
                checkEntry.setAmountOfProducts(newAmount.intValue());
                checkEntry.setProduct_selling_price(BigDecimal.valueOf(productPriceField.getValue()));
                checkEntry.setSelling_price(newTotal);
                BigDecimal delta = newTotal.subtract(oldTotal);
                fireEvent(new UpdateCheckSum(this, delta));
            }
        });
    }

    public void addUpdateListener(ComponentEventListener<UpdateCheckSum> listener) {
        addListener(UpdateCheckSum.class, listener);
    }

    public void addDeleteListener(ComponentEventListener<DeleteCheckEntry> listener) {
        addListener(DeleteCheckEntry.class, listener);
    }
    public static class UpdateCheckSum extends ComponentEvent<CheckFormLine> {
        private BigDecimal delta;
        public UpdateCheckSum(CheckFormLine source, BigDecimal delta) {
            super(source, true);
            this.delta = delta;
        }
        public BigDecimal getDelta() {
            return delta;
        }
    }

    public static class DeleteCheckEntry extends ComponentEvent<CheckFormLine> {

        public DeleteCheckEntry(CheckFormLine checkFormLine) {
            super(checkFormLine, true);
        }
    }
}
