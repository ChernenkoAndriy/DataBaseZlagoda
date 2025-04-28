package com.example.demo.views.components.CategoryPageComponents;

import com.example.demo.views.components.EmployeePageComponents.EmployeeToolbar;
import com.example.demo.views.events.UpdateEvent;
import com.example.demo.views.repositories.database_entities.Category;
import com.example.demo.views.services.CategoryService;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import org.checkerframework.checker.units.qual.C;

import java.util.List;

public class CategoryToolbar extends HorizontalLayout {
    private final Button printButton = new Button("Print");
    private final Button exportButton = new Button("Export");
    private final Button addButton = new Button("Add");
    private Button salesStatisticsButton = new Button("Categories without Sale products");
    private TextField filterField = new TextField();

    public Button getSalesStatisticsButton() {
        return salesStatisticsButton;
    }

    public StatisticsForm getStatisticsForm() {
        return statisticsForm;
    }

    private StatisticsForm statisticsForm = new StatisticsForm();

    public Button getAddButton() {
        return addButton;
    }
    public CategoryToolbar(){
        addButton.addThemeName("primary");
        addButton.setWidth("20%");
        exportButton.addThemeName("primary");
        exportButton.setWidth("15%");
        printButton.setWidth("15%");
        printButton.addThemeName("primary");
        HorizontalLayout rightLayout = new HorizontalLayout(exportButton, printButton);
        rightLayout.setAlignItems(FlexComponent.Alignment.END);
        rightLayout.setSpacing(true);
        configureComponents();
        addComponentAsFirst(addButton);
        add(rightLayout);
        add(salesStatisticsButton);
    }
    protected void configureComponents() {
        filterField.setPlaceholder("Find category by name");
        filterField.setValueChangeMode(ValueChangeMode.LAZY);
        filterField.setWidth("55%");
        filterField.addValueChangeListener(e ->
                fireEvent(new UpdateCategoryEvent(this)));
        add(filterField);
    }
    public List<Category> getAllByfilters(CategoryService service) {
        return service.getAllBy(filterField.getValue());
    }
    public void addUpdateListener(ComponentEventListener<UpdateCategoryEvent> listener) {
        addListener(UpdateCategoryEvent.class, listener);
    }
    public static class UpdateCategoryEvent extends UpdateEvent<CategoryToolbar> {
        public UpdateCategoryEvent(CategoryToolbar categoryToolbar) {
            super(categoryToolbar);
        }
    }
    public void setItems(List<Category> categories){
        statisticsForm.setItems(categories);

    }
    public void openForm(){
        statisticsForm.open();
    }
    private class StatisticsForm extends Dialog {
        private List<Category> categories;
        private Grid<Category> table;
        private HorizontalLayout layout;
        StatisticsForm(){
            layout = new HorizontalLayout();
            table = new Grid<>();
            add(layout);
            layout.add(table);
            setWidth("35%");
            table.addColumn(Category::getCategory_name).setHeader("Category name").setSortable(true);
            table.setColumnReorderingAllowed(true);
            table.setMultiSort(true);
        }

        public void setItems(List<Category> categories){
            this.categories=categories;
            table.setItems(this.categories);
        }
    }
}
