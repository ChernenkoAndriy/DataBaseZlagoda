package com.example.demo.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.spring.security.AuthenticationContext;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class MainLayout extends AppLayout {

    private final AuthenticationContext authContext;
    private final SideNav sideNav;

    public MainLayout(AuthenticationContext authContext) {
        this.authContext = authContext;
        this.sideNav = new SideNav();
        setupNavbar();
        setupDrawer();
    }

    private void setupNavbar() {
        DrawerToggle toggle = new DrawerToggle();
        H1 title = new H1("Zlagoda");
        title.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        addToNavbar(toggle, title);
    }

    private void setupDrawer() {
        sideNav.setWidth("250px");

        VerticalLayout drawerContent = new VerticalLayout();
        drawerContent.setPadding(false);
        drawerContent.setSpacing(false);
        drawerContent.setSizeFull();

        addMenuItemsBasedOnRole();
        drawerContent.add(sideNav);
        drawerContent.expand(sideNav);

        authContext.getAuthenticatedUser(UserDetails.class).ifPresent(user -> {
            Span username = new Span("Welcome, " + user.getUsername());
            username.addClassNames(LumoUtility.Margin.Left.MEDIUM, LumoUtility.FontSize.MEDIUM);

            Button logoutButton = new Button("Logout", e -> authContext.logout());
            logoutButton.addClassNames(
                    LumoUtility.Margin.MEDIUM,
                    LumoUtility.TextColor.ERROR,
                    LumoUtility.FontWeight.BOLD
            );

            drawerContent.add(username, logoutButton);
        });

        Scroller scroller = new Scroller(drawerContent);
        scroller.addClassName(LumoUtility.Padding.SMALL);
        scroller.getStyle().set("scrollbar-width", "none");

        addToDrawer(scroller);
    }

    private void addMenuItemsBasedOnRole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getAuthorities() == null) {
            return;
        }

        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_MANAGER"))) {
            sideNav.addItem(new SideNavItem("Employees", "manager"));
            sideNav.addItem(new SideNavItem("Customers", "customers"));
            sideNav.addItem(new SideNavItem("Warehouse Content", "warehouse"));
            sideNav.addItem(new SideNavItem("Product Categories", "categories"));
            sideNav.addItem(new SideNavItem("Product Items", "products"));
            sideNav.addItem(new SideNavItem("Checks", "checks"));
            sideNav.addItem(new SideNavItem("Sales Report", "sales-report"));
            sideNav.addItem(new SideNavItem("Employee Report", "employee-report")); // Added Employee Report link
        } else if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CASHIER"))) {
            sideNav.addItem(new SideNavItem("Customers", "customers"));
            sideNav.addItem(new SideNavItem("Checks", "checks"));
        }
    }
}
