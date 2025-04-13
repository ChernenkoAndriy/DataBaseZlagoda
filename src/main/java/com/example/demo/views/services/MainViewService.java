//package com.example.demo.views.services;
//
//import com.example.demo.views.CashierInitialView;
//import com.example.demo.views.ManagerEmployeeView;
//import com.vaadin.flow.component.UI;
//import com.vaadin.flow.component.login.LoginForm;
//import com.vaadin.flow.component.notification.Notification;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
////цей клас бекенд репрезентація LoginView, що надає статичний метод для логіну
////ми змінимо його в майбутньому щоб допилити шифрування
//public class MainViewService {
//    //всі запити ми будемо робити ось так
//    //якщо дані в запит підставляються, то позначаємо їх знаком ?
//    private static final String QUERY =
//            "SELECT empl_role FROM \"Employee\" WHERE empl_name = ? AND empl_surname = ? AND zip_code = ?";
//    //метод через isValidUser перевіряє дані в базі даних а потім направляє на наступну сторінку залежно від
//    //ролі користувача або каже про помилку при реєстрації
//    public static boolean authenticate(String username, String password, LoginForm loginForm) {
//        String[] nameAndSurname = username.split(" ");
//        if (nameAndSurname.length < 2) {
//            Notification.show("Invalid username or password", 3000, Notification.Position.MIDDLE);
//            loginForm.setError(true);
//            return false;
//        }
//        int userRole = isValidUser(nameAndSurname[0], nameAndSurname[1], password);
//        if (userRole == 1) {
//            UI.getCurrent().navigate(CashierInitialView.class);
//            return true;
//        } else if (userRole == 2) {
//            UI.getCurrent().navigate(ManagerEmployeeView.class);
//            return true;
//        } else {
//            Notification.show("Invalid username or password", 3000, Notification.Position.MIDDLE);
//            loginForm.setError(true);
//            return false;
//        }
//    }
//    //робить запит в бд
//    public static int isValidUser(String username, String surname, String password) {
//        try (Connection conn = DatabaseConnectionPool.getConnection();//ось так започатковувати з'єднання з бд
//             PreparedStatement stmt = conn.prepareStatement(QUERY)) {//загортаємо запит в такий об'єкт для безепеки
//            stmt.setString(1, surname);
//            stmt.setString(2, username);
//            stmt.setString(3, password); //підставляємо дані замість ?
//            ResultSet rs = stmt.executeQuery();//отримуємо колекцію з результатом запиту
//            while (rs.next()) {//ітеруємо по колекції
//                String role = rs.getString("empl_role");
//                if ("Cashier".equals(role)) {
//                    return 1;
//                } else if ("Manager".equals(role)) {
//                    return 2;
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return 0; //повертаємо числа 0 1 2 якщо працівника немає, його посада касир, його посада менеджер
//    }
//}
