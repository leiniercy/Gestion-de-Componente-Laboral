/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.application.views.vicedecano.profesor;

import com.example.application.data.entity.Area;
import com.example.application.data.entity.Grupo;
import com.example.application.data.entity.Profesor;
import com.example.application.data.entity.User;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import java.util.List;

/**
 *
 * @author Leinier
 */
public class ProfesorForm extends FormLayout {

    private Profesor profesor;

    private TextField nombre = new TextField("Nombre");
    private TextField apellidos = new TextField("Apellidos");
    private ComboBox<User> user = new ComboBox<>("Usuario");
    private EmailField email = new EmailField("Correo");
    private TextField solapin = new TextField("Solapin");
    private ComboBox<Area> a = new ComboBox<>("Area");

    private Button save = new Button("Añadir", VaadinIcon.PLUS.create());
    private Button close = new Button("Cancelar", VaadinIcon.ERASER.create());
    private Button delete = new Button("Eliminar", VaadinIcon.REFRESH.create());

    private BeanValidationBinder<Profesor> binder = new BeanValidationBinder<>(Profesor.class);

    public ProfesorForm(List<User> users, List<Area> areas) {

        addClassName("rofesor-form");

        binder.bindInstanceFields(this);

        user.setItems(users);
        user.setItemLabelGenerator(User::getName);

        a.setItems(areas);
        a.setItemLabelGenerator(Area::getNombre);

        //Config form
        //nombre
        //apellidos
        //usuario
        //email
        email.setPlaceholder("usuario@uci.cu");
        email.setPattern("^.+@uci\\.cu$");
        email.setErrorMessage("Por favor escriba un correo válido");
        email.setClearButtonVisible(true);
        //solapin
        //area

        add(
                nombre,
                apellidos,
                user,
                email,
                solapin,
                a,
                createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout() {

        save.addClickListener(event -> validateAndSave());
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickShortcut(Key.ENTER);

        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, profesor)));
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        delete.addClickShortcut(Key.DELETE);

        close.addClickListener(event -> fireEvent(new CloseEvent(this)));
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        close.addClickShortcut(Key.ESCAPE);

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, delete, close);
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
        binder.readBean(profesor);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(profesor);
            fireEvent(new SaveEvent(this, profesor));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    // Events
    public static abstract class ProfesorFormEvent extends ComponentEvent<ProfesorForm> {

        private Profesor profesor;

        protected ProfesorFormEvent(ProfesorForm source, Profesor profesor) {
            super(source, false);
            this.profesor = profesor;
        }

        public Profesor getProfesor() {
            return profesor;
        }
    }

    public static class SaveEvent extends ProfesorFormEvent {

        SaveEvent(ProfesorForm source, Profesor profesor) {
            super(source, profesor);
        }
    }

    public static class DeleteEvent extends ProfesorFormEvent {

        DeleteEvent(ProfesorForm source, Profesor profesor) {
            super(source, profesor);
        }

    }

    public static class CloseEvent extends ProfesorFormEvent {

        CloseEvent(ProfesorForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
            ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

}
