package com.example.demo.views;
import com.example.demo.views.components.ProductPageComponents.ProductForm;
import com.example.demo.views.components.ProductPageComponents.ProductTable;
import com.example.demo.views.components.ProductPageComponents.ProductToolbar;
import com.example.demo.repositories.database_entities.Product;
import com.example.demo.services.CategoryService;
import com.example.demo.services.ProductService;
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
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.annotation.Scope;
@Route(value = "products", layout = MainLayout.class)
@SpringComponent
@RolesAllowed("ROLE_MANAGER")
@Scope("prototype")
@PageTitle("Products | ZLAGODA")
public class ProductView extends AppLayout {
    protected ProductTable table;
    protected ProductToolbar bar;
    protected ProductService service;
    protected ProductForm productForm;
    protected CategoryService categoryService;
    public ProductView(ProductService productService, CategoryService categoryService) {
        this.categoryService = categoryService;
        this.service = productService;
        this.productForm = new ProductForm(categoryService.getAllEntities());
        this.table = new ProductTable(service);
        this.bar = new ProductToolbar(categoryService.getAllEntities());
        configureContent();
    }
    private void configureContent() {
        bar.setWidth("100%");
        table.setMinWidth("130%");
        table.asSingleSelect().addValueChangeListener(event ->
                editProduct(event.getValue()));
        VerticalLayout tableContainer = new VerticalLayout(table);
        tableContainer.setSizeFull();
        tableContainer.setPadding(false);
        tableContainer.getStyle().set("overflow", "auto");
        VerticalLayout content = new VerticalLayout(bar, tableContainer);
        content.setSizeFull();
        setContent(content);
        productForm.addSaveListener(this::saveProduct);
        productForm.addDeleteListener(this::deleteProduct);
        productForm.addCloseListener(e -> closeEditor());
        bar.getAddButton().addClickListener(e -> addProduct());
        bar.addUpdateListener(e -> updateList());
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
    private void saveProduct(ProductForm.SaveProductEvent event) {
        try {
            Product e = event.getEntity();
            if (e.getId() == null) {
                service.addEntity(e);
            } else {
                service.updateEntity(e);
            }
            updateList();
            closeEditor();
        }catch (ConstraintViolationException e){
            showErrorNotification(e.getMessage());
            productForm.setInavalidName();
        }
    }
    private void deleteProduct(ProductForm.DeleteProductEvent event) {
        try {
            service.deleteEntity(event.getEntity().getId());
            updateList();
            closeEditor();
        } catch (ConstraintViolationException er) {
            showErrorNotification(er.getMessage());
        }
    }
    private void editProduct(Product e) {
        if (e == null) {
            closeEditor();
        } else {
            productForm.setProduct(e);
            productForm.open();
            addClassName("editing");
        }
    }
    private void addProduct(){
        table.asSingleSelect().clear();
        editProduct(new Product());
    }
}
