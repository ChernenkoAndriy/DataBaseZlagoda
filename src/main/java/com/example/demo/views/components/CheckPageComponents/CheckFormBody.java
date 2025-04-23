package com.example.demo.views.components.CheckPageComponents;

import com.example.demo.views.repositories.database_entities.Check;
import com.example.demo.views.repositories.database_entities.CheckEntry;
import com.example.demo.views.repositories.database_entities.StoreProduct;
import com.example.demo.views.services.CheckService;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class CheckFormBody extends VerticalLayout {
    public List<CheckEntry> getCheckItems() {
        return checkItems;
    }

    private List<CheckEntry> checkItems;
    private ArrayList<CheckFormLine> checkFormLines;
    private UUID checkNumber;
    private Check check;
    private CheckService service;
    public CheckFormBody(CheckService service) {
        setWidth("100%");
        checkFormLines = new ArrayList<>();
        this.service = service;
    }
    public void setCheck(Check check) {
        if(check != null) {
            this.check = check;
            this.checkItems = check.getGoods();
            this.checkNumber = check.getCheck_number();
            this.removeAll();
            checkFormLines = new ArrayList<>();

            for (CheckEntry c : checkItems) {
                CheckFormLine line = new CheckFormLine(c);
                checkFormLines.add(line);
                this.add(line);
                addListeners(line);
            }
        }else{
            this.check = check;
        }
    }
    public void addProduct(StoreProduct sp) {
        boolean alreadyExists = false;
        for (CheckEntry c : checkItems) {
            if (Objects.equals(c.getProductName(), sp.getProduct())) {
                alreadyExists = true;
                break;
            }
        }
        if (alreadyExists) return;

        CheckEntry newEntry = new CheckEntry(
                0,
                BigDecimal.ZERO,
                sp.getUPC(),
                checkNumber,
                sp.getSelling_price(),
                sp.getProduct()
        );
        newEntry.addProductsAmount(1);
        CheckFormLine newLine = new CheckFormLine(newEntry);
        checkFormLines.add(newLine);
        checkItems.add(newEntry);
        this.add(newLine);
        addListeners(newLine);
        fireEvent(new CheckFormLine.UpdateCheckSum(newLine, sp.getSelling_price(), 1));
    }
    public void addListeners(CheckFormLine formLine){
        formLine.addDeleteListener(e -> {
            checkItems.remove(formLine.getCheckEntry());
            checkFormLines.remove(formLine);
            formLine.removeFromParent();
            BigDecimal price = e.getSource().getCheckEntry().getSelling_price();
            price = price.multiply(BigDecimal.valueOf(-1));
            fireEvent(new UpdateTotalPriceEvent(this, price));
        });
        formLine.addUpdateListener(e -> {
           fireEvent(new UpdateTotalPriceEvent(this, e.getDelta()));
        });

    }
    public void addUpdateListener(ComponentEventListener<UpdateTotalPriceEvent> listener) {
        addListener(UpdateTotalPriceEvent.class, listener);
    }
    public static class UpdateTotalPriceEvent extends ComponentEvent<CheckFormBody> {
        public BigDecimal getDelta() {
            return delta;
        }

        private BigDecimal delta;

        public UpdateTotalPriceEvent(CheckFormBody body, BigDecimal delta) {
            super(body, true);
            this.delta = delta;
        }

    }

}
