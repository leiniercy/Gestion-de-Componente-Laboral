package com.example.application.views.vicedecano.lista_tareas;

import com.example.application.data.service.DataService;
import com.example.application.data.entity.Estudiante;
import com.example.application.data.entity.Tarea;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.gridpro.GridPro;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.reports.PrintPreviewReport;

import javax.annotation.security.RolesAllowed;
import java.time.format.DateTimeFormatter;
import java.util.List;

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


    private TextField filterNombre = new TextField();
    private TextField filterDescripcion = new TextField();
    private DatePicker fechaInicioFilter = new DatePicker();
    private DatePicker fechaFinFilter = new DatePicker();
    private ComboBox<Estudiante> estudiantefilter = new ComboBox<>();

    private DataService dataService;

    public ListadeTareasVicedecanoView(@Autowired DataService dataService) {
        this.dataService = dataService;
        addClassName("listaTareas-view");
        setSizeFull();
        createGrid();
        add(getToolbar(), grid);
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
                grid.addColumn(new LocalDateRenderer<>(tarea -> tarea.getFecha_inicio(), DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                        .setComparator(tarea -> tarea.getFecha_inicio())
                        .setHeader("Fecha de inicio").setAutoWidth(true).setFlexGrow(0);
    }

    private void createFechaFinColumn() {
        fecha_finColumn =
                grid.addColumn(new LocalDateRenderer<>(tarea -> tarea.getFecha_fin(), DateTimeFormatter.ofPattern("dd/MM/yyyy")))
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


        filterNombre.setPlaceholder("Filtrar");
        filterNombre.setClearButtonVisible(true);
        filterNombre.setWidth("100%");
        filterNombre.setValueChangeMode(ValueChangeMode.EAGER);
        filterNombre.addValueChangeListener(
                event -> gridListDataView
                        .addFilter(tarea -> StringUtils.containsIgnoreCase(tarea.getNombre(), filterNombre.getValue()))
        );
        filterRow.getCell(nombreColumn).setComponent(filterNombre);


        filterDescripcion.setPlaceholder("Filtrar");
        filterDescripcion.setClearButtonVisible(true);
        filterDescripcion.setWidth("100%");
        filterDescripcion.setValueChangeMode(ValueChangeMode.EAGER);
        filterDescripcion.addValueChangeListener(
                event -> gridListDataView
                        .addFilter(tarea -> StringUtils.containsIgnoreCase(tarea.getDescripcion(), filterDescripcion.getValue()))
        );
        filterRow.getCell(descripcionColumn).setComponent(filterDescripcion);


        fechaInicioFilter.setPlaceholder("Filter");
        fechaInicioFilter.setClearButtonVisible(true);
        fechaInicioFilter.setWidth("100%");
        fechaInicioFilter.addValueChangeListener(event -> {
            if (fechaInicioFilter.getValue() == null)
                gridListDataView = grid.setItems(dataService.findAllTareas());
            else gridListDataView.addFilter(tarea -> areFechaInicioEqual(tarea, fechaInicioFilter));
        });
        filterRow.getCell(fecha_inicioColumn).setComponent(fechaInicioFilter);


        fechaFinFilter.setPlaceholder("Filter");
        fechaFinFilter.setClearButtonVisible(true);
        fechaFinFilter.setWidth("100%");
        fechaFinFilter.addValueChangeListener(event -> {
            if (fechaFinFilter.getValue() == null)
                gridListDataView = grid.setItems(dataService.findAllTareas());
            else gridListDataView.addFilter(tarea -> areFechaFinEqual(tarea, fechaFinFilter));
        });
        filterRow.getCell(fecha_finColumn).setComponent(fechaFinFilter);


        estudiantefilter.setItems(dataService.findAllEstudiante());
        estudiantefilter.setItemLabelGenerator(Estudiante::getStringNombreApellidos);
        estudiantefilter.setPlaceholder("Filtrar");
        estudiantefilter.setClearButtonVisible(true);
        estudiantefilter.setWidth("100%");
        estudiantefilter.addValueChangeListener(event -> {
            if (estudiantefilter.getValue() == null)
                gridListDataView = grid.setItems(dataService.findAllTareas());
            else gridListDataView.addFilter(tarea -> areEstudiantesEqual(tarea, estudiantefilter));
        });
        filterRow.getCell(estudianteColumn).setComponent(estudiantefilter);

    }

    private boolean areEstudiantesEqual(Tarea tarea, ComboBox<Estudiante> filterEstudiante) {
        String estudianteFilterValue = filterEstudiante.getValue().getStringNombreApellidos();
        if (estudianteFilterValue != null) {
            return StringUtils.equals(tarea.getE().getStringNombreApellidos(), estudianteFilterValue);
        }
        return true;
    }

    private boolean areFechaInicioEqual(Tarea tarea, DatePicker dateFilter) {
        String dateFilterValue = dateFilter.getValue().toString();
        String tareaDate = tarea.getFecha_inicio().toString();
        if (dateFilterValue != null) {
            return StringUtils.equals(dateFilterValue, tareaDate);
        }
        return true;
    }

    private boolean areFechaFinEqual(Tarea tarea, DatePicker dateFilter) {
        String dateFilterValue = dateFilter.getValue().toString();
        String date = tarea.getFecha_fin().toString();
        if (dateFilterValue != null) {
            return StringUtils.equals(dateFilterValue, date);
        }
        return true;
    }

    private HorizontalLayout getToolbar() {

        addClassName("menu-items");
        Html total = new Html("<span>Total: <b>" + dataService.countTarea() + "</b> tareas</span>");

        HorizontalLayout toolbar = new HorizontalLayout(total, CrearReporte());
        toolbar.setAlignItems(FlexComponent.Alignment.CENTER);
        toolbar.setWidth("99%");
        toolbar.setFlexGrow(1, total);


        return toolbar;
    }


    private Component CrearReporte() {

        PrintPreviewReport report
                = new PrintPreviewReport<>(Tarea.class, "nombre", "descripcion", "fecha_inicio", "fecha_fin", "e");
        report.setItems(dataService.findAllTareas());
        report.getReportBuilder().setTitle("Tareas");
        StreamResource pdf = report.getStreamResource("tareas.pdf", dataService::findAllTareas, PrintPreviewReport.Format.PDF);

        Icon icon = new Icon(VaadinIcon.DOWNLOAD);
        icon.getStyle().set("width", "var(--lumo-icon-size-s)");
        icon.getStyle().set("height", "var(--lumo-icon-size-s)");
        icon.getStyle().set("marginRight", "var(--lumo-space-s)");

        Anchor rp = new Anchor();
        rp.setHref(pdf);
        rp.add(icon, new Span("Reporte"));


        return rp;
    }

};
