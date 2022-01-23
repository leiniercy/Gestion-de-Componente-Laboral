package com.example.application.views.vicedecano.lista_evaluaciones;

import com.example.application.data.DataService;
import com.example.application.data.entity.Estudiante;
import com.example.application.data.entity.Evaluacion;
import com.example.application.data.entity.Tarea;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.gridpro.GridPro;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;


import java.util.Arrays;
import java.util.List;
import javax.annotation.security.RolesAllowed;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("Evaluaciones")
@Route(value = "list-evaluaicones", layout = MainLayout.class)
@RolesAllowed("vicedecano")
public class ListadeEvaluacionesVicedecanoView extends Div {

    private GridPro<Evaluacion> grid;
    private GridListDataView<Evaluacion> gridListDataView;

    private Grid.Column<Evaluacion> notaColumn;
    private Grid.Column<Evaluacion> descripcionColumn;
    private Grid.Column<Evaluacion> estudianteColumn;
    private Grid.Column<Evaluacion> tareaColumn;
    private Grid.Column<Evaluacion> statusColumn;

    private DataService dataService;


    public ListadeEvaluacionesVicedecanoView(@Autowired DataService dataService) {
        this.dataService = dataService;
        addClassName("evaluacionesList-view");
        setSizeFull();
        createGrid();
        add(grid);
    }

    private void createGrid() {
        createGridComponent();
        addColumnsToGrid();
        addFiltersToGrid();
    }

    private void createGridComponent() {
        grid = new GridPro<>();
//        grid.setSelectionMode(SelectionMode.MULTI);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_COLUMN_BORDERS);
        grid.setHeight("100%");

        List<Evaluacion> evaluaciones = dataService.findAllEvaluacion();
        gridListDataView = grid.setItems(evaluaciones);
    }

    private void addColumnsToGrid() {
        createNotaColumn();
        createDescripcionColumn();
        createEstudianteColumn();
        createTareaColumn();
        createStatusColumn();
    }

    private void createNotaColumn() {
        notaColumn = grid.addColumn(Evaluacion::getNota, "nota").setHeader("Nota").setWidth("120px").setFlexGrow(0);
    }

    private void createDescripcionColumn() {
        descripcionColumn = grid.addColumn(Evaluacion::getDescripcion, "descripcion").setHeader("Descripción").setAutoWidth(true);
    }

    private void createEstudianteColumn() {
        estudianteColumn = grid.addColumn(new ComponentRenderer<>(evaluacion -> {
            HorizontalLayout hl = new HorizontalLayout();
            hl.setAlignItems(Alignment.CENTER);
            Span span = new Span();
            span.setClassName("nombre-apellidos");
            span.setText(evaluacion.getEstudiante().getStringNombreApellidos());
            hl.add(span);
            return hl;
        })).setComparator(evaluacion -> evaluacion.getEstudiante().getStringNombreApellidos()).setHeader("Estudiante").setAutoWidth(true);
    }


    private void createTareaColumn() {
        tareaColumn = grid.addColumn(new ComponentRenderer<>(evaluacion -> {
            HorizontalLayout hl = new HorizontalLayout();
            hl.setAlignItems(Alignment.CENTER);
            Span span = new Span();
            span.setClassName("nombre");
            span.setText(evaluacion.getTarea().getNombre());
            hl.add(span);
            return hl;
        })).setComparator(evaluacion -> evaluacion.getTarea().getNombre()).setHeader("Tarea").setAutoWidth(true);
    }

    private void createStatusColumn() {
        statusColumn = grid.addEditColumn(Evaluacion::getStatus, new ComponentRenderer<>(evaluacion -> {
                    Span span = new Span();
                    span.setText(evaluacion.getStatus());
                    span.getElement().setAttribute("theme", "badge " + evaluacion.getStatus().toLowerCase());
                    return span;
                })).select((item, newValue) -> item.setStatus(newValue), Arrays.asList("Pendiente", "Completado", "No Completado"))
                .setComparator(evaluacion -> evaluacion.getStatus()).setHeader("Estatus").setAutoWidth(true);
    }


