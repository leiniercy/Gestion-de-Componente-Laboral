/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.application.views.jefe_area.evaluacion;

import com.example.application.data.DataService;
import com.example.application.data.entity.Evaluacion;
import com.example.application.data.entity.Estudiante;
import com.example.application.data.entity.Tarea;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import javax.annotation.security.RolesAllowed;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Arrays;

/**
 *
 * @author Leinier
 */
@PageTitle("Evaluaciones")
@Route(value = "evaluaciones-view", layout = MainLayout.class)
@Uses(Icon.class)
@RolesAllowed("jefeArea")
public class EvaluacionesView extends VerticalLayout {

    private Grid<Evaluacion> grid = new Grid<>(Evaluacion.class, false);

    EvaluacionForm form;

    private Evaluacion evaluacion;

    private DataService dataService;

    private GridListDataView<Evaluacion> gridListDataView;

    private Grid.Column<Evaluacion> notaColumn = grid.addColumn(Evaluacion::getNota).setHeader("Nota").setAutoWidth(true);
    private Grid.Column<Evaluacion> descripcionColumn = grid.addColumn(Evaluacion::getDescripcion).setHeader("Descripción").setAutoWidth(true);
    private Grid.Column<Evaluacion> estudianteColumn = grid.addColumn(evaluacion -> evaluacion.getEstudiante().getStringNombreApellidos()).setHeader("Estudiante").setAutoWidth(true);
    private Grid.Column<Evaluacion> tareaColumn = grid.addColumn(evaluacion -> evaluacion.getTarea().getNombre()).setHeader("Tarea").setAutoWidth(true);
    private Grid.Column<Evaluacion> statusColumn = grid.addColumn(evaluacion -> evaluacion.getStatus()).setHeader("Status").setAutoWidth(true);

    public EvaluacionesView(
            @Autowired DataService dataService
    ) {

        this.dataService = dataService;
        addClassNames("evaluacion-view", "flex", "flex-col", "h-full");
        setSizeFull();
        configureGrid();

        form = new EvaluacionForm();
        form.setWidth("25em");
        form.addListener(EvaluacionForm.SaveEvent.class, this::saveEvaluacion);
        form.addListener(EvaluacionForm.DeleteEvent.class, this::deleteEvaluacion);
        form.addListener(EvaluacionForm.CloseEvent.class, e -> closeEditor());

        FlexLayout content = new FlexLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.setFlexShrink(0, form);
        content.addClassNames("content", "gap-m");
        content.setSizeFull();

        add(getToolbar(), content);
        updateList();
        closeEditor();
        grid.asSingleSelect().addValueChangeListener(event
                -> editEvaluacion(event.getValue()));

    }

    private void configureGrid() {

        HeaderRow headerRow = grid.appendHeaderRow();
        headerRow.getCell(notaColumn).setComponent(FiltrarNota());
        headerRow.getCell(descripcionColumn).setComponent(FiltrarDescripcion());
        headerRow.getCell(estudianteColumn).setComponent(FiltrarEstudinate());
        headerRow.getCell(tareaColumn).setComponent(FiltarTarea());
        headerRow.getCell(statusColumn).setComponent(FiltarStatus());

        gridListDataView = grid.setItems(dataService.findAllEvaluacion());
        grid.addClassNames("evaluacion-grid");
        grid.setSizeFull();
        grid.setHeightFull();
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private HorizontalLayout getToolbar() {

        addClassName("menu-items");
        Html total = new Html("<span>Total: <b>" + dataService.countEvaluacion()+ "</b> evaluaciones</span>");

        Button addButton = new Button("Añadir Evaluación ", VaadinIcon.USER.create());
        addButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        addButton.addClickListener(click -> addEvaluacion());

        HorizontalLayout toolbar = new HorizontalLayout(total, addButton);
        toolbar.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        toolbar.setWidth("100%");
        toolbar.expand(total);

        return toolbar;
    }

    private void saveEvaluacion(EvaluacionForm.SaveEvent event) {
        dataService.saveEvaluacion(event.getEvaluacion());
        updateList();
        closeEditor();
    }

    private void deleteEvaluacion(EvaluacionForm.DeleteEvent event) {
        dataService.deleteEvaluacion(event.getEvaluacion());
        updateList();
        closeEditor();
    }

    public void editEvaluacion(Evaluacion evaluacion) {
        if (evaluacion == null) {
            closeEditor();
        } else {
            form.setEvaluacion(evaluacion);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    void addEvaluacion() {
        grid.asSingleSelect().clear();
        editEvaluacion(new Evaluacion());
    }

    private void closeEditor() {
        form.setEvaluacion(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(dataService.findAllEvaluacion());
    }

    //Filtros
    private TextField FiltrarNota() {

        TextField filterNota = new TextField();
        filterNota.setPlaceholder("Filtrar");
        filterNota.setClearButtonVisible(true);
        filterNota.setWidth("100%");
        filterNota.setValueChangeMode(ValueChangeMode.LAZY);
        filterNota.addValueChangeListener(e -> {

        });

        return filterNota;
    }

    private TextField FiltrarDescripcion() {

        TextField filterDescripcion = new TextField();
        filterDescripcion.setPlaceholder("Filtrar");
        filterDescripcion.setClearButtonVisible(true);
        filterDescripcion.setWidth("100%");
        filterDescripcion.setValueChangeMode(ValueChangeMode.LAZY);
        filterDescripcion.addValueChangeListener(e -> {

        });
        return filterDescripcion;
    }

    private ComboBox<Estudiante> FiltrarEstudinate() {

        ComboBox<Estudiante> filterEstudiante = new ComboBox<>();
        filterEstudiante.setItems(dataService.findAllEstudiante());
        filterEstudiante.setItemLabelGenerator(Estudiante::getStringNombreApellidos);
        filterEstudiante.setPlaceholder("Filtrar");
        filterEstudiante.setClearButtonVisible(true);
        filterEstudiante.setWidth("100%");
        filterEstudiante.addValueChangeListener(e -> {

        });
        return filterEstudiante;
    }

    private ComboBox<Tarea> FiltarTarea() {

        ComboBox<Tarea> filterTarea = new ComboBox<>();
        filterTarea.setItems(dataService.findAllTareas());
        filterTarea.setItemLabelGenerator(Tarea::getNombre);
        filterTarea.setPlaceholder("Filter");
        filterTarea.setClearButtonVisible(true);
        filterTarea.setWidth("100%");
        filterTarea.addValueChangeListener(e -> {

        });

        return filterTarea;
    }

    private ComboBox<String> FiltarStatus() {

        ComboBox<String> statusFilter = new ComboBox<>();
        statusFilter.setItems(Arrays.asList("Pendiente", "Completada", "No Completada"));
        statusFilter.setPlaceholder("Filter");
        statusFilter.setClearButtonVisible(true);
        statusFilter.setWidth("100%");
        statusFilter.addValueChangeListener(e -> {

        });

        return statusFilter;
    }

}
