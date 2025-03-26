package com.example.demo.views.viewmanagers;

import com.example.demo.views.CashierInitialView;
import com.example.demo.views.ManagerInitialView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import database_manegment.database_entities.DatabaseConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MainViewManager {
    private static final String QUERY =
            "SELECT empl_role FROM \"Employee\" WHERE empl_name = ? AND empl_surname = ? AND zip_code = ?";

    public static boolean authenticate(String username, String password, LoginForm loginForm) {
        String[] nameAndSurname = username.split(" ");
        if (nameAndSurname.length < 2) {
            Notification.show("Invalid username or password", 3000, Notification.Position.MIDDLE);
            loginForm.setError(true);
            return false;
        }
        int userRole = isValidUser(nameAndSurname[0], nameAndSurname[1], password);
        if (userRole == 1) {
            UI.getCurrent().navigate(CashierInitialView.class);
            return true;
        } else if (userRole == 2) {
            UI.getCurrent().navigate(ManagerInitialView.class);
            return true;
        } else {
            Notification.show("Invalid username or password", 3000, Notification.Position.MIDDLE);
            loginForm.setError(true);
            return false;
        }
    }

    public static int isValidUser(String username, String surname, String password) {
        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(QUERY)) {
            stmt.setString(1, surname);
            stmt.setString(2, username);
            stmt.setString(3, password);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String role = rs.getString("empl_role");
                if ("Cashier".equals(role)) {
                    return 1;
                } else if ("Manager".equals(role)) {
                    return 2;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
