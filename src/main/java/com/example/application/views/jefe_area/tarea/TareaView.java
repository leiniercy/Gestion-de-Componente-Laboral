/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.application.views.jefe_area.tarea;

import com.example.application.data.entity.Estudiante;
import com.example.application.data.entity.Profesor;
import com.example.application.data.entity.Tarea;
import com.example.application.data.entity.User;
import com.example.application.data.service.AreaService;
import com.example.application.data.service.EstudianteService;
import com.example.application.data.service.EvaluacionService;
import com.example.application.data.service.GrupoService;
import com.example.application.data.service.ProfesorService;
import com.example.application.data.service.TareaService;
import com.example.application.data.service.UserService;
import com.example.application.security.AuthenticatedUser;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import javax.annotation.security.RolesAllowed;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Leinier
 */
@org.springframework.stereotype.Component
@Scope("prototype")
@PageTitle("Tarea")
@Route(value = "tarea-view", layout = MainLayout.class)
@Uses(Icon.class)
@RolesAllowed("jefeArea")
public class TareaView extends VerticalLayout {

    Grid<Tarea> grid = new Grid<>(Tarea.class, false);

    TareaForm form;

    private UserService userService;
    private AreaService areaService;
    private EstudianteService estudianteService;
    private ProfesorService profesorService;
    private EvaluacionService evaluacionService;
    private GrupoService grupoService;
    private TareaService tareaService;

    private AuthenticatedUser authenticatedUser;

    private List<Tarea> listaTareas;

    private Profesor profesor_registrado;

    private List<Profesor> profesores;

    private List<Estudiante> listaEstudiantes;

    private GridListDataView<Tarea> gridListDataView;

    Grid.Column<Tarea> nombreColumn = grid.addColumn(Tarea::getNombre).setHeader("Nombre").setAutoWidth(true).setFlexGrow(0).setSortable(true);
    Grid.Column<Tarea> descripcionColumn = grid.addColumn(Tarea::getDescripcion).setHeader("Descripción").setAutoWidth(true).setFlexGrow(0).setSortable(true);
    Grid.Column<Tarea> fecha_inicioColumn
            = grid.addColumn(new LocalDateRenderer<>(tarea -> tarea.getFecha_inicio(), DateTimeFormatter.ofPattern("dd/MM/yyyy")))
            .setComparator(tarea -> tarea.getFecha_inicio())
            .setHeader("Fecha de inicio").setAutoWidth(true)
            .setFlexGrow(0).setSortable(true);
    Grid.Column<Tarea> fecha_finColumn
            = grid.addColumn(new LocalDateRenderer<>(tarea -> tarea.getFecha_fin(), DateTimeFormatter.ofPattern("dd/MM/yyyy")))
            .setComparator(tarea -> tarea.getFecha_fin())
            .setHeader("Fecha de fin").setAutoWidth(true)
            .setSortable(true).setFlexGrow(0);
    Grid.Column<Tarea> estudianteColumn
            = grid.addColumn(tarea -> tarea.getE().getStringNombreApellidos())
            .setComparator(tarea -> tarea.getE().getStringNombreApellidos())
            .setHeader("Estudiante").setAutoWidth(true)
            .setSortable(true).setFlexGrow(0);

    Grid.Column<Tarea> editColumn = grid.addComponentColumn(tarea -> {
        HorizontalLayout layout = new HorizontalLayout();
        Button editButton = new Button(VaadinIcon.EDIT.create());
        editButton.addClickShortcut(Key.F2);
        editButton.addClickListener(e -> this.editTarea(tarea));
        Button removeButton = new Button(VaadinIcon.TRASH.create());
        removeButton.addClickShortcut(Key.DELETE);
        removeButton.addClickListener(e -> this.deleteTarea(tarea));
        layout.add(editButton, removeButton);
        return layout;
    }).setAutoWidth(true).setFlexGrow(0);

