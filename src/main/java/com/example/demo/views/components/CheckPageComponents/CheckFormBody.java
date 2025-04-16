package com.example.demo.views.components.CheckPageComponents;

import com.example.demo.views.repositories.database_entities.Check;
import com.example.demo.views.repositories.database_entities.CheckEntry;
import com.example.demo.views.repositories.database_entities.Store_Product;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class CheckFormBody extends VerticalLayout {
    private ArrayList<CheckEntry> checkItems;
    private ArrayList<CheckFormLine> checkFormLines;
    private UUID checkNumber;
    private BigDecimal sum;
    public CheckFormBody() {
        setWidth("100%");
        checkFormLines = new ArrayList<>();
    }

    private void updatePrice(BigDecimal delta) {
        sum = sum.add(delta);
    }

    public void addProduct(Store_Product sp){
        boolean check = false;
        for(CheckEntry c : checkItems){
            if (Objects.equals(c.getProductName(), sp.getProduct())) {
                check = true;
                break;
            }
        }
        if(check)
            return;
        CheckEntry newEntry = new CheckEntry(1,
                                            sp.getSelling_price(),
                                            sp.getUPC(),
                                            checkNumber,
                                            sp.getSelling_price(),
                                            sp.getProduct());
        CheckFormLine newLine = new CheckFormLine(newEntry);
        this.add(newLine);
        checkFormLines.add(newLine);
        checkItems.add(newEntry);
        sum = sum.add(newEntry.getSelling_price());
    }

    public void delete(CheckFormLine line){
        checkFormLines.remove(line);
        checkItems.remove(line.getCheckEntry());
        this.remove(line);
        sum = sum.subtract(line.getCheckEntry().getSelling_price());
    }

    public void setCheck(Check check) {
        this.checkItems = (ArrayList<CheckEntry>) check.getGoods();
        this.checkNumber = check.getCheck_number();
        this.sum = check.getSum_total();
        checkFormLines = new ArrayList<>();
        for(CheckEntry c: checkItems){
            CheckFormLine line = new CheckFormLine(c);
            checkFormLines.add(line);
            this.add(line);
            line.addDeleteListener(e ->delete(e.getSource()));
            line.addUpdateListener(e ->updatePrice(e.getDelta()));
        }
    }
}
