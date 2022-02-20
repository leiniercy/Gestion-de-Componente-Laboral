package com.example.application.views.estudiante;

import com.example.application.data.entity.*;
import com.example.application.data.service.*;
import com.example.application.security.AuthenticatedUser;

import com.example.application.views.MainLayout;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.gridpro.GridPro;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.Section;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.util.Arrays;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.security.RolesAllowed;
import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("Lista de Tareas ")
@Route(value = "tareas", layout = MainLayout.class)
@RolesAllowed("estudiante")
public class ListadeTareasEstudianteView extends Div {

    private GridPro<Evaluacion> grid;
    private GridListDataView<Evaluacion> gridListDataView;

    private Grid.Column<Evaluacion> notaColumn;
    private Grid.Column<Evaluacion> descripcionColumn;
    private Grid.Column<Evaluacion> tareaColumn;
    private Grid.Column<Evaluacion> statusColumn;

    private AuthenticatedUser authenticatedUser;
    private EstudianteService estudianteService;
    private EvaluacionService evaluacionService;
    private TareaService tareaService;

    private List<Evaluacion> listaEvaluaciones;

    TextField filterNota = new TextField();
    TextField filterDescripcion = new TextField();
    ComboBox<Tarea> filterTarea = new ComboBox<>();
    ComboBox<String> statusFilter = new ComboBox<>();

    public ListadeTareasEstudianteView(
            @Autowired AuthenticatedUser authenticatedUser,
            @Autowired EstudianteService estudianteService,
            @Autowired EvaluacionService evaluacionService,
            @Autowired TareaService tareaService
    ) {

        this.authenticatedUser = authenticatedUser;
        this.estudianteService = estudianteService;
        this.evaluacionService = evaluacionService;
        this.tareaService = tareaService;

        addClassName("listade-evaluaciones-tareas-view");
        setSizeFull();

        Optional<User> maybeUser = authenticatedUser.get();
        if (maybeUser.isPresent()) {

            listaEvaluaciones = evaluacionService.findAllEvaluacion();
            User user = maybeUser.get();
            String userName = user.getUsername();

            listaEvaluaciones
                    = listaEvaluaciones.stream()
                            .filter(eva -> eva.getEstudiante().getUser().getUsername().equals(userName))
                            .collect(Collectors.toList());

            if (listaEvaluaciones.size() != 0) {

                createGrid();

               
                HorizontalLayout ly = new HorizontalLayout(new Span(VaadinIcon.ACADEMY_CAP.create()), new H6("Universidad de Ciencias Informáticas"));
                ly.setAlignItems(FlexComponent.Alignment.BASELINE);
                Footer footer = new Footer(ly);
                footer.getStyle().set("padding", "var(--lumo-space-wide-m)");

                add(grid, footer);
                updateList();

            } else {
                add(new H1("No hay tareas asignadas"));
            }

        } else {
            add(new H1("Hola Mundo"));
        }
    }

    private void createGrid() {
        createGridComponent();
        addColumnsToGrid();
        addFiltersToGrid();
    }

    private void createGridComponent() {

        grid = new GridPro<>();
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_COLUMN_BORDERS);
        grid.setHeight("100%");

        gridListDataView = grid.setItems(listaEvaluaciones);
    }

    private void addColumnsToGrid() {
        createTareaColumn();
        createStatusColumn();
        createNotaColumn();
        createDescripcionColumn();
    }

    private void createNotaColumn() {
        notaColumn = grid.addColumn(Evaluacion::getNota, "nota").setHeader("Nota").setWidth("120px").setFlexGrow(0);
    }

    private void createDescripcionColumn() {
        descripcionColumn = grid.addColumn(Evaluacion::getDescripcion, "descripcion").setHeader("Descripción").setAutoWidth(true);
    }

    private void createTareaColumn() {
        tareaColumn = grid.addColumn(new ComponentRenderer<>(evaluacion -> {
            HorizontalLayout hl = new HorizontalLayout();
            hl.setAlignItems(FlexComponent.Alignment.CENTER);
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

        filterNota.setPlaceholder("Filtrar");
        filterNota.setClearButtonVisible(true);
        filterNota.setWidth("100%");
        filterNota.setValueChangeMode(ValueChangeMode.LAZY);
        filterNota.addValueChangeListener(
                event -> gridListDataView
                        .addFilter(evaluacion -> StringUtils.containsIgnoreCase(evaluacion.getNota(), filterNota.getValue()))
        );
        filterRow.getCell(notaColumn).setComponent(filterNota);

        filterDescripcion.setPlaceholder("Filtrar");
        filterDescripcion.setClearButtonVisible(true);
        filterDescripcion.setWidth("100%");
        filterDescripcion.setValueChangeMode(ValueChangeMode.LAZY);
        filterDescripcion.addValueChangeListener(
                event -> gridListDataView
                        .addFilter(evaluacion -> StringUtils.containsIgnoreCase(evaluacion.getDescripcion(), filterDescripcion.getValue()))
        );
        filterRow.getCell(descripcionColumn).setComponent(filterDescripcion);

        filterTarea.setItems(tareaService.findAllTareas());
        filterTarea.setItemLabelGenerator(Tarea::getNombre);
        filterTarea.setPlaceholder("Filter");
        filterTarea.setClearButtonVisible(true);
        filterTarea.setWidth("100%");
        filterTarea.addValueChangeListener(event -> {
            if (filterTarea.getValue() == null) {
                gridListDataView = grid.setItems(evaluacionService.findAllEvaluacion());
            } else {
                gridListDataView.addFilter(evaluacion -> areTareasEqual(evaluacion, filterTarea));
            }
        });
        filterRow.getCell(tareaColumn).setComponent(filterTarea);

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

    private boolean areTareasEqual(Evaluacion evaluacion, ComboBox<Tarea> filterTarea) {
        String tareaFilterValue = filterTarea.getValue().getNombre();
        if (tareaFilterValue != null) {
            return StringUtils.equals(evaluacion.getTarea().getNombre(), tareaFilterValue);
        }
        return true;
    }

    private void updateList() {
        grid.setItems(listaEvaluaciones);
    }

};