    private Html total;
    private HorizontalLayout toolbar;

    public TareaView(
            @Autowired AuthenticatedUser authenticatedUser,
            @Autowired UserService userService,
            @Autowired AreaService areaService,
            @Autowired EstudianteService estudianteService,
            @Autowired ProfesorService profesorService,
            @Autowired EvaluacionService evaluacionService,
            @Autowired GrupoService grupoService,
            @Autowired TareaService tareaService
    ) {

        this.authenticatedUser = authenticatedUser;
        this.userService = userService;
        this.areaService = areaService;
        this.estudianteService = estudianteService;
        this.profesorService = profesorService;
        this.evaluacionService = evaluacionService;
        this.evaluacionService = evaluacionService;
        this.tareaService = tareaService;
        addClassNames("tarea-view", "flex", "flex-col", "h-full");
        setSizeFull();

        Optional<User> maybeUser = authenticatedUser.get();
        if (maybeUser.isPresent()) {

            profesores = profesorService.findAllProfesor();

            User user = maybeUser.get();
            Optional<Profesor> profesor = profesores.stream().filter(pro -> pro.getUser().getUsername().equals(user.getUsername())).findFirst();
            profesor_registrado = profesor.get();

            llenarLista();


            configureGrid();

            form = new TareaForm(listaEstudiantes);
            form.setWidth("25em");
            form.addListener(TareaForm.SaveEvent.class, this::saveTarea);
            form.addListener(TareaForm.CloseEvent.class, e -> closeEditor());

            Section section1 = new Section(grid);
            Scroller scroller = new Scroller(new Div(section1));
            scroller.setScrollDirection(Scroller.ScrollDirection.VERTICAL);
            scroller.getStyle()
                    .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                    .set("padding", "var(--lumo-space-m)");

            FlexLayout content = new FlexLayout(scroller, form);
            content.setFlexGrow(2, scroller);
            content.setFlexGrow(1, form);
            content.setFlexShrink(0, form);
            content.addClassNames("content", "gap-m");
            content.setSizeFull();

            HorizontalLayout ly = new HorizontalLayout(new Span(VaadinIcon.ACADEMY_CAP.create()), new H6("Universidad de Ciencias Informáticas"));
            ly.setAlignItems(Alignment.BASELINE);
            Footer footer = new Footer(ly);
            footer.getStyle().set("padding", "var(--lumo-space-wide-m)");

            add(getToolbar(), content, footer);
            updateList();
            closeEditor();
            grid.asSingleSelect().addValueChangeListener(event
                    -> editTarea(event.getValue()));


        } else {
            add(new H1("Hola Mundo"));
        }

    }

    private void llenarLista() {

        listaTareas = tareaService.findAllTareas();

        listaTareas = listaTareas.stream()
                .filter(tarea -> tarea.getE().getArea().getNombre().equals(profesor_registrado.getA().getNombre()))
                .collect(Collectors.toList());

        listaEstudiantes = estudianteService.findAllEstudiante();

        listaEstudiantes = listaEstudiantes.stream()
                .filter(est -> est.getArea().getNombre().equals(profesor_registrado.getA().getNombre()))
                .collect(Collectors.toList());

    }

    private void configureGrid() {

        HeaderRow headerRow = grid.appendHeaderRow();
        headerRow.getCell(nombreColumn).setComponent(FiltrarNombre());
        headerRow.getCell(descripcionColumn).setComponent(FiltrarDescripcion());
        headerRow.getCell(fecha_inicioColumn).setComponent(FiltrarFechaInicio());
        headerRow.getCell(fecha_finColumn).setComponent(FiltrarFechaFin());
        headerRow.getCell(estudianteColumn).setComponent(FiltrarEstudiante());

        gridListDataView = grid.setItems(listaTareas);
        grid.addClassNames("tarea-grid");
        grid.setAllRowsVisible(true);
        grid.setSizeFull();
        grid.setHeightFull();
        grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
    }

