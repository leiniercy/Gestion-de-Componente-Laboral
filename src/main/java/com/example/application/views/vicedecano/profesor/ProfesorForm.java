/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.application.views.vicedecano.profesor;

import com.example.application.data.entity.*;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
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
    private Button close = new Button("Cancelar", VaadinIcon.REFRESH.create());


    private BeanValidationBinder<Profesor> binder = new BeanValidationBinder<>(Profesor.class);

    public ProfesorForm(List<User> users, List<Area> areas) {

        addClassName("rofesor-form");

        binder.bindInstanceFields(this);

        //Config form
        //nombre
        nombre.setLabel("Nombre");
        nombre.getElement().setAttribute("nombre", "Ejemplo: Daniel");
        nombre.setAutofocus(true);
        nombre.setRequired(true);
        nombre.setMinLength(2);
        nombre.setMaxLength(100);
        nombre.setPattern("^[a-zA-Z][a-zA-Z\\s]+$");
        nombre.setErrorMessage("Solo letras, mínimo 2 caracteres y máximo 100");
        nombre.addValueChangeListener(event -> {
            event.getSource().setHelperText(event.getValue().length() + "/" +100);
        });
        //apellidos
        apellidos.setLabel("Apellidos");
        apellidos.getElement().setAttribute("apellidos", "Ejemplo:Perez Diaz");
        apellidos.setAutofocus(true);
        apellidos.setRequired(true);
        apellidos.setMinLength(3);
        apellidos.setMaxLength(100);
        apellidos.setPattern("^[a-zA-Z][a-zA-Z\\s]*$");
        apellidos.setErrorMessage("Solo letras, mínimo 3 caracteres y máximo 100");
        apellidos.addValueChangeListener(event -> {
            event.getSource().setHelperText(event.getValue().length() + "/" +100);
        });
        //usuario
        user.setItems(users);
        user.setItemLabelGenerator(User::getName);
        //email
        email.setLabel("Correo");
        email.setValue("usuario");
        email.setSuffixComponent(new Div(new Text("@estudiantes.uci.cu")));
        email.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
        nombre.setAutoselect(true);
        nombre.setClearButtonVisible(true);
        email.setPattern("^[a-zA-Z][a-zA-Z0-9_\\.][a-zA-Z0-9]+(@estudiantes\\.uci\\.cu)$");
        email.setErrorMessage("Por favor escriba un correo válido");
        email.setClearButtonVisible(true);
        //solapin
        solapin.setLabel("Solapín");
        solapin.getElement().setAttribute("solapin", "E1705587");
        solapin.setAutofocus(true);
        solapin.setRequired(true);
        solapin.setMinLength(7);
        solapin.setMaxLength(7);
        solapin.setPattern("^[A-Z][0-9]+$");
        solapin.setErrorMessage("Una letra , mínimo 7 caracteres y máximo 7");
        solapin.addValueChangeListener(event -> {
            event.getSource().setHelperText(event.getValue().length() + "/" +7);
        });
        //area
        a.setItems(areas);
        a.setItemLabelGenerator(Area::getNombre);

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

        close.addClickListener(event -> fireEvent(new CloseEvent(this)));
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        close.addClickShortcut(Key.ESCAPE);

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, close);
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
        binder.readBean(profesor);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(profesor);
            fireEvent(new SaveEvent(this, profesor));
            Notification.show("Profesor añadido");
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
