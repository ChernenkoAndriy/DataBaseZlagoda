package com.example.demo.views.components.CheckPageComponents;

import com.example.demo.repositories.database_entities.CheckEntry;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CheckFormLine extends HorizontalLayout {
    private CheckEntry checkEntry;
    private TextField productNameField = new TextField("");
    private BigDecimalField productPriceField= new BigDecimalField("Unit price");
    private BigDecimalField totalProductPriceField = new BigDecimalField("Total");
    private IntegerField amountField = new IntegerField("Amount");
    private BigDecimal unitPrice;
    private Button deleteButton;

    public CheckFormLine(CheckEntry checkEntry) {
        this.checkEntry = checkEntry;
        productNameField.setValue(String.valueOf(checkEntry.getProductName()));
        productNameField.setReadOnly(true);
        this.amountField.setValue(checkEntry.getAmountOfProducts());
        unitPrice = checkEntry.getSelling_price();
        this.productPriceField.setValue(unitPrice.setScale(2, RoundingMode.HALF_UP));
        productPriceField.setReadOnly(true);
        BigDecimal total = unitPrice.multiply(BigDecimal.valueOf(checkEntry.getAmountOfProducts()));
        this.totalProductPriceField.setValue(total.setScale(2, RoundingMode.HALF_UP));
        totalProductPriceField.setReadOnly(true);
        configureUI();
        configureListeners();
    }
    private void configureListeners() {
        deleteButton.addClickListener(e -> fireEvent(new DeleteCheckEntry(this)));
        amountField.setValueChangeMode(ValueChangeMode.EAGER);
        amountField.addValueChangeListener(event -> {
            Integer newAmount = event.getValue();
            if (newAmount != null) {
                int currentAmount = checkEntry.getAmountOfProducts();
                int newAmountInt = newAmount;
                if (newAmountInt != currentAmount) {
                    int deltaAmount = newAmountInt - currentAmount;
                    checkEntry.setAmountOfProducts(newAmount);
                    fireEvent(new UpdateCheckSum(this,
                            checkEntry.getSelling_price().multiply(BigDecimal.valueOf(deltaAmount)),
                            deltaAmount
                    ));
                    totalProductPriceField.setValue(checkEntry.getTotal());
                }
                if(newAmount<=0){
                    amountField.setValue(1);
                }
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
        private final BigDecimal delta;
        private final int deltaAmount;

        public UpdateCheckSum(CheckFormLine source, BigDecimal delta, int deltaAmount) {
            super(source, true);
            this.delta = delta;
            this.deltaAmount = deltaAmount;
        }

        public BigDecimal getDelta() {
            return delta;
        }

        public int getDeltaAmount() {
            return deltaAmount;
        }
    }

    public static class DeleteCheckEntry extends ComponentEvent<CheckFormLine> {
        public DeleteCheckEntry(CheckFormLine checkFormLine) {
            super(checkFormLine, true);
        }
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

    public CheckEntry getCheckEntry() {
        return checkEntry;
    }
    public void setAmountFieldReadOnly(boolean b){
        amountField.setReadOnly(b);
    }

}
