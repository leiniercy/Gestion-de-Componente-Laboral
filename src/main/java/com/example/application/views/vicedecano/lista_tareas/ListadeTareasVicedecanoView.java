package com.example.application.views.vicedecano.lista_tareas;

import com.example.application.data.service.*;
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

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.RolesAllowed;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.menubar.MenuBar;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Embedded;

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
    private TextField filtrar;

    private UserService userService;
    private AreaService areaService;
    private EstudianteService estudianteService;
    private ProfesorService profesorService;
    private EvaluacionService evaluacionService;
    private GrupoService grupoService;
    private TareaService tareaService;

    public ListadeTareasVicedecanoView(
            @Autowired UserService userService,
            @Autowired AreaService areaService,
            @Autowired EstudianteService estudianteService,
            @Autowired ProfesorService profesorService,
            @Autowired EvaluacionService evaluacionService,
            @Autowired GrupoService grupoService,
            @Autowired TareaService tareaService
    ) {

        this.userService = userService;
        this.areaService = areaService;
        this.estudianteService = estudianteService;
        this.profesorService = profesorService;
        this.evaluacionService = evaluacionService;
        this.evaluacionService = evaluacionService;
        this.tareaService = tareaService;

        addClassName("listaTareas-view");
        setSizeFull();
        createGrid();

        HorizontalLayout ly = new HorizontalLayout(new Span(VaadinIcon.ACADEMY_CAP.create()), new H6("Universidad de Ciencias Informáticas"));
        ly.setAlignItems(Alignment.BASELINE);
        Footer footer = new Footer(ly);
        footer.getStyle().set("padding", "var(--lumo-space-wide-m)");
        
        add(getToolbar(), grid,footer);
    }

    private void createGrid() {
        createGridComponent();
        addColumnsToGrid();
        addFiltersToGrid();
    }

    private void createGridComponent() {
        grid = new GridPro<>();
        grid.setAllRowsVisible(true);
        grid.setSizeFull();
        grid.setHeightFull();
        grid.setWidthFull();
        grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
        grid.addThemeVariants(GridVariant.LUMO_COMPACT);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);

        List<Tarea> tareas = tareaService.findAllTareas();
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
                .setHeader("Nombre").setAutoWidth(true).setSortable(true);
    }

    private void createDescripcionColumn() {
        descripcionColumn = grid.addColumn(Tarea::getDescripcion)
                .setHeader("Descripción").setAutoWidth(true).setSortable(true);
    }

    private void createFechaInicioColumn() {
        fecha_inicioColumn
                = grid.addColumn(new LocalDateRenderer<>(tarea -> tarea.getFecha_inicio(), DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                        .setComparator(tarea -> tarea.getFecha_inicio())
                        .setHeader("Fecha de inicio").setAutoWidth(true).setFlexGrow(0).setSortable(true);
    }

    private void createFechaFinColumn() {
        fecha_finColumn
                = grid.addColumn(new LocalDateRenderer<>(tarea -> tarea.getFecha_fin(), DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                        .setComparator(tarea -> tarea.getFecha_fin())
                        .setHeader("Fecha de fin").setAutoWidth(true).setFlexGrow(0).setSortable(true);
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
        })).setComparator(tarea -> tarea.getE().getStringNombreApellidos())
                .setHeader("Estudiante").setAutoWidth(true).setSortable(true);
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
            if (fechaInicioFilter.getValue() == null) {
                gridListDataView = grid.setItems(tareaService.findAllTareas());
            } else {
                gridListDataView.addFilter(tarea -> areFechaInicioEqual(tarea, fechaInicioFilter));
            }
        });
        filterRow.getCell(fecha_inicioColumn).setComponent(fechaInicioFilter);

        fechaFinFilter.setPlaceholder("Filter");
        fechaFinFilter.setClearButtonVisible(true);
        fechaFinFilter.setWidth("100%");
        fechaFinFilter.addValueChangeListener(event -> {
            if (fechaFinFilter.getValue() == null) {
                gridListDataView = grid.setItems(tareaService.findAllTareas());
            } else {
                gridListDataView.addFilter(tarea -> areFechaFinEqual(tarea, fechaFinFilter));
            }
        });
        filterRow.getCell(fecha_finColumn).setComponent(fechaFinFilter);

        estudiantefilter.setItems(estudianteService.findAllEstudiante());
        estudiantefilter.setItemLabelGenerator(Estudiante::getStringNombreApellidos);
        estudiantefilter.setPlaceholder("Filtrar");
        estudiantefilter.setClearButtonVisible(true);
        estudiantefilter.setWidth("100%");
        estudiantefilter.addValueChangeListener(event -> {
            if (estudiantefilter.getValue() == null) {
                gridListDataView = grid.setItems(tareaService.findAllTareas());
            } else {
                gridListDataView.addFilter(tarea -> areEstudiantesEqual(tarea, estudiantefilter));
            }
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
        Html total = new Html("<span>Total: <b>" + tareaService.countTarea() + "</b> tareas</span>");

        HorizontalLayout toolbar = new HorizontalLayout(total, ButtonReporte());
        toolbar.setAlignItems(FlexComponent.Alignment.BASELINE);
        toolbar.setWidth("100%");
        toolbar.expand(total);
        toolbar.getStyle()
                .set("padding", "var(--lumo-space-wide-xl)");

        return toolbar;
    }

    private Component ButtonReporte() {

        Icon icon = new Icon(VaadinIcon.DOWNLOAD);
        icon.getStyle().set("width", "var(--lumo-icon-size-s)");
        icon.getStyle().set("height", "var(--lumo-icon-size-s)");
        icon.getStyle().set("marginRight", "var(--lumo-space-s)");

        Span span = new Span("Reporte");
        span.getStyle().set("width", "var(--lumo-size-l)");
        span.getStyle().set("heigth", "var(--lumo-size-l)");
        span.getStyle().set("font-size", "var(--lumo-font-size-m)");
        span.getStyle().set("font-weight", "500");
        span.getStyle().set("font-family", "var(--lumo-font-family)");
        span.getStyle().set("color", "var(--_lumo-button-color, var(--lumo-primary-text-color))");

        Anchor rp = new Anchor();
        rp.setHref(ReportePDF());
        rp.add(icon, span);
        rp.getStyle().set("border-radius", "var(--lumo-border-radius-l");

        MenuBar menuBar = new MenuBar();
        menuBar.addItem(rp);

        return menuBar;
    }

    private StreamResource ReportePDF() {

        //String fileName = System.currentTimeMillis() + ".pdf";
        StreamResource source = new StreamResource("ReporteTareas.pdf", () -> {

            String path = "src/main/resources/META-INF/resources/archivos/ReporteTareas.pdf";

            try {
                PdfWriter pdfWriter = new PdfWriter(path);
                PdfDocument pdfDocument = new PdfDocument(pdfWriter);
                Document document = new Document(pdfDocument);

                //Titulo
                float col = 280f;
                float columnWidth[] = {col, col};

                Table titleTable = new Table(columnWidth);

                titleTable.setBackgroundColor(new DeviceRgb(63, 169, 219)).setFontColor(Color.WHITE);

                titleTable.addCell(new Cell().add("Facultad 4")
                        .setTextAlignment(TextAlignment.CENTER)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
                        .setMarginTop(30f)
                        .setMarginBottom(30f)
                        .setFontSize(30f)
                        .setBorder(Border.NO_BORDER)
                );
                titleTable.addCell(new Cell().add("Reporte \n Componente Laboral \nUniveridad de Ciencias Informáticas")
                        .setTextAlignment(TextAlignment.RIGHT)
                        .setMarginTop(30f)
                        .setMarginBottom(30f)
                        .setMarginRight(10f)
                        .setBorder(Border.NO_BORDER)
                );

                //Informacion Personal
                float itemPersonalInfoColWidth[] = {80, 300, 100, 80};
                Table perosnalInfo = new Table(itemPersonalInfoColWidth);

                perosnalInfo.addCell(new Cell(0, 4)
                        .add("Información")
                        .setBold()
                        .setBorder(Border.NO_BORDER)
                );

                perosnalInfo.addCell(new Cell().add("Nombre:").setBorder(Border.NO_BORDER));
                perosnalInfo.addCell(new Cell().add(".....").setBorder(Border.NO_BORDER));
                perosnalInfo.addCell(new Cell().add("Categoría:").setBorder(Border.NO_BORDER));
                perosnalInfo.addCell(new Cell().add(".....").setBorder(Border.NO_BORDER));

                perosnalInfo.addCell(new Cell().add("Departamento:").setBorder(Border.NO_BORDER));
                perosnalInfo.addCell(new Cell().add(".....").setBorder(Border.NO_BORDER));
                perosnalInfo.addCell(new Cell().add("Fecha:").setBorder(Border.NO_BORDER));
                perosnalInfo.addCell(new Cell().add(".....").setBorder(Border.NO_BORDER));

                //Tabla con el Reporte
                float itemInfoColWidth[] = {140, 140, 140, 140, 140};

                Table itemInfoTable = new Table(itemInfoColWidth);

                itemInfoTable.addCell(new Cell().add("Nombre")
                        .setBackgroundColor(new DeviceRgb(63, 169, 219))
                        .setFontColor(Color.WHITE)
                );
                itemInfoTable.addCell(new Cell().add("Descripción")
                        .setBackgroundColor(new DeviceRgb(63, 169, 219))
                        .setFontColor(Color.WHITE)
                );
                itemInfoTable.addCell(new Cell().add("Inicio")
                        .setBackgroundColor(new DeviceRgb(63, 169, 219))
                        .setFontColor(Color.WHITE)
                        .setTextAlignment(TextAlignment.RIGHT)
                );
                itemInfoTable.addCell(new Cell().add("Fin")
                        .setBackgroundColor(new DeviceRgb(63, 169, 219))
                        .setFontColor(Color.WHITE)
                        .setTextAlignment(TextAlignment.RIGHT)
                );
                itemInfoTable.addCell(new Cell().add("Estudiante")
                        .setBackgroundColor(new DeviceRgb(63, 169, 219))
                        .setFontColor(Color.WHITE)
                        .setTextAlignment(TextAlignment.RIGHT)
                );

                List<Tarea> tareaList = listTareas();

                for (int i = 0; i < tareaList.size(); i++) {
                    String name = tareaList.get(i).getNombre();
                    String description = tareaList.get(i).getDescripcion();
                    String initDate = tareaList.get(i).getFecha_inicio().toString();
                    String endDate = tareaList.get(i).getFecha_fin().toString();
                    String student = tareaList.get(i).getE().getStringNombreApellidos();

                    itemInfoTable.addCell(new Cell().add(name));
                    itemInfoTable.addCell(new Cell().add(description));
                    itemInfoTable.addCell(new Cell().add(initDate).setTextAlignment(TextAlignment.RIGHT));
                    itemInfoTable.addCell(new Cell().add(endDate).setTextAlignment(TextAlignment.RIGHT));
                    itemInfoTable.addCell(new Cell().add(student).setTextAlignment(TextAlignment.RIGHT));

                }

                itemInfoTable.addCell(new Cell().add("")
                        .setBorder(Border.NO_BORDER)
                        .setBackgroundColor(new DeviceRgb(63, 169, 219))
                        .setFontColor(Color.WHITE)
                );
                itemInfoTable.addCell(new Cell().add("")
                        .setBorder(Border.NO_BORDER)
                        .setBackgroundColor(new DeviceRgb(63, 169, 219))
                        .setFontColor(Color.WHITE)
                );
                itemInfoTable.addCell(new Cell().add("")
                        .setBorder(Border.NO_BORDER)
                        .setBackgroundColor(new DeviceRgb(63, 169, 219))
                        .setFontColor(Color.WHITE)
                );
                itemInfoTable.addCell(new Cell().add("")
                        .setBorder(Border.NO_BORDER)
                        .setBackgroundColor(new DeviceRgb(63, 169, 219))
                        .setFontColor(Color.WHITE)
                );
                itemInfoTable.addCell(new Cell().add(String.format("Total: %d", tareaList.size()))
                        .setBorder(Border.NO_BORDER)
                        .setBackgroundColor(new DeviceRgb(63, 169, 219))
                        .setFontColor(Color.WHITE)
                        .setTextAlignment(TextAlignment.CENTER)
                );

                document.add(titleTable);
                document.add(new Paragraph("\n"));
                document.add(perosnalInfo);
                document.add(new Paragraph("\n"));
                document.add(itemInfoTable);
                document.add(new Paragraph("\n (Firma: ...)")
                        .setTextAlignment(TextAlignment.RIGHT)
                );

                document.close();

                File initialFile = new File(path);
                InputStream targetStream = new FileInputStream(initialFile);
                return targetStream;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        });

        return source;
    }

    //Lista de tareas del Reporte
    private List<Tarea> listTareas() {

        List<Tarea> list = tareaService.findAllTareas();

        if (filterNombre.getValue() != null) {
            list = list.stream()
                    .filter(tarea -> StringUtils.containsIgnoreCase(tarea.getNombre(), filterNombre.getValue()))
                    .collect(Collectors.toList());
        }

        if (filterDescripcion.getValue() != null) {
            list = list.stream()
                    .filter(tarea -> StringUtils.containsIgnoreCase(tarea.getDescripcion(), filterDescripcion.getValue()))
                    .collect(Collectors.toList());
        }

        if (fechaInicioFilter.getValue() != null) {
            list = list.stream()
                    .filter(tarea -> tarea.getFecha_inicio().isEqual(fechaInicioFilter.getValue()))
                    .collect(Collectors.toList());
        }

        if (fechaFinFilter.getValue() != null) {
            list = list.stream()
                    .filter(tarea -> tarea.getFecha_fin().isEqual(fechaFinFilter.getValue()))
                    .collect(Collectors.toList());
        }

        if (estudiantefilter.getValue() != null) {
            list = list.stream()
                    .filter(tarea -> tarea.getE().getSolapin().equals(estudiantefilter.getValue().getSolapin()))
                    .collect(Collectors.toList());
        }

        return list;
    }

};