    private HorizontalLayout getToolbar() {

        addClassName("menu-items");
        total = new Html("<span>Total: <b>" + listaTareas.size() + "</b> tareas</span>");

        Button addButton = new Button("Añadir Tarea", VaadinIcon.USER.create());
        addButton.addClickListener(click -> addTarea());

        toolbar = new HorizontalLayout(total, addButton);
        toolbar.setAlignItems(FlexComponent.Alignment.BASELINE);
        toolbar.setWidth("100%");
        toolbar.expand(total);
        toolbar.getStyle()
                .set("padding", "var(--lumo-space-wide-m)");

        return toolbar;
    }

    private void saveTarea(TareaForm.SaveEvent event) {

        List<Tarea> listTareas = tareaService.findAllTareas();

        listTareas = listTareas.parallelStream()
                .filter(tarea -> tarea.getNombre().equals(event.getTarea().getNombre())
                        && tarea.getDescripcion().equals(event.getTarea().getDescripcion())
                        && tarea.getFecha_inicio().isEqual(event.getTarea().getFecha_inicio())
                        && tarea.getFecha_fin().isEqual(event.getTarea().getFecha_fin())
                        && tarea.getE().equals(event.getTarea().getE())
                )
                .collect(Collectors.toList());

        ConfirmDialog dialog = new ConfirmDialog();
        Icon icon = new Icon(VaadinIcon.WARNING);
        icon.setColor("red");
        icon.getStyle().set("width", "var(--lumo-icon-size-l)");
        icon.getStyle().set("height", "var(--lumo-icon-size-xl)");

        HorizontalLayout ly = new HorizontalLayout(icon, new H1("Error:"));
        ly.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
        dialog.setHeader(ly);
//        dialog.setText(new H3("La Tarea ya existe"));
        dialog.setConfirmText("Aceptar");
        dialog.setConfirmButtonTheme("error primary");
        dialog.addConfirmListener(new ComponentEventListener<ConfirmDialog.ConfirmEvent>() {
            @Override
            public void onComponentEvent(ConfirmDialog.ConfirmEvent event) {
                TareaView.this.refreshGrid();
            }
        });

        if (listTareas.size() != 0) {
            dialog.setText(new H3("La Tarea ya existe"));
            dialog.open();
        } else {
            if (event.getTarea().getFecha_fin().isBefore(event.getTarea().getFecha_inicio())) {
                dialog.setText(new H3("Intervalo de tiempo incorrecto"));
                dialog.open();
            } else {
                tareaService.saveTarea(event.getTarea());
                toolbar.remove(total);
                total = new Html("<span>Total: <b>" + listaTareas.size() + "</b> tareas</span>");
                toolbar.addComponentAtIndex(0, total);
                toolbar.expand(total);
                updateList();
                closeEditor();
            }
        }
    }

    private void deleteTarea(Tarea tarea) {

        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader(String.format("Eliminar %s", tarea.getNombre()));
        dialog.setText("¿Está seguro/a de que quiere eliminar esta tarea?");

        dialog.setCancelText("Cancelar");
        dialog.setCancelable(true);
        dialog.addCancelListener(event -> {
            this.refreshGrid();
        });

        dialog.setConfirmText("Eliminar");
        dialog.setConfirmButtonTheme("error primary");
        dialog.addConfirmListener(event -> {

            tareaService.deleteTarea(tarea);
            toolbar.remove(total);
            total = new Html("<span>Total: <b>" + listaTareas.size() + "</b> tareas</span>");
            toolbar.addComponentAtIndex(0, total);
            toolbar.expand(total);
            Notification.show("Tarea eliminada");
            this.refreshGrid();
        });

        if (tarea == null) {
            return;
        } else {
            dialog.open();
        }
    }

    private void refreshGrid() {
        llenarLista();
        grid.setVisible(true);
        grid.setItems(listaTareas);
    }

