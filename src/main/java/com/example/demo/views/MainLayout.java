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

    private final SideNav sideMenu = new SideNav();
    private final AuthenticationContext authContext;

    public MainLayout(AuthenticationContext authContext) {
        this.authContext = authContext;
        configureNavbar();
        configureDrawer();
    }

    private void configureNavbar() {
        DrawerToggle toggle = new DrawerToggle();
        H1 title = new H1("Zlagoda");
        title.getStyle()
                .set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0");
        addToNavbar(toggle, title);
    }

    private void configureDrawer() {
        sideMenu.setWidth("250px");
        VerticalLayout drawerContent = new VerticalLayout();
        drawerContent.setPadding(false);
        drawerContent.setSpacing(false);
        drawerContent.setSizeFull();
        addMenuItemsBasedOnRole();
        drawerContent.add(sideMenu);
        drawerContent.expand(sideMenu);

        // Додаємо ім'я користувача та кнопку logout
        authContext.getAuthenticatedUser(UserDetails.class).ifPresent(user -> {
            Span username = new Span("Welcome " + user.getUsername());
            username.getStyle().set("margin-left", "1em");

            Button logoutButton = new Button("Logout", e -> authContext.logout());
            logoutButton.getStyle()
                    .set("margin", "1em")
                    .set("color", "var(--lumo-error-color)")
                    .set("font-weight", "600");

            drawerContent.add(username, logoutButton);
        });

        Scroller scroller = new Scroller(drawerContent);
        scroller.setClassName(LumoUtility.Padding.SMALL);
        scroller.getElement().getStyle().set("scrollbar-width", "none");

        addToDrawer(scroller);
    }

    private void addMenuItemsBasedOnRole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_MANAGER"))) {
            sideMenu.addItem(new SideNavItem("Employees", "manager"));
            sideMenu.addItem(new SideNavItem("Customers", "customers"));
            sideMenu.addItem(new SideNavItem("Warehouse content", "warehouse"));
            sideMenu.addItem(new SideNavItem("Product Categories", "categories"));
            sideMenu.addItem(new SideNavItem("Product Items", "products"));
            sideMenu.addItem(new SideNavItem("Checks", "checks"));
            sideMenu.addItem(new SideNavItem("Top Customers", "top-customers"));

        } else if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CASHIER"))) {
            sideMenu.addItem(new SideNavItem("Customers", "customers"));
            sideMenu.addItem(new SideNavItem("Checks", "checks"));
        }
    }
}
