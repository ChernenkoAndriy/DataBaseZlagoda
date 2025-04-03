package com.example.demo.views;

import com.example.demo.views.components.*;
import com.example.demo.views.viewmanagers.MEService;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import database_manegment.database_entities.Employee;
import java.util.ArrayList;

@Route("manager")
public class ManagerEmployeeView extends AppLayout {
    private SideMenu sidemenu;
    private final ListDataProvider<Employee> dataProvider;
    private final Grid<Employee> table;
    private final ArrayList<Employee> data;
    private final EmployeeToolbar bar;
    private MEService MEService;

    public ManagerEmployeeView() {
        this.data = new ArrayList<>();
        this.dataProvider = new ListDataProvider<>(data);
        MEService = new MEService(data, dataProvider);
        this.sidemenu = new SideMenu();
        this.table = new EmployeeTable(dataProvider, MEService);
        this.bar = new EmployeeToolbar(MEService);
        configureNavbar();
        configureDrawer();
        configureContent();
    }

    private void configureNavbar() {
        DrawerToggle toggle = new DrawerToggle();
        H1 title = new H1("Zlagoda");
        title.getStyle().set("font-size", "var(--lumo-font-size-l)").set("margin", "0");
        addToNavbar(toggle, title);
    }

    private void configureDrawer() {
        sidemenu.setWidth("250px");
        Scroller scroller = new Scroller(sidemenu);
        scroller.getElement().getStyle().set("scrollbar-width", "none");
        scroller.setClassName(LumoUtility.Padding.SMALL);
        addToDrawer(scroller);
    }

    private void configureContent() {
        bar.setWidth("100%");
        table.setMinWidth("130%");
        VerticalLayout tableContainer = new VerticalLayout(table);
        tableContainer.setSizeFull();
        tableContainer.setPadding(false);
        tableContainer.getStyle().set("overflow", "auto");
        VerticalLayout content = new VerticalLayout(bar, tableContainer);
        content.setSizeFull();
        setContent(content);
    }
}
