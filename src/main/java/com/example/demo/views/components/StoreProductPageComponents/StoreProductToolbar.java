package com.example.demo.views.components.StoreProductPageComponents;

import com.example.demo.views.events.UpdateEvent;
import com.example.demo.repositories.database_entities.Category;
import com.example.demo.repositories.database_entities.StoreProduct;
import com.example.demo.services.StoreProductService;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.util.List;
import java.util.Objects;

public class StoreProductToolbar extends HorizontalLayout{
    private TextField filterField = new TextField();
    private ComboBox<Category> categories = new ComboBox<>("");
    private ComboBox<String> promFilter;
    private Button printButton = new Button("Print");
    private Button exportButton= new Button("Export");
    public Button getAddButton() {
        return addButton;
    }
    private Button addButton= new Button("Add");
    public Button getAddGoodsButton() {
        return addGoodsButton;
    }
    private Button addGoodsButton = new Button("Add Goods to");

    public Button getMakeSaleGoodsButton() {
        return makeSaleGoodsButton;
    }

    private Button makeSaleGoodsButton = new Button("Make Sale for");
    public StoreProductToolbar(List<Category> categories){
        promFilter=new ComboBox<>("", "All", "Prom", "Not prom");
        promFilter.setValue("All");
        addButton.addThemeName("primary");
        addButton.setWidth("10%");
        exportButton.addThemeName("primary");
        exportButton.setWidth("15%");
        printButton.setWidth("15%");
        printButton.addThemeName("primary");
        Category allCategory =  new Category();
        allCategory.setCategory_name("All");
        categories.add(allCategory);
        this.categories.setItems(categories);
        HorizontalLayout rightLayout = new HorizontalLayout(exportButton, printButton);
        rightLayout.setAlignItems(FlexComponent.Alignment.END);
        rightLayout.setSpacing(true);
        configureComponents();
        addGoodsButton.addThemeName("primary");
        makeSaleGoodsButton.addThemeName("primary");
        addComponentAsFirst(makeSaleGoodsButton);
        addComponentAsFirst(addGoodsButton);
        addComponentAsFirst(addButton);
        add(rightLayout);
    }
    protected void configureComponents() {
        filterField.setPlaceholder("Find goods");
        filterField.setValueChangeMode(ValueChangeMode.LAZY);
        filterField.setWidth("70%");
        filterField.addValueChangeListener(e ->
                fireEvent(new UpdateStoreProductEvent(this)));
        categories.setItemLabelGenerator(Category::getCategory_name);
        categories.setAllowCustomValue(false);
        categories.addValueChangeListener(e ->
                fireEvent(new UpdateStoreProductEvent(this)));
        promFilter.addValueChangeListener(e ->
                fireEvent(new UpdateStoreProductEvent(this)));
        add(filterField);
        add(categories, promFilter);
    }
    public List<StoreProduct> getAllByfilters(StoreProductService service) {
        Boolean isPromotional = null;
        String upc = null;
        String productName = null;

        String promFilterValue = promFilter.getValue();
        switch (promFilterValue) {
            case "Not prom":
                isPromotional = false;
                break;
            case "Prom":
                isPromotional = true;
                break;
        }

        String filtertext = filterField.getValue();
        if (filtertext != null && !filtertext.isBlank()) {
            if (filtertext.matches(".*\\d.*")) {
                upc=filtertext;
            }else {
                productName = filtertext;
            }


        }
        Category category = categories.getValue();
        String categoryName = (category != null) ? category.getCategory_name() : null;
        if (Objects.equals(categoryName, "All")) {
            categoryName = null;
        }
        return service.getAllBy(productName, categoryName, isPromotional, upc);

    }

    public void addUpdateListener(ComponentEventListener<UpdateStoreProductEvent> listener) {
        addListener(UpdateStoreProductEvent.class, listener);
    }
    public static class UpdateStoreProductEvent extends UpdateEvent<StoreProductToolbar> {
        public UpdateStoreProductEvent(StoreProductToolbar storeProductToolbar) {
            super(storeProductToolbar);
        }
    }

}
