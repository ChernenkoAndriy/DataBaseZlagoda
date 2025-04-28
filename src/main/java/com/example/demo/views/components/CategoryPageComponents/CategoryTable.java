package com.example.demo.views.components.CategoryPageComponents;

import com.example.demo.repositories.database_entities.Category;
import com.example.demo.services.CategoryService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.grid.contextmenu.GridMenuItem;
import com.vaadin.flow.component.grid.contextmenu.GridSubMenu;

import java.util.HashMap;
import java.util.Map;

public class CategoryTable extends Grid<Category> {
    private Map<String, Column<Category>> columnsMap = new HashMap<>();

    public CategoryTable(CategoryService service) {
        setItems(service.getAllEntities());
        configureColumns();
        configureColumnVisibilityContextMenu();
    }

    protected void configureColumns() {
        columnsMap.put("Category name", addColumn(Category::getCategory_name)
                .setHeader("Category name").setSortable(true));
        columnsMap.put("Amount of Products", addColumn(Category::getAmountOfGoods)
                .setHeader("Amount of Products").setSortable(true));
        setColumnReorderingAllowed(true);
        getColumns().forEach(col -> col.setAutoWidth(true));
        setMultiSort(true);
    }

    private void configureColumnVisibilityContextMenu() {
        GridContextMenu<Category> contextMenu = new GridContextMenu<>(this);
        GridMenuItem<Category> toggleColumn = contextMenu.addItem("Set column visibility");
        GridSubMenu<Category> exportSubMenu = toggleColumn.getSubMenu();

        for (String c : this.getColumnNames()) {
            GridMenuItem<Category> item = exportSubMenu.addItem(c, e -> toggleColumnVisibility(c));
            item.setCheckable(true);
            item.setChecked(true);
        }
    }

    public String[] getColumnNames() {
        return columnsMap.keySet().toArray(new String[0]);
    }

    private void toggleColumnVisibility(String columnName) {
        Column<Category> column = columnsMap.get(columnName);
        if (column != null) {
            column.setVisible(!column.isVisible());
        }
    }
}
