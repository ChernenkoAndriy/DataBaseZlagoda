package com.example.demo.views.components.CheckPageComponents;

import com.example.demo.views.repositories.database_entities.Check;
import com.example.demo.views.services.CheckService;
import com.vaadin.flow.component.grid.Grid;

public class CheckTable extends Grid<Check> {
    public CheckTable(CheckService service) {
        super(Check.class);
        setItems(service.getAllEntities());

        addColumn(Check::getCheck_number).setHeader("Check Number");
        addColumn(Check::getPrint_date).setHeader("Date");
        addColumn(Check::getSum_total).setHeader("Total");
        addColumn(Check::getVat).setHeader("VAT");
        setSelectionMode(SelectionMode.SINGLE);
    }
}
