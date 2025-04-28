package com.example.demo.views.components.CheckPageComponents;

import com.example.demo.repositories.database_entities.Check;
import com.example.demo.repositories.database_entities.CheckEntry;
import com.example.demo.repositories.database_entities.StoreProduct;
import com.example.demo.services.CheckService;
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
    private CheckService service;

    public CheckFormBody(CheckService service) {
        setWidth("100%");
        checkFormLines = new ArrayList<>();
        this.service = service;
    }

    public void setCheck(Check check) {
        if (check != null) {
            checkItems = check.getGoods();
            checkNumber = check.getCheck_number();
            checkFormLines.clear();
            this.removeAll();
            boolean isEdit = (check.getCheck_number() != null);
            for (CheckEntry c : checkItems) {
                CheckFormLine line = new CheckFormLine(c);
                this.add(line);
                addListeners(line);
                line.setAmountFieldReadOnly(isEdit);
                checkFormLines.add(line);
            }
        } else {
            checkItems.clear();
            checkNumber = null;
            checkFormLines.clear();
        }
    }

    public boolean addProduct(StoreProduct sp) {
        boolean alreadyExists = false;
        for (CheckEntry c : checkItems) {
            if (Objects.equals(c.getProductName(), sp.getProduct()) ||
                    Objects.equals(c.getProductName(), sp.getProduct()+" prom")||
                            Objects.equals(c.getProductName()+" prom", sp.getProduct())) {
                alreadyExists = true;
                break;
            }
        }
        if (alreadyExists) return false;
        CheckEntry newEntry = new CheckEntry(
                1,
                sp.getSelling_price(),
                sp.getUPC(),
                checkNumber,
                sp.getProduct()
        );
        CheckFormLine newLine = new CheckFormLine(newEntry);
        checkFormLines.add(newLine);
        checkItems.add(newEntry);
        this.add(newLine);
        addListeners(newLine);
        fireEvent(new CheckFormLine.UpdateCheckSum(newLine, sp.getSelling_price(), 1));
        return true;
    }

    public void addListeners(CheckFormLine formLine) {
        formLine.addDeleteListener(e -> {
            checkItems.remove(formLine.getCheckEntry());
            checkFormLines.remove(formLine);
            formLine.removeFromParent();
            CheckEntry cur = e.getSource().getCheckEntry();
            BigDecimal price = cur.getSelling_price().multiply(BigDecimal.valueOf(-1*cur.getAmountOfProducts()));
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
        private final BigDecimal delta;

        public UpdateTotalPriceEvent(CheckFormBody source, BigDecimal delta) {
            super(source, true);
            this.delta = delta;
        }

        public BigDecimal getDelta() {
            return delta;
        }
    }
}
