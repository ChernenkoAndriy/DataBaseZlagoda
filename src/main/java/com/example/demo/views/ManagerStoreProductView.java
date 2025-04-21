package com.example.demo.views;
import com.example.demo.views.components.StoreProductPageComponents.*;
import com.example.demo.views.repositories.database_entities.StoreProduct;
import com.example.demo.views.services.CategoryService;
import com.example.demo.views.services.ProductService;
import com.example.demo.views.services.StoreProductService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.annotation.Scope;
@Route(value = "warehouse", layout = ManagerLayout.class)
@SpringComponent
@Scope("prototype")
@PageTitle("Warehouse | ZLAGODA")
public class ManagerStoreProductView extends AppLayout {
    protected StoreProductTable table;
    protected StoreProductToolbar bar;
    protected StoreProductService service;
    protected ProductService productService;
    protected CategoryService categoryService;
    protected StoreProductForm productForm;
    private AddProductsForm addProductsForm;
    private MakeSaleProductForm makeSaleProductForm;
    public ManagerStoreProductView(StoreProductService storeProductService, ProductService productService, CategoryService categoryService) {
        this.service = storeProductService;
        this.productService = productService;
        this.makeSaleProductForm = new MakeSaleProductForm(service);
        this.addProductsForm = new AddProductsForm(service);
        this.categoryService = categoryService;
        this.productForm = new StoreProductForm(productService.getAllEntities(), service);
        this.table = new StoreProductTable(service);
        this.bar = new StoreProductToolbar(categoryService.getAllEntities());
        configureContent();
    }
    private void configureContent() {
        bar.setWidth("100%");
        table.setMinWidth("70%");
        table.asSingleSelect().addValueChangeListener(event ->
                editStoreProduct(event.getValue()));
        VerticalLayout tableContainer = new VerticalLayout(table);
        tableContainer.setSizeFull();
        tableContainer.setPadding(false);
        tableContainer.getStyle().set("overflow", "auto");
        VerticalLayout content = new VerticalLayout(bar, tableContainer);
        content.setSizeFull();
        setContent(content);
        productForm.addSaveListener(this::saveProduct);
        productForm.addDeleteListener(this::deleteStoreProduct);
        productForm.addCloseListener(e -> closeEditor());
        bar.getAddButton().addClickListener(e -> addStoreProduct());
        bar.addUpdateListener(e -> updateList());
        bar.getAddGoodsButton().addClickListener(e -> addProductsForm.open());
        bar.getMakeSaleGoodsButton().addClickListener(e ->makeSaleProductForm.open());
        addProductsForm.addUpdateListener(e -> updateList());
        makeSaleProductForm.addUpdateListener(e ->updateList());
    }
    private void showErrorNotification(String message) {
        Notification notification = new Notification();
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        Div text = new Div(new Text(message));
        Button closeButton = new Button(new Icon("lumo", "cross"));
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        closeButton.setAriaLabel("Close");
        closeButton.addClickListener(event -> notification.close());
        HorizontalLayout layout = new HorizontalLayout(text, closeButton);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        notification.setDuration(7000);
        notification.add(layout);
        notification.open();
    }
    private void closeEditor() {
        productForm.setProduct(null);
        productForm.close();
    }
    private void updateList() {
        table.setItems(bar.getAllByfilters(service));
    }
    private void saveProduct(StoreProductForm.SaveStoreProductEvent event) {
        try {
            StoreProduct e = event.getEntity();
            if (e.getId() == null) {
                service.addEntity(e);
            } else {
                service.updateEntity(e);
            }
            updateList();
            closeEditor();
        }catch (ConstraintViolationException e){
            showErrorNotification(e.getMessage());
        }
    }
    private void deleteStoreProduct(StoreProductForm.DeleteStoreProductEvent event) {
        try {
            service.deleteEntity(event.getEntity().getId());
            updateList();
            closeEditor();
        } catch (ConstraintViolationException er) {
            showErrorNotification(er.getMessage());
        }
    }
    private void editStoreProduct(StoreProduct e) {
        if (e == null) {
            closeEditor();
        } else {
            productForm.setProduct(e);
            productForm.open();
            addClassName("editing");
        }
    }
    private void addStoreProduct(){
        table.asSingleSelect().clear();
        editStoreProduct(new StoreProduct());
    }
}
