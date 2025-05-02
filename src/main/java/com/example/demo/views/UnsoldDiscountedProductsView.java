package com.example.demo.views;

import com.example.demo.views.services.UnsoldProductInfo;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.example.demo.views.MainLayout;

@Route(value = "unsold-products", layout = MainLayout.class)
@PageTitle("Unsold Products")
@RolesAllowed("ROLE_MANAGER")


public class UnsoldDiscountedProductsView extends VerticalLayout {

    private final ComboBox<String> categoryComboBox = new ComboBox<>("Product Category");
    private final IntegerField discountField = new IntegerField("Min Discount %");
    private final Button searchButton = new Button("Find Products");
    private final Grid<UnsoldProductInfo> productGrid = new Grid<>(UnsoldProductInfo.class);

    public UnsoldDiscountedProductsView() {
        setPadding(true);
        setSpacing(true);

        categoryComboBox.setPlaceholder("Choose category");
        discountField.setMin(1);
        discountField.setMax(100);
        discountField.setValue(5); // default

        productGrid.setColumns("productName", "sellingPrice", "productsNumber", "categoryName", "upc");
        productGrid.getColumnByKey("productName").setHeader("Product Name");
        productGrid.getColumnByKey("sellingPrice").setHeader("Price");
        productGrid.getColumnByKey("productsNumber").setHeader("In Stock");
        productGrid.getColumnByKey("categoryName").setHeader("Category");
        productGrid.getColumnByKey("upc").setHeader("UPC");

        add(categoryComboBox, discountField, searchButton, productGrid);

        fetchCategories();

        searchButton.addClickListener(e -> {
            List<UnsoldProductInfo> products = fetchUnsoldProducts(categoryComboBox.getValue(), discountField.getValue());
            productGrid.setItems(products);
        });
    }

    private void fetchCategories() {
        List<String> categories = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Zlagoda", "postgres", "your_password");
             PreparedStatement stmt = conn.prepareStatement("SELECT DISTINCT category_name FROM \"Category\"")) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                categories.add(rs.getString("category_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        categoryComboBox.setItems(categories);
    }

    private List<UnsoldProductInfo> fetchUnsoldProducts(String category, int discount) {
        List<UnsoldProductInfo> result = new ArrayList<>();

        String sql = """
                SELECT DISTINCT p.product_name,
                                sp.selling_price,
                                sp.products_number,
                                c.category_name,
                                sp."UPC"
                FROM "Product" p
                JOIN "Category" c ON p.category_number = c.category_number
                JOIN "Store_Product" sp ON p.id_product = sp.id_product
                WHERE c.category_name = ?
                  AND p.id_product NOT IN (
                      SELECT sp.id_product
                      FROM "Store_Product" sp
                      JOIN "Sale" s ON sp."UPC" = s."UPC"
                      JOIN "Check" ch ON s.check_number = ch.check_number
                      JOIN "Customer_Card" cc ON ch.card_number = cc.card_number
                      WHERE cc.percent >= ?
                  )
                """;

        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Zlagoda", "postgres", "your_password");
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, category);
            stmt.setInt(2, discount);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                result.add(new UnsoldProductInfo(
                        rs.getString("product_name"),
                        rs.getDouble("selling_price"),
                        rs.getInt("products_number"),
                        rs.getString("category_name"),
                        rs.getString("UPC")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
}
