package com.example.demo.views.components.CategoryPageComponents;

import com.example.demo.views.components.EmployeePageComponents.EmployeeToolbar;
import com.example.demo.views.events.UpdateEvent;
import com.example.demo.views.repositories.database_entities.Category;
import com.example.demo.views.services.CategoryService;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.util.List;

public class CategoryToolbar extends HorizontalLayout {
    private final Button printButton = new Button("Print");
    private final Button exportButton = new Button("Export");
    private final Button addButton = new Button("Add");
    private TextField filterField = new TextField();

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
}
