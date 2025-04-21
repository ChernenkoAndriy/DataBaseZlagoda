package com.example.demo.views.components.ProductPageComponents;

import com.example.demo.views.events.UpdateEvent;
import com.example.demo.views.repositories.database_entities.Category;
import com.example.demo.views.repositories.database_entities.Product;
import com.example.demo.views.services.EmployeeService;
import com.example.demo.views.services.ProductService;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.example.demo.views.repositories.database_entities.Employee;
import java.util.List;

public class ProductToolbar extends HorizontalLayout{
    private TextField filterField = new TextField();;
    private ComboBox<Category> categories = new ComboBox<>("");
    private Button printButton = new Button("Print");
    private Button exportButton= new Button("Export");
    public Button getAddButton() {
        return addButton;
    }
    private Button addButton= new Button("Add");
    public ProductToolbar(List<Category> roles){
        addButton.addThemeName("primary");
        addButton.setWidth("20%");
        exportButton.addThemeName("primary");
        exportButton.setWidth("15%");
        printButton.setWidth("15%");
        printButton.addThemeName("primary");
        Category allCategory =  new Category();
        allCategory.setCategory_name("All");
        roles.add(allCategory);
        categories.setItems(roles);
        HorizontalLayout rightLayout = new HorizontalLayout(exportButton, printButton);
        rightLayout.setAlignItems(FlexComponent.Alignment.END);
        rightLayout.setSpacing(true);
        configureComponents();
        addComponentAsFirst(addButton);
        add(rightLayout);
    }
    protected void configureComponents() {
        filterField.setPlaceholder("Find product by name");
        filterField.setValueChangeMode(ValueChangeMode.LAZY);
        filterField.setWidth("55%");
        filterField.addValueChangeListener(e ->
                fireEvent(new UpdateProductEvent(this)));
        categories.setItemLabelGenerator(Category::getCategory_name);
        categories.setAllowCustomValue(false);
        categories.addValueChangeListener(e ->
                fireEvent(new UpdateProductEvent(this)));
        add(filterField);
        add(categories);
    }
    public List<Product> getAllByfilters(ProductService service) {
        String name = filterField.getValue();
        if (name == null || name.trim().isEmpty()) {
            name = null;
        }
        Category selectedCategory = categories.getValue();
        Integer categoryNumber = null;
        if (selectedCategory != null && !"All".equals(selectedCategory.getCategory_name())) {
            categoryNumber = selectedCategory.getCategory_number();
        }

        return service.getAllBy(name, categoryNumber);
    }

    public void addUpdateListener(ComponentEventListener<UpdateProductEvent> listener) {
        addListener(UpdateProductEvent.class, listener);
    }
    public static class UpdateProductEvent extends UpdateEvent<ProductToolbar> {
        public UpdateProductEvent(ProductToolbar employeeToolbar) {
            super(employeeToolbar);
        }
    }

}
