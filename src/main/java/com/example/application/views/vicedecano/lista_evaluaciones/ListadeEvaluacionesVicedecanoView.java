package com.example.application.views.vicedecano.lista_evaluaciones;

import com.example.application.data.service.*;
import com.example.application.data.entity.Estudiante;
import com.example.application.data.entity.Evaluacion;
import com.example.application.data.entity.Tarea;
import com.example.application.views.MainLayout;
import com.example.application.views.vicedecano.estudiante.EstudiantesView;
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
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.contextmenu.HasMenuItems;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.gridpro.GridPro;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
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

import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.StreamResource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.reports.PrintPreviewReport;

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

    private UserService userService;
    private AreaService areaService;
    private EstudianteService estudianteService;
    private ProfesorService profesorService;
    private EvaluacionService evaluacionService;
    private GrupoService grupoService;
    private TareaService tareaService;

    TextField filterNota = new TextField();
    TextField filterDescripcion = new TextField();
    ComboBox<Estudiante> filterEstudiante = new ComboBox<>();
    ComboBox<Tarea> filterTarea = new ComboBox<>();
    ComboBox<String> statusFilter = new ComboBox<>();

    public ListadeEvaluacionesVicedecanoView(
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
        addClassName("evaluacionesList-view");
        setSizeFull();
        createGrid();

        HorizontalLayout ly = new HorizontalLayout(new Span(VaadinIcon.ACADEMY_CAP.create()), new H6("Universidad de Ciencias Informáticas"));
        ly.setAlignItems(Alignment.BASELINE);
        Footer footer = new Footer(ly);
        footer.getStyle().set("padding", "var(--lumo-space-wide-m)");

        add(getToolbar(), grid, footer);
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
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);

        List<Evaluacion> evaluaciones = evaluacionService.findAllEvaluacion();
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
        notaColumn = grid.addColumn(Evaluacion::getNota, "nota").setHeader("Nota").setWidth("120px").setFlexGrow(0).setSortable(true);
    }

    private void createDescripcionColumn() {
        descripcionColumn = grid.addColumn(Evaluacion::getDescripcion, "descripcion").setHeader("Descripción").setAutoWidth(true).setSortable(true);
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
        })).setComparator(evaluacion -> evaluacion.getEstudiante().getStringNombreApellidos())
                .setHeader("Estudiante").setAutoWidth(true).setSortable(true);
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
        })).setComparator(evaluacion -> evaluacion.getTarea().getNombre())
                .setHeader("Tarea").setAutoWidth(true).setSortable(true);
    }

    private void createStatusColumn() {
        statusColumn = grid.addEditColumn(Evaluacion::getStatus, new ComponentRenderer<>(evaluacion -> {
            Span span = new Span();
            span.setText(evaluacion.getStatus());
            span.getElement().setAttribute("theme", "badge " + evaluacion.getStatus().toLowerCase());
            return span;
        })).select((item, newValue) -> item.setStatus(newValue), Arrays.asList("Pendiente", "Completado", "No Completado"))
                .setComparator(evaluacion -> evaluacion.getStatus())
                .setHeader("Estatus").setAutoWidth(true).setSortable(true);
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

        filterEstudiante.setItems(estudianteService.findAllEstudiante());
        filterEstudiante.setItemLabelGenerator(Estudiante::getStringNombreApellidos);
        filterEstudiante.setPlaceholder("Filtrar");
        filterEstudiante.setClearButtonVisible(true);
        filterEstudiante.setWidth("100%");
        filterEstudiante.addValueChangeListener(event -> {
            if (filterEstudiante.getValue() == null) {
                gridListDataView = grid.setItems(evaluacionService.findAllEvaluacion());
            } else {
                gridListDataView.addFilter(evaluacion -> areEstudiantesEqual(evaluacion, filterEstudiante));
            }
        });
        filterRow.getCell(estudianteColumn).setComponent(filterEstudiante);

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

        StreamResource source = new StreamResource("ReporteEvaluaciones.pdf", () -> {

            String path = "src/main/resources/META-INF/resources/archivos/ReporteEvaluaciones.pdf";

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

                itemInfoTable.addCell(new Cell().add("Nota")
                        .setBackgroundColor(new DeviceRgb(63, 169, 219))
                        .setFontColor(Color.WHITE)
                );
                itemInfoTable.addCell(new Cell().add("Descripción")
                        .setBackgroundColor(new DeviceRgb(63, 169, 219))
                        .setFontColor(Color.WHITE)
                );
                itemInfoTable.addCell(new Cell().add("Estudiante")
                        .setBackgroundColor(new DeviceRgb(63, 169, 219))
                        .setFontColor(Color.WHITE)
                        .setTextAlignment(TextAlignment.RIGHT)
                );
                itemInfoTable.addCell(new Cell().add("Tarea")
                        .setBackgroundColor(new DeviceRgb(63, 169, 219))
                        .setFontColor(Color.WHITE)
                        .setTextAlignment(TextAlignment.RIGHT)
                );
                itemInfoTable.addCell(new Cell().add("Estatus")
                        .setBackgroundColor(new DeviceRgb(63, 169, 219))
                        .setFontColor(Color.WHITE)
                        .setTextAlignment(TextAlignment.RIGHT)
                );

                List<Evaluacion> evaluacionesList = lisatEvaluaciones();

                for (int i = 0; i < evaluacionesList.size(); i++) {
                    String name = evaluacionesList.get(i).getNota();
                    String description = evaluacionesList.get(i).getDescripcion();
                    String student = evaluacionesList.get(i).getEstudiante().getStringNombreApellidos();
                    String works = evaluacionesList.get(i).getTarea().getNombre();
                    String status = evaluacionesList.get(i).getStatus();

                    itemInfoTable.addCell(new Cell().add(name));
                    itemInfoTable.addCell(new Cell().add(description));
                    itemInfoTable.addCell(new Cell().add(student).setTextAlignment(TextAlignment.RIGHT));
                    itemInfoTable.addCell(new Cell().add(works).setTextAlignment(TextAlignment.RIGHT));
                    itemInfoTable.addCell(new Cell().add(status).setTextAlignment(TextAlignment.RIGHT));

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
                itemInfoTable.addCell(new Cell().add(String.format("Total: %d", evaluacionesList.size()))
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

    //Lista de evaluaciones del Reporte
    private List<Evaluacion> lisatEvaluaciones() {

        List<Evaluacion> list = evaluacionService.findAllEvaluacion();

        if (filterNota.getValue() != null) {
            list = list.stream()
                    .filter(evaluacion -> StringUtils.containsIgnoreCase(evaluacion.getNota(), filterNota.getValue()))
                    .collect(Collectors.toList());
        }

        if (filterDescripcion.getValue() != null) {
            list = list.stream()
                    .filter(evaluacion -> StringUtils.containsIgnoreCase(evaluacion.getDescripcion(), filterDescripcion.getValue()))
                    .collect(Collectors.toList());
        }

        if (filterEstudiante.getValue() != null) {
            list = list.stream()
                    .filter(evaluacion -> evaluacion.getEstudiante().getSolapin().equals(filterEstudiante.getValue().getSolapin()))
                    .collect(Collectors.toList());
        }

        if (filterTarea.getValue() != null) {
            list = list.stream()
                    .filter(evaluacion -> evaluacion.getTarea().getNombre().equals(filterTarea.getValue().getNombre()))
                    .collect(Collectors.toList());
        }

        if (statusFilter.getValue() != null) {
            list = list.stream()
                    .filter(evaluacion -> evaluacion.getStatus().equals(statusFilter.getValue()))
                    .collect(Collectors.toList());
        }

        return list;
    }

};
