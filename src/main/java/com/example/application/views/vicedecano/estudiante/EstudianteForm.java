/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.application.views.vicedecano.estudiante;

import com.example.application.data.entity.Area;
import com.example.application.data.entity.Estudiante;
import com.example.application.data.entity.Grupo;
import com.example.application.data.entity.User;
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
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

import java.util.List;

/**
 * @author Leinier
 */
public class EstudianteForm extends FormLayout {

    private Estudiante estudiante;

    TextField nombre = new TextField();
    TextField apellidos = new TextField();
    ComboBox<User> user = new ComboBox<>("Usuario");
    EmailField email = new EmailField();
    TextField solapin = new TextField();
    IntegerField anno_repitencia = new IntegerField("Año de repitencia");
    IntegerField cantidad_asignaturas = new IntegerField("Cantidad de asignaturas");
    ComboBox<Area> area = new ComboBox<>("Area");
    ComboBox<Grupo> grupo = new ComboBox<>("Grupo");

    Button save = new Button("Añadir", VaadinIcon.PLUS.create());
    Button close = new Button("Cancelar", VaadinIcon.ERASER.create());

    BeanValidationBinder<Estudiante> binder = new BeanValidationBinder<>(Estudiante.class);

    public EstudianteForm(List<User> users, List<Area> areas, List<Grupo> grupos) {
        addClassName("estudiante-form");

        binder.bindInstanceFields(this);

        //Config form
        //nombre
        nombre.setLabel("Nombre");
        nombre.getElement().setAttribute("nombre", "Ejemplo: Daniel");
        nombre.setAutofocus(true);
        nombre.setRequired(true);
        nombre.setMinLength(2);
        nombre.setMaxLength(100);
        nombre.setPattern("^[a-zA-ZÀ-ÿ\\u00f1\\u00d1]+(\\s*[a-zA-ZÀ-ÿ\\u00f1\\u00d1]*)*[a-zA-ZÀ-ÿ\\u00f1\\u00d1]+$");
        nombre.setErrorMessage("Solo letras, mínimo 2 caracteres y máximo 100");
        nombre.addValueChangeListener(event -> {
            event.getSource().setHelperText(event.getValue().length() + "/" + 100);
        });
        //apellidos
        apellidos.setLabel("Apellidos");
        apellidos.getElement().setAttribute("apellidos", "Ejemplo:Perez Diaz");
        apellidos.setAutofocus(true);
        apellidos.setRequired(true);
        apellidos.setMinLength(3);
        apellidos.setMaxLength(100);
        apellidos.setPattern("^[a-zA-ZÀ-ÿ\\u00f1\\u00d1]+(\\s*[a-zA-ZÀ-ÿ\\u00f1\\u00d1]*)*[a-zA-ZÀ-ÿ\\u00f1\\u00d1]+$");
        apellidos.setErrorMessage("Solo letras, mínimo 3 caracteres y máximo 100");
        apellidos.addValueChangeListener(event -> {
            event.getSource().setHelperText(event.getValue().length() + "/" + 100);
        });
        //usuario
        user.setItems(users);
        user.setItemLabelGenerator(User::getUsername);
        //email
        email.setPlaceholder("usuario@estudiantes.uci.cu");
        email.setLabel("Correo");
        email.setValue("usuario@estudiantes.uci.cu");
        nombre.setAutoselect(true);
        nombre.setClearButtonVisible(true);
        email.setPattern("^[a-zA-Z][a-zA-Z0-9_\\.][a-zA-Z0-9]+(@estudiantes\\.uci\\.cu)$");
        email.setErrorMessage("Por favor escriba un correo válido");
        email.setClearButtonVisible(true);
        //solapin
        solapin.setLabel("Solapín");
        solapin.setPlaceholder("E1705587");
        solapin.getElement().setAttribute("solapin", "E1705587");
        solapin.setAutofocus(true);
        solapin.setRequired(true);
        solapin.setMinLength(7);
        solapin.setMaxLength(7);
        solapin.setPattern("^[A-Z][0-9]+$");
        solapin.setErrorMessage("Una letra , mínimo 7 caracteres y máximo 7");
        solapin.addValueChangeListener(event -> {
            event.getSource().setHelperText(event.getValue().length() + "/" + 7);
        });
        //anno_repitencia
        anno_repitencia.setHelperText("Máximo 5");
        anno_repitencia.setValue(1);
        anno_repitencia.setHasControls(true);
        anno_repitencia.setMin(1);
        anno_repitencia.setMax(5);
        //cantidad_asignaturas
        cantidad_asignaturas.setHelperText("Máximo 16");
        cantidad_asignaturas.setValue(2);
        cantidad_asignaturas.setHasControls(true);
        cantidad_asignaturas.setMin(2);
        cantidad_asignaturas.setMax(16);
        //grupo
        grupo.setItems(grupos);
        grupo.setItemLabelGenerator(Grupo::getNumero);
        //area
        area.setItems(areas);
        area.setItemLabelGenerator(Area::getNombre);

        add(
                nombre,
                apellidos,
                user,
                email,
                solapin,
                anno_repitencia,
                cantidad_asignaturas,
                area,
                grupo,
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

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
        binder.readBean(estudiante);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(estudiante);
            fireEvent(new SaveEvent(this, estudiante));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    // Events
    public static abstract class EstudianteFormEvent extends ComponentEvent<EstudianteForm> {

        private Estudiante estudiante;

        protected EstudianteFormEvent(EstudianteForm source, Estudiante estudiante) {
            super(source, false);
            this.estudiante = estudiante;
        }

        public Estudiante getEstudiante() {
            return estudiante;
        }
    }

    public static class SaveEvent extends EstudianteFormEvent {

        SaveEvent(EstudianteForm source, Estudiante estudiante) {
            super(source, estudiante);
        }
    }

    public static class DeleteEvent extends EstudianteFormEvent {

        DeleteEvent(EstudianteForm source, Estudiante estudiante) {
            super(source, estudiante);
        }

    }

    public static class CloseEvent extends EstudianteFormEvent {

        CloseEvent(EstudianteForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
            ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

}
