package com.example.demo.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.theme.lumo.LumoUtility;
public class ManagerLayout extends AppLayout {
    private SideNav sideMenu;
    ManagerLayout(){
        sideMenu = new SideNav();
        sideMenu.addItem(new SideNavItem("Employees", "manager"));
        sideMenu.addItem(new SideNavItem("Customers", "customers"));
        sideMenu.addItem(new SideNavItem("Warehouse content", "warehouse"));
        sideMenu.addItem(new SideNavItem("Product Categories", "categories"));
        sideMenu.addItem(new SideNavItem("Product Items", "product_tems"));
        sideMenu.addItem(new SideNavItem("Checks", "checks"));
        configureNavbar();
        configureDrawer();

    }
    private void configureNavbar() {
        DrawerToggle toggle = new DrawerToggle();
        H1 title = new H1("Zlagoda");
        title.getStyle().set("font-size", "var(--lumo-font-size-l)").set("margin", "0");
        addToNavbar(toggle, title);
    }

    private void configureDrawer() {
        sideMenu.setWidth("250px");
        Scroller scroller = new Scroller(sideMenu);
        scroller.getElement().getStyle().set("scrollbar-width", "none");
        scroller.setClassName(LumoUtility.Padding.SMALL);
        addToDrawer(scroller);
    }
}
