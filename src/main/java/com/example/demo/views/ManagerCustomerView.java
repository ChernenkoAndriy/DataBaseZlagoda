//package com.example.demo.views;
//
//import com.example.demo.views.services.CustomerService;
//import com.example.demo.views.components.*;
//import com.example.demo.views.components.CustomerPageComponents.CustomerTable;
//import com.example.demo.views.components.CustomerPageComponents.CustomerToolbar;
//import com.vaadin.flow.component.applayout.AppLayout;
//import com.vaadin.flow.component.applayout.DrawerToggle;
//import com.vaadin.flow.component.grid.Grid;
//import com.vaadin.flow.component.html.H1;
//import com.vaadin.flow.component.orderedlayout.Scroller;
//import com.vaadin.flow.component.orderedlayout.VerticalLayout;
//import com.vaadin.flow.data.provider.ListDataProvider;
//import com.vaadin.flow.router.Route;
//import com.vaadin.flow.theme.lumo.LumoUtility;
//import database_manegment.database_entities.CustomerCard;
//
//import java.util.ArrayList;
//
//@Route("customers")
//public class ManagerCustomerView extends AppLayout {
//    private ManagerSideMenu sidemenu;
//    private final ListDataProvider<CustomerCard> dataProvider;
//    private final Grid<CustomerCard> table;
//    private final ArrayList<CustomerCard> data;
//    private final CustomerToolbar bar;
//    private CustomerService cs;
//
//    public ManagerCustomerView() {
//        this.data = new ArrayList<>();
//        this.dataProvider = new ListDataProvider<>(cs.getAllCustomers());
//        this.sidemenu = new ManagerSideMenu();
//        this.table = new CustomerTable(dataProvider, cs);
//        this.bar = new CustomerToolbar(dataProvider, cs, data);
//        configureNavbar();
//        configureDrawer();
//        configureContent();
//    }
//
//    private void configureNavbar() {
//        DrawerToggle toggle = new DrawerToggle();
//        H1 title = new H1("Zlagoda");
//        title.getStyle().set("font-size", "var(--lumo-font-size-l)").set("margin", "0");
//        addToNavbar(toggle, title);
//    }
//
//    private void configureDrawer() {
//        sidemenu.setWidth("250px");
//        Scroller scroller = new Scroller(sidemenu);
//        scroller.getElement().getStyle().set("scrollbar-width", "none");
//        scroller.setClassName(LumoUtility.Padding.SMALL);
//        addToDrawer(scroller);
//    }
//
//    private void configureContent() {
//        bar.setWidth("100%");
//        table.setMinWidth("90%");
//        VerticalLayout tableContainer = new VerticalLayout(table);
//        tableContainer.setSizeFull();
//        tableContainer.setPadding(false);
//        tableContainer.getStyle().set("overflow", "auto");
//        VerticalLayout content = new VerticalLayout(bar, tableContainer);
//        content.setSizeFull();
//        setContent(content);
//    }
//}