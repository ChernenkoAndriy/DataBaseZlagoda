package com.example.demo.views.components.ProductPageComponents;

import com.example.demo.views.repositories.database_entities.Product;
import com.example.demo.views.services.EmployeeService;
import com.example.demo.views.services.ProductService;
import com.vaadin.flow.component.grid.Grid;
import com.example.demo.views.repositories.database_entities.Employee;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.grid.contextmenu.GridMenuItem;
import com.vaadin.flow.component.grid.contextmenu.GridSubMenu;

import java.util.HashMap;
import java.util.Map;
public class ProductTable extends Grid<Product> {
    private Map<String, Grid.Column<Product>> columnsMap = new HashMap<>();
    public ProductTable(ProductService service) {
        setItems(service.getAllEntities());
        configureColumns();
        configureColumnVisibilityContextMenu();
    }
    protected void configureColumns() {
        columnsMap.put("Product name", addColumn(Product::getProduct_name).setHeader("Product name").setSortable(true));
        columnsMap.put("Product category", addColumn(Product::getCategoryName).setHeader("Product category").setSortable(true));
                setColumnReorderingAllowed(true);
        getColumns().forEach(col -> col.setAutoWidth(true));
        setMultiSort(true);
    }
    private void configureColumnVisibilityContextMenu() {
        GridContextMenu<Product> contextMenu = new GridContextMenu<>(this);
        GridMenuItem<Product> toggleColumn = contextMenu.addItem("Set column visibility");
        GridSubMenu<Product> exportSubMenu = toggleColumn.getSubMenu();
        for(String c : this.getColumnNames()){
            GridMenuItem<Product> item = exportSubMenu.addItem(c, e -> toggleColumnVisibility(c));
            item.setCheckable(true);
            item.setChecked(true);
            ;
        }
    }
    public String[] getColumnNames() {
        return columnsMap.keySet().toArray(new String[0]);
    }
    private void toggleColumnVisibility(String columnName) {
        Grid.Column<Product> column = columnsMap.get(columnName);
        if (column != null) {
            column.setVisible(!column.isVisible());
        }
    }
}
