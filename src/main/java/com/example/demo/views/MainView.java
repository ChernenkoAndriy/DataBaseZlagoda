package com.example.demo.views;

import com.example.demo.views.viewmanagers.MainViewService;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
//клас репрезентує головну сторінку(в нашому випадку логін сторінку)

//аннотація Route потрібна щоб дати назву сторінки
//наприклад для сторінки https://github.com/ChernenkoAndriy/DataBaseZlagoda Route("DataBaseZlagoda")
//route потрібен щоб правильно показувати адресу сторінки в адресній строці
//в даному випадку нічого не потрібно
//але при переході на наступну сторінку строка оновлюється і додається route сторінки на яку ми перейдемо
//Ім'я route мусить бути завжди ункальним, інакше проект видасть помилку
//Для кожної сторінки View, яку ми використаємо ми мусимо створювати route
@Route("")
public class MainView extends VerticalLayout { //класи для сторінок мають закінчуватись на View
    //для кожної сторінки View ми будемо створювати ViewManager, в якому описуватиметься бекенд сторінки
    //конкретно цей клас буде наслідуватись від VerticalLayout, щоб елемнти додавались по сторінці в стовпчик
    private LoginForm loginForm;//поле для об'єкту що репрезентує формочку для логіна
    public MainView() {
        loginForm = new LoginForm();
        loginForm.setForgotPasswordButtonVisible(false); //прибирає кнопку для забутого пароля
        // (у нас всі будуть його пам'ятати)
        loginForm.addLoginListener(loginEvent -> MainViewService.authenticate(loginEvent.getUsername(),
                loginEvent.getPassword(), loginForm));
        //метод назначає метод на кнопку login
        //за допопмогою lambda вона буде виконувати authenticate з параметрами формочки
        add(loginForm); //додає елемент
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER); // ці методи центрують форму на сторінці
    }


    //метод отримує результат аутентифікації через бд і
    // в залежності від його результату переправляє користувача на наступну сторінку, чи каже що щось не те

}
