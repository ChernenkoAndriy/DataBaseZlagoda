package com.example.demo.views;

import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;

public class SideMenu extends SideNav {

    public SideMenu() {
       addItem(new SideNavItem("Employees", "manager"));
       addItem(new SideNavItem("Customers", "customers"));
       addItem(new SideNavItem("Warehouse content", "warehouse"));
       addItem(new SideNavItem("Product Categories", "categories"));
       addItem(new SideNavItem("Product Items", "product_tems"));
       addItem(new SideNavItem("Checks", "checks"));
    }
}
