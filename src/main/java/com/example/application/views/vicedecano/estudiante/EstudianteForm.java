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
import com.example.application.views.Prueba.*;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

import java.util.List;

/**
 *
 * @author Leinier
 */
public class EstudianteForm extends FormLayout {

    private Estudiante estudiante;

    private TextField nombre = new TextField("Nombre");
    private TextField apellidos = new TextField("Apellidos");
    private ComboBox<User> user = new ComboBox<>("Usuario");
    private EmailField email = new EmailField("Correo");
    private TextField solapin = new TextField("Solapin");
    private IntegerField anno_repitencia = new IntegerField("Año de repitencia");
    private IntegerField cantidad_asignaturas = new IntegerField("Cantidad de asignaturas");
    private ComboBox<Area> area = new ComboBox<>("Area");
    private ComboBox<Grupo> grupo = new ComboBox<>("Grupo");

    private Button save = new Button("Añadir", VaadinIcon.PLUS.create());
    private Button close = new Button("Cancelar", VaadinIcon.ERASER.create());
    private Button delete = new Button("Eliminar", VaadinIcon.REFRESH.create());

    private BeanValidationBinder<Estudiante> binder = new BeanValidationBinder<>(Estudiante.class);

    public EstudianteForm(List<User> users, List<Area> areas, List<Grupo> grupos) {
        addClassName("estudiante-form");
        binder.bindInstanceFields(this);

        user.setItems(users);
        user.setItemLabelGenerator(User::getName);

        area.setItems(areas);
        area.setItemLabelGenerator(Area::getNombre);

        grupo.setItems(grupos);
        grupo.setItemLabelGenerator(Grupo::getNumero);

        //Config form
        
        //nombre
        //apellidos
        //usuario
        //email
        email.setPlaceholder("usuario@estdiantes.uci.cu");
        email.setPattern("^.+@estudiantes.uci\\.cu$");
        email.setErrorMessage("Por favor escriba un correo válido");
        email.setClearButtonVisible(true);
        //solapin
        //grupo
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
        //area

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
      
        
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, estudiante)));
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        delete.addClickShortcut(Key.DELETE);
        
        
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        close.addClickShortcut(Key.ESCAPE);
        
        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, delete, close);
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
