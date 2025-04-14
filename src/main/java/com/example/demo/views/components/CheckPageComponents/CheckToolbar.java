package com.example.demo.views.components.CheckPageComponents;

import com.example.demo.views.services.CheckService;
import com.example.demo.views.repositories.database_entities.Check;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;

import java.util.List;
import java.util.function.Consumer;

public class CheckToolbar extends HorizontalLayout {
    private final TextField filter = new TextField("Filter by cashier");
    private final Button addButton = new Button("Add");
    private Consumer<Void> updateListener;

    public CheckToolbar() {
        add(filter, addButton);

        filter.addValueChangeListener(e -> {
            if (updateListener != null) updateListener.accept(null);
        });
    }

    public List<Check> getAllByFilters(CheckService service) {
        return service.getAllEntities(); // тимчасово без фільтра
    }

    public Button getAddButton() {
        return addButton;
    }

    public void addUpdateListener(Consumer<Void> listener) {
        this.updateListener = listener;
    }
}
