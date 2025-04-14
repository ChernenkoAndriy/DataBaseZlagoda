package com.example.demo.views.components.CheckPageComponents;

import com.example.demo.views.repositories.database_entities.Check;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;

public class CheckForm extends VerticalLayout {
    private Check check;

    private final Button save = new Button("Save");
    private final Button delete = new Button("Delete");
    private final Button close = new Button("Cancel");

    public CheckForm() {
        add(save, delete, close);

        save.addClickListener(e -> fireEvent(new SaveCheckEvent(this, check)));
        delete.addClickListener(e -> fireEvent(new DeleteCheckEvent(this, check)));
        close.addClickListener(e -> fireEvent(new CloseEvent(this)));
    }

    public void setCheck(Check c) {
        this.check = c;
    }

    public Check getCheck() {
        return this.check;
    }

    public void setInvalidData() {
        // Поки заглушка
    }

    public void open() {
        setVisible(true);
    }

    public void close() {
        setVisible(false);
    }

    // Events
    public static class SaveCheckEvent extends ComponentEvent<CheckForm> {
        private final Check check;
        public SaveCheckEvent(CheckForm source, Check check) {
            super(source, false);
            this.check = check;
        }
        public Check getCheck() {
            return check;
        }
    }

    public static class DeleteCheckEvent extends ComponentEvent<CheckForm> {
        private final Check check;
        public DeleteCheckEvent(CheckForm source, Check check) {
            super(source, false);
            this.check = check;
        }
        public Check getCheck() {
            return check;
        }
    }

    public static class CloseEvent extends ComponentEvent<CheckForm> {
        public CloseEvent(CheckForm source) {
            super(source, false);
        }
    }

    public Registration addSaveListener(ComponentEventListener<SaveCheckEvent> listener) {
        return addListener(SaveCheckEvent.class, listener);
    }

    public Registration addDeleteListener(ComponentEventListener<DeleteCheckEvent> listener) {
        return addListener(DeleteCheckEvent.class, listener);
    }

    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
        return addListener(CloseEvent.class, listener);
    }
}