    public void editTarea(Tarea tarea) {
        if (tarea == null) {
            closeEditor();
        } else {
            form.setTarea(tarea);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    void addTarea() {
        grid.asSingleSelect().clear();
        editTarea(new Tarea());
    }

    private void closeEditor() {
        form.setTarea(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void updateList() {
        llenarLista();
        grid.setItems(listaTareas);
    }

    // Filtros
    private TextField FiltrarNombre() {

        TextField filterNombre = new TextField();
        filterNombre.setPlaceholder("Filtrar");
        filterNombre.setClearButtonVisible(true);
        filterNombre.setWidth("100%");
        filterNombre.setValueChangeMode(ValueChangeMode.EAGER);
        filterNombre.addValueChangeListener(
                event -> gridListDataView
                        .addFilter(tarea -> StringUtils.containsIgnoreCase(tarea.getNombre(), filterNombre.getValue()))
        );

        return filterNombre;
    }

    private TextField FiltrarDescripcion() {
        TextField filterDescripcion = new TextField();
        filterDescripcion.setPlaceholder("Filtrar");
        filterDescripcion.setClearButtonVisible(true);
        filterDescripcion.setWidth("100%");
        filterDescripcion.setValueChangeMode(ValueChangeMode.EAGER);
        filterDescripcion.addValueChangeListener(
                event -> gridListDataView
                        .addFilter(tarea -> StringUtils.containsIgnoreCase(tarea.getDescripcion(), filterDescripcion.getValue()))
        );
        return filterDescripcion;
    }

    private ComboBox<Estudiante> FiltrarEstudiante() {
        ComboBox<Estudiante> estudiantefilter = new ComboBox<>();
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
        return estudiantefilter;
    }

    private boolean areEstudiantesEqual(Tarea tarea, ComboBox<Estudiante> filterEstudiante) {
        String estudianteFilterValue = filterEstudiante.getValue().getStringNombreApellidos();
        if (estudianteFilterValue != null) {
            return StringUtils.equals(tarea.getE().getStringNombreApellidos(), estudianteFilterValue);
        }
        return true;
    }

    private DatePicker FiltrarFechaInicio() {
        DatePicker dateFilter = new DatePicker();
        dateFilter.setPlaceholder("Filter");
        dateFilter.setClearButtonVisible(true);
        dateFilter.setWidth("100%");
        dateFilter.addValueChangeListener(event -> {
            if (dateFilter.getValue() == null) {
                gridListDataView = grid.setItems(tareaService.findAllTareas());
            } else {
                gridListDataView.addFilter(tarea -> areFechaInicioEqual(tarea, dateFilter));
            }
        });
        return dateFilter;
    }

    private boolean areFechaInicioEqual(Tarea tarea, DatePicker dateFilter) {
        String dateFilterValue = dateFilter.getValue().toString();
        String tareaDate = tarea.getFecha_inicio().toString();
        if (dateFilterValue != null) {
            return StringUtils.equals(dateFilterValue, tareaDate);
        }
        return true;
    }

    private DatePicker FiltrarFechaFin() {
        DatePicker dateFilter = new DatePicker();
        dateFilter.setPlaceholder("Filter");
        dateFilter.setClearButtonVisible(true);
        dateFilter.setWidth("100%");
        dateFilter.addValueChangeListener(event -> {
            if (dateFilter.getValue() == null) {
                gridListDataView = grid.setItems(tareaService.findAllTareas());
            } else {
                gridListDataView.addFilter(tarea -> areFechaFinEqual(tarea, dateFilter));
            }
        });
        return dateFilter;
    }

    private boolean areFechaFinEqual(Tarea tarea, DatePicker dateFilter) {
        String dateFilterValue = dateFilter.getValue().toString();
        String date = tarea.getFecha_fin().toString();
        if (dateFilterValue != null) {
            return StringUtils.equals(dateFilterValue, date);
        }
        return true;
    }

}
