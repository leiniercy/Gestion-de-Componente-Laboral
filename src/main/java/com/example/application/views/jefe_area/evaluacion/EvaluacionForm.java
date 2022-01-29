/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.application.views.jefe_area.evaluacion;


import com.example.application.data.entity.Estudiante;
import com.example.application.data.entity.Evaluacion;
import com.example.application.data.entity.Tarea;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.shared.Registration;

import java.util.Arrays;
import java.util.List;

/**
 * @author Leinier
 */
public class EvaluacionForm extends FormLayout {

    private Evaluacion evaluacion;

    private TextField nota = new TextField();
    private TextArea descripcion = new TextArea();
    private ComboBox<Estudiante> estudiante = new ComboBox<>("Estudiante");
    private ComboBox<Tarea> tarea = new ComboBox<>("Tarea");
    private ComboBox<String> status = new ComboBox<>("Status");

    private Button save = new Button("Añadir", VaadinIcon.PLUS.create());
    private Button close = new Button("Cancelar", VaadinIcon.REFRESH.create());

    private BeanValidationBinder<Evaluacion> binder = new BeanValidationBinder<>(Evaluacion.class);

    public EvaluacionForm(List<Estudiante> estudiantes, List<Tarea> tareas) {

        addClassName("evaluacion-form");

        binder.bindInstanceFields(this);

        //Config form

        //nota
        nota.setLabel("Nota");
        nota.setHelperText("Cualiatativa(B,R,M)");
        nota.getElement().setAttribute("nota", "Ejemplo: B");
        nota.setAutofocus(true);
        nota.setRequired(true);
        nota.setMinLength(1);
        nota.setMaxLength(1);
        nota.setPattern("^(B|M|R)$");
        nota.setErrorMessage("Solo letras, mínimo 1 caracteres y máximo 1");

        //descripcion
        descripcion.setLabel("Descripción");
        descripcion.setWidthFull();
        descripcion.setMinLength(3);
        descripcion.setMaxLength(255);
        descripcion.setMinHeight("100px");
        descripcion.setMaxHeight("150px");

      //  descripcion.setPattern("^[a-zA-Z][a-zA-Z\\s]+$");
        descripcion.setErrorMessage("Solo caracteres y numeros, mínimo 3 caracteres y  máximo 255");
        descripcion.setValueChangeMode(ValueChangeMode.EAGER);
        descripcion.addValueChangeListener(e -> {
            e.getSource().setHelperText(e.getValue().length() + "/" + 255);
        });
        //estudiante
        estudiante.setItems(estudiantes);
        estudiante.setItemLabelGenerator(Estudiante::getStringNombreApellidos);
        //tarea
        tarea.setItems(tareas);
        tarea.setItemLabelGenerator(Tarea::getNombre);
        //status
        status.setItems(Arrays.asList("Pendiente", "Completada", "No Completada"));

        add(
                nota,
                descripcion,
                estudiante,
                tarea,
                status,
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

    public void setEvaluacion(Evaluacion evaluacion) {
        this.evaluacion = evaluacion;
        binder.readBean(evaluacion);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(evaluacion);
            fireEvent(new SaveEvent(this, evaluacion));
            Notification.show("Evaluación añadida");
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    // Events
    public static abstract class EvaluacionFormEvent extends ComponentEvent<EvaluacionForm> {

        private Evaluacion evaluacion;

        protected EvaluacionFormEvent(EvaluacionForm source, Evaluacion evaluacion) {
            super(source, false);
            this.evaluacion = evaluacion;
        }

        public Evaluacion getEvaluacion() {
            return evaluacion;
        }
    }

    public static class SaveEvent extends EvaluacionFormEvent {

        SaveEvent(EvaluacionForm source, Evaluacion evaluacion) {
            super(source, evaluacion);
        }
    }

    public static class DeleteEvent extends EvaluacionFormEvent {

        DeleteEvent(EvaluacionForm source, Evaluacion evaluacion) {
            super(source, evaluacion);
        }

    }

    public static class CloseEvent extends EvaluacionFormEvent {

        CloseEvent(EvaluacionForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }


}
