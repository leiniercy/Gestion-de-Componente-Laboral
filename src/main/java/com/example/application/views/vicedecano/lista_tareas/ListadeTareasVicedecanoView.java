package com.example.application.views.vicedecano.lista_tareas;

import com.example.application.data.DataService;
import com.example.application.data.entity.Estudiante;
import com.example.application.data.entity.Tarea;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.gridpro.GridPro;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import javax.annotation.security.RolesAllowed;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("Tareas")
@Route(value = "list-tareas", layout = MainLayout.class)
@RolesAllowed("vicedecano")
public class ListadeTareasVicedecanoView extends Div {

    private GridPro<Tarea> grid;
    private GridListDataView<Tarea> gridListDataView;

    private Grid.Column<Tarea> nombreColumn;
    private Grid.Column<Tarea> descripcionColumn;
    private Grid.Column<Tarea> fecha_inicioColumn;
    private Grid.Column<Tarea> fecha_finColumn;
    private Grid.Column<Tarea> estudianteColumn;


    private DataService dataService;

    public ListadeTareasVicedecanoView(@Autowired DataService dataService) {
        this.dataService = dataService;
        addClassName("listaTareas-view");
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
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_COLUMN_BORDERS);
        grid.setHeight("100%");

        List<Tarea> tareas = dataService.findAllTareas();
        gridListDataView = grid.setItems(tareas);
    }

    private void addColumnsToGrid() {
        createNombreColumn();
        createDescripcionColumn();
        createFechaInicioColumn();
        createFechaFinColumn();
        createEstudianteColumn();

    }

    private void createNombreColumn() {
        nombreColumn = grid.addColumn(Tarea::getNombre)
                .setHeader("Nombre").setAutoWidth(true);
    }

    private void createDescripcionColumn() {
        descripcionColumn = grid.addColumn(Tarea::getDescripcion)
                .setHeader("Descripción").setAutoWidth(true);
    }

    private void createFechaInicioColumn() {
        fecha_inicioColumn =
                grid.addColumn(Tarea::getFecha_inicio)
                        .setComparator(tarea -> tarea.getFecha_inicio())
                        .setHeader("Fecha de inicio").setAutoWidth(true).setFlexGrow(0);
    }

    private void createFechaFinColumn() {
        fecha_finColumn =
                 grid.addColumn(Tarea::getFecha_fin)
                .setComparator(tarea -> tarea.getFecha_fin())
                .setHeader("Fecha de fin").setAutoWidth(true).setFlexGrow(0);
    }

    private void createEstudianteColumn() {
        estudianteColumn = grid.addColumn(new ComponentRenderer<>(tarea -> {
            HorizontalLayout hl = new HorizontalLayout();
            hl.setAlignItems(Alignment.CENTER);
            Span span = new Span();
            span.setClassName("nombre-apellidos");
            span.setText(tarea.getE().getStringNombreApellidos());
            hl.add(span);
            return hl;
        })).setComparator(tarea -> tarea.getE().getStringNombreApellidos()).setHeader("Estudiante").setAutoWidth(true);
    }


    private void addFiltersToGrid() {
        HeaderRow filterRow = grid.appendHeaderRow();

        TextField filterNombre = new TextField();
        filterNombre.setPlaceholder("Filtrar");
        filterNombre.setClearButtonVisible(true);
        filterNombre.setWidth("100%");
        filterNombre.setValueChangeMode(ValueChangeMode.EAGER);
        filterNombre.addValueChangeListener(
                event -> gridListDataView
                        .addFilter(tarea -> StringUtils.containsIgnoreCase(tarea.getNombre(), filterNombre.getValue()))
        );
        filterRow.getCell(nombreColumn).setComponent(filterNombre);

        TextField filterDescripcion = new TextField();
        filterDescripcion.setPlaceholder("Filtrar");
        filterDescripcion.setClearButtonVisible(true);
        filterDescripcion.setWidth("100%");
        filterDescripcion.setValueChangeMode(ValueChangeMode.EAGER);
        filterDescripcion.addValueChangeListener(
                event -> gridListDataView
                        .addFilter(tarea -> StringUtils.containsIgnoreCase(tarea.getDescripcion(), filterDescripcion.getValue()))
        );
        filterRow.getCell(descripcionColumn).setComponent(filterDescripcion);

        DatePicker fechaInicioFilter = new DatePicker();
        fechaInicioFilter.setPlaceholder("Filter");
        fechaInicioFilter.setClearButtonVisible(true);
        fechaInicioFilter.setWidth("100%");
        fechaInicioFilter.addValueChangeListener(e->{
            grid.setItems(dataService.searchTareaByFecha( fechaInicioFilter.getValue() ) );
        });
        filterRow.getCell(fecha_inicioColumn).setComponent(fechaInicioFilter);

        DatePicker fechaFinFilter = new DatePicker();
        fechaFinFilter.setPlaceholder("Filter");
        fechaFinFilter.setClearButtonVisible(true);
        fechaFinFilter.setWidth("100%");
        fechaFinFilter.addValueChangeListener(e -> {
            grid.setItems(dataService.searchTareaByFecha( fechaFinFilter.getValue() ) );
        });
        filterRow.getCell(fecha_finColumn).setComponent(fechaFinFilter);

        ComboBox<Estudiante> estudiantefilter = new ComboBox<>();
        estudiantefilter.setItems(dataService.findAllEstudiante());
        estudiantefilter.setItemLabelGenerator(Estudiante::getStringNombreApellidos);
        estudiantefilter.setPlaceholder("Filtrar");
        estudiantefilter.setClearButtonVisible(true);
        estudiantefilter.setWidth("100%");
        estudiantefilter.addValueChangeListener(
                event -> gridListDataView
                        .addFilter(tarea ->  areEstudiantesEqual( tarea, estudiantefilter))
        );
        filterRow.getCell(estudianteColumn).setComponent(estudiantefilter);


    }

    private boolean areEstudiantesEqual(Tarea tarea, ComboBox<Estudiante> filterEstudiante) {
        String estudianteFilterValue = filterEstudiante.getValue().getStringNombreApellidos();
        if (estudianteFilterValue != null) {
            return StringUtils.equals(tarea.getE().getStringNombreApellidos(), estudianteFilterValue);
        }
        return true;
    }
};
