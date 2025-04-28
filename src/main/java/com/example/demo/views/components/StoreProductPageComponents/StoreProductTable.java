package com.example.demo.views.components.StoreProductPageComponents;
import com.example.demo.repositories.database_entities.StoreProduct;
import com.example.demo.services.StoreProductService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.grid.contextmenu.GridMenuItem;
import com.vaadin.flow.component.grid.contextmenu.GridSubMenu;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import java.util.HashMap;
import java.util.Map;
public class StoreProductTable extends Grid<StoreProduct> {
    private Map<String, Grid.Column<StoreProduct>> columnsMap = new HashMap<>();
    public StoreProductTable(StoreProductService service) {
        setItems(service.getAllEntities());
        configureColumns();
        configureColumnVisibilityContextMenu();
    }
    protected void configureColumns() {
        columnsMap.put("UPC", addColumn(StoreProduct::getUPC).setHeader("UPC").setSortable(true));
        columnsMap.put("Product name", addColumn(StoreProduct::getProduct).setHeader("Product name").setSortable(true));
        columnsMap.put("Selling price", addColumn(StoreProduct::getSelling_price).setHeader("Selling price").setSortable(true));
        columnsMap.put("Goods amount", addColumn(StoreProduct::getProducts_number).setHeader("Goods amount").setSortable(true));
        columnsMap.put("Prom",
                addColumn(new ComponentRenderer<>(storeProduct -> {
                    return storeProduct.isPromotional_product()
                            ? new Span("✓")
                            : new Span("✗");
                }))
                        .setHeader("Prom")
                        .setSortable(true)
        );
        setColumnReorderingAllowed(true);
        getColumns().forEach(col -> col.setAutoWidth(true));
        setMultiSort(true);
    }
    private void configureColumnVisibilityContextMenu() {
        GridContextMenu<StoreProduct> contextMenu = new GridContextMenu<>(this);
        GridMenuItem<StoreProduct> toggleColumn = contextMenu.addItem("Set column visibility");
        GridSubMenu<StoreProduct> exportSubMenu = toggleColumn.getSubMenu();
        for(String c : this.getColumnNames()){
            GridMenuItem<StoreProduct> item = exportSubMenu.addItem(c, e -> toggleColumnVisibility(c));
            item.setCheckable(true);
            item.setChecked(true);
            ;
        }
    }
    public String[] getColumnNames() {
        return columnsMap.keySet().toArray(new String[0]);
    }
    private void toggleColumnVisibility(String columnName) {
        Grid.Column<StoreProduct> column = columnsMap.get(columnName);
        if (column != null) {
            column.setVisible(!column.isVisible());
        }
    }
}