    private void addFiltersToGrid() {
        HeaderRow filterRow = grid.appendHeaderRow();

        TextField filterNota = new TextField();
        filterNota.setPlaceholder("Filtrar");
        filterNota.setClearButtonVisible(true);
        filterNota.setWidth("100%");
        filterNota.setValueChangeMode(ValueChangeMode.LAZY);
        filterNota.addValueChangeListener(
                event -> gridListDataView
                        .addFilter(evaluacion -> StringUtils.containsIgnoreCase(evaluacion.getNota(), filterNota.getValue()))
        );
        filterRow.getCell(notaColumn).setComponent(filterNota);

        TextField filterDescripcion = new TextField();
        filterDescripcion.setPlaceholder("Filtrar");
        filterDescripcion.setClearButtonVisible(true);
        filterDescripcion.setWidth("100%");
        filterDescripcion.setValueChangeMode(ValueChangeMode.LAZY);
        filterDescripcion.addValueChangeListener(
                event -> gridListDataView
                        .addFilter(evaluacion -> StringUtils.containsIgnoreCase(evaluacion.getDescripcion(), filterDescripcion.getValue()))
        );
        filterRow.getCell(descripcionColumn).setComponent(filterDescripcion);

        ComboBox<Estudiante> filterEstudiante = new ComboBox<>();
        filterEstudiante.setItems(dataService.findAllEstudiante());
        filterEstudiante.setItemLabelGenerator(Estudiante::getStringNombreApellidos);
        filterEstudiante.setPlaceholder("Filtrar");
        filterEstudiante.setClearButtonVisible(true);
        filterEstudiante.setWidth("100%");
        filterEstudiante.addValueChangeListener(
                event -> gridListDataView
                        .addFilter(evaluacion -> areEstudiantesEqual(evaluacion , filterEstudiante))
        );
        filterRow.getCell(estudianteColumn).setComponent(filterEstudiante);

        ComboBox<Tarea> filterTarea = new ComboBox<>();
        filterTarea.setItems(dataService.findAllTareas());
        filterTarea.setItemLabelGenerator(Tarea::getNombre);
        filterTarea.setPlaceholder("Filter");
        filterTarea.setClearButtonVisible(true);
        filterTarea.setWidth("100%");
        filterTarea.addValueChangeListener(
                event -> gridListDataView
                        .addFilter(evaluacion -> areTareasEqual(evaluacion, filterTarea))
        );
        filterRow.getCell(tareaColumn).setComponent(filterTarea);

        ComboBox<String> statusFilter = new ComboBox<>();
        statusFilter.setItems(Arrays.asList("Pendiente", "Completada", "No Completada"));
        statusFilter.setPlaceholder("Filter");
        statusFilter.setClearButtonVisible(true);
        statusFilter.setWidth("100%");
        statusFilter.addValueChangeListener(
                event -> gridListDataView
                        .addFilter(evaluacion -> areStatusesEqual(evaluacion, statusFilter))
        );
        filterRow.getCell(statusColumn).setComponent(statusFilter);


    }

    private boolean areStatusesEqual(Evaluacion evaluacion, ComboBox<String> statusFilter) {
        String statusFilterValue = statusFilter.getValue();
        if (statusFilterValue != null) {
            return StringUtils.equals(evaluacion.getStatus(), statusFilterValue);
        }
        return true;
    }

    private boolean areEstudiantesEqual(Evaluacion evaluacion, ComboBox<Estudiante> filterEstudiante) {
        String estudianteFilterValue = filterEstudiante.getValue().getStringNombreApellidos();
        if (estudianteFilterValue != null) {
            return StringUtils.equals(evaluacion.getEstudiante().getStringNombreApellidos(), estudianteFilterValue);
        }
        return true;
    }
    private boolean areTareasEqual(Evaluacion evaluacion, ComboBox<Tarea> filterTarea) {
        String tareaFilterValue = filterTarea.getValue().getNombre();
        if (tareaFilterValue != null) {
            return StringUtils.equals(evaluacion.getTarea().getNombre(), tareaFilterValue);
        }
        return true;
    }

};
