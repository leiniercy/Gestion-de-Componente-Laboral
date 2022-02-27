/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.application.views.jefe_area.evaluacion;

import com.example.application.data.service.*;
import com.example.application.data.entity.Evaluacion;
import com.example.application.data.entity.Estudiante;
import com.example.application.data.entity.Profesor;
import com.example.application.data.entity.Tarea;
import com.example.application.data.entity.User;
import com.example.application.security.AuthenticatedUser;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.gridpro.GridPro;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.RolesAllowed;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Leinier
 */
@PageTitle("Evaluaciones")
@Route(value = "evaluaciones-view", layout = MainLayout.class)
@Uses(Icon.class)
@RolesAllowed("jefeArea")
public class EvaluacionesView extends VerticalLayout {

    private GridPro<Evaluacion> grid = new GridPro<Evaluacion>();

    EvaluacionForm form;

    private UserService userService;
    private AreaService areaService;
    private EstudianteService estudianteService;
    private ProfesorService profesorService;
    private EvaluacionService evaluacionService;
    private GrupoService grupoService;
    private TareaService tareaService;

    private AuthenticatedUser authenticatedUser;

    private Evaluacion evaluacion;

    private List<Evaluacion> listaEvaluaciones;

    private List<Estudiante> listaEstudiantes;

    private List<Tarea> listaTareas;

    private Profesor profesor_registrado;

    private List<Profesor> profesores;

    private GridListDataView<Evaluacion> gridListDataView;

    private Grid.Column<Evaluacion> notaColumn = grid.addColumn(Evaluacion::getNota).setHeader("Nota").setAutoWidth(true).setFlexGrow(0).setSortable(true);
    private Grid.Column<Evaluacion> descripcionColumn = grid.addColumn(Evaluacion::getDescripcion).setHeader("Descripción").setAutoWidth(true).setFlexGrow(0).setSortable(true);
    private Grid.Column<Evaluacion> estudianteColumn
            = grid.addColumn(evaluacion -> evaluacion.getEstudiante().getStringNombreApellidos())
                    .setComparator(evaluacion -> evaluacion.getEstudiante().getStringNombreApellidos())
                    .setHeader("Estudiante").setAutoWidth(true).setFlexGrow(0).setSortable(true);
    private Grid.Column<Evaluacion> tareaColumn
            = grid.addColumn(evaluacion -> evaluacion.getTarea().getNombre())
                    .setComparator(evaluacion -> evaluacion.getTarea().getNombre())
                    .setHeader("Tarea").setAutoWidth(true).setFlexGrow(0).setSortable(true);
    private Grid.Column<Evaluacion> statusColumn
            = grid.addEditColumn(Evaluacion::getStatus, new ComponentRenderer<>(evaluacion -> {
                Span span = new Span();
                span.setText(evaluacion.getStatus());
                span.getElement().setAttribute("theme", "badge" + evaluacion.getStatus().toLowerCase());
                return span;
            })).select((item, newValue) -> item.setStatus(newValue), Arrays.asList("Pendiente", "Completado", "No Completado"))
                    .setComparator(evaluacion -> evaluacion.getStatus()).setHeader("Estatus").setSortable(true);

    private Grid.Column<Evaluacion> editColumn = grid.addComponentColumn(evaluacion -> {
        HorizontalLayout layout = new HorizontalLayout();
        Button editButton = new Button(VaadinIcon.EDIT.create());
        editButton.addClickShortcut(Key.F2);
        editButton.addClickListener(e -> this.editEvaluacion(evaluacion));
        Button removeButton = new Button(VaadinIcon.TRASH.create());
        removeButton.addClickShortcut(Key.DELETE);
        removeButton.addClickListener(e -> this.deleteEvaluacion(evaluacion));
        layout.add(editButton, removeButton);
        return layout;
    }).setFlexGrow(0);

    private Html total;
    private HorizontalLayout toolbar;

    public EvaluacionesView(
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
        addClassNames("evaluacion-view", "flex", "flex-col", "h-full");
        setSizeFull();

        Optional<User> maybeUser = authenticatedUser.get();
        if (maybeUser.isPresent()) {

            profesores = profesorService.findAllProfesor();

            User user = maybeUser.get();
            Optional<Profesor> profesor = profesores.stream().filter(pro -> pro.getUser().getUsername().equals(user.getUsername())).findFirst();
            profesor_registrado = profesor.get();

            LlenarListas();

            if (listaEvaluaciones.size() != 0) {

                configureGrid();

                form = new EvaluacionForm(listaEstudiantes, listaTareas);
                form.setWidth("25em");
                form.addListener(EvaluacionForm.SaveEvent.class, this::saveEvaluacion);
                form.addListener(EvaluacionForm.CloseEvent.class, e -> closeEditor());

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
                        -> editEvaluacion(event.getValue()));
            } else {

                add(new H1("No hay evaluaciones disponibles"));

            }

        } else {

        }

    }

    private void LlenarListas() {

        listaEvaluaciones = evaluacionService.findAllEvaluacion();

        listaEvaluaciones = listaEvaluaciones.stream()
                .filter(eva -> eva.getEstudiante().getArea().getNombre().equals(profesor_registrado.getA().getNombre()))
                .collect(Collectors.toList());

        listaEstudiantes = estudianteService.findAllEstudiante();

        listaEstudiantes = listaEstudiantes.stream()
                .filter(est -> est.getArea().getNombre().equals(profesor_registrado.getA().getNombre()))
                .collect(Collectors.toList());

        listaTareas = tareaService.findAllTareas();

        listaTareas = listaTareas.stream()
                .filter(tarea -> tarea.getE().getArea().getNombre().equals(profesor_registrado.getA().getNombre()))
                .collect(Collectors.toList());

    }

    private void configureGrid() {

        HeaderRow headerRow = grid.appendHeaderRow();
        headerRow.getCell(notaColumn).setComponent(FiltrarNota());
        headerRow.getCell(descripcionColumn).setComponent(FiltrarDescripcion());
        headerRow.getCell(estudianteColumn).setComponent(FiltrarEstudinate());
        headerRow.getCell(tareaColumn).setComponent(FiltarTarea());
        headerRow.getCell(statusColumn).setComponent(FiltarStatus());

        gridListDataView = grid.setItems(listaEvaluaciones);
        grid.addClassNames("evaluacion-grid");
        grid.setAllRowsVisible(true);
        grid.setSizeFull();
        grid.setHeightFull();
        grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
    }

    private HorizontalLayout getToolbar() {

        addClassName("menu-items");
        total = new Html("<span>Total: <b>" + listaEvaluaciones.size() + "</b> evaluaciones</span>");

        Button addButton = new Button("Añadir Evaluación ", VaadinIcon.USER.create());
        addButton.addClickListener(click -> addEvaluacion());

        toolbar = new HorizontalLayout(total, addButton);
        toolbar.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        toolbar.setWidth("100%");
        toolbar.expand(total);
        toolbar.getStyle()
                .set("padding", "var(--lumo-space-wide-m)");

        return toolbar;
    }

    private void saveEvaluacion(EvaluacionForm.SaveEvent event) {

        List<Evaluacion> listEvaluaciones = evaluacionService.findAllEvaluacion();

        listEvaluaciones = listEvaluaciones.parallelStream()
                .filter(eva -> eva.getNota().equals(event.getEvaluacion().getNota())
                && eva.getDescripcion().equals(event.getEvaluacion().getDescripcion())
                && eva.getTarea().equals(event.getEvaluacion().getTarea())
                && eva.getEstudiante().equals(event.getEvaluacion().getEstudiante())
                && eva.getStatus().equals(event.getEvaluacion().getStatus())
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
        dialog.setText(new H3("La evaluación ya existe"));
        dialog.setConfirmText("Aceptar");
        dialog.setConfirmButtonTheme("error primary");
        dialog.addConfirmListener(new ComponentEventListener<ConfirmDialog.ConfirmEvent>() {
            @Override
            public void onComponentEvent(ConfirmDialog.ConfirmEvent event) {
                EvaluacionesView.this.refreshGrid();
            }
        });

        if (listEvaluaciones.size() != 0) {
            dialog.open();
            throw new RuntimeException("La evaluación ya existe");
        } else {
            evaluacionService.saveEvaluacion(event.getEvaluacion());
            toolbar.remove(total);
            total = new Html("<span>Total: <b>" + listaEvaluaciones.size() + "</b> evaluaciones</span>");
            toolbar.addComponentAtIndex(0, total);
            toolbar.expand(total);
            updateList();
            closeEditor();
        }

    }

    private void deleteEvaluacion(Evaluacion evaluacion) {

        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader(String.format("Eliminar %s?", evaluacion.getNota()));
        dialog.setText("¿Está seguro/a de que quiere eliminar esta evaluación?");

        dialog.setCancelText("Cancelar");
        dialog.setCancelable(true);
        dialog.addCancelListener(event -> {
            this.refreshGrid();
        });

        dialog.setConfirmText("Eliminar");
        dialog.setConfirmButtonTheme("error primary");
        dialog.addConfirmListener(event -> {

            evaluacionService.deleteEvaluacion(evaluacion);

            toolbar.remove(total);
            total = new Html("<span>Total: <b>" + listaEvaluaciones.size() + "</b> evaluaciones</span>");
            toolbar.addComponentAtIndex(0, total);
            toolbar.expand(total);

            Notification.show("Evaluación eliminada");
            this.refreshGrid();
        });

        if (evaluacion == null) {
            return;
        } else {
            dialog.open();
        }

    }

    private void refreshGrid() {

        LlenarListas();

        grid.setVisible(true);
        grid.setItems(listaEvaluaciones);

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

        LlenarListas();

        grid.setItems(listaEvaluaciones);

    }

    //Filtros
    private TextField FiltrarNota() {

        TextField filterNota = new TextField();
        filterNota.setPlaceholder("Filtrar");
        filterNota.setClearButtonVisible(true);
        filterNota.setWidth("100%");
        filterNota.setValueChangeMode(ValueChangeMode.LAZY);
        filterNota.addValueChangeListener(
                event -> gridListDataView
                        .addFilter(evaluacion -> StringUtils.containsIgnoreCase(evaluacion.getNota(), filterNota.getValue()))
        );

        return filterNota;
    }

    private TextField FiltrarDescripcion() {

        TextField filterDescripcion = new TextField();
        filterDescripcion.setPlaceholder("Filtrar");
        filterDescripcion.setClearButtonVisible(true);
        filterDescripcion.setWidth("100%");
        filterDescripcion.setValueChangeMode(ValueChangeMode.LAZY);
        filterDescripcion.addValueChangeListener(
                event -> gridListDataView
                        .addFilter(evaluacion -> StringUtils.containsIgnoreCase(evaluacion.getDescripcion(), filterDescripcion.getValue()))
        );
        return filterDescripcion;
    }

    private ComboBox<Estudiante> FiltrarEstudinate() {

        ComboBox<Estudiante> filterEstudiante = new ComboBox<>();
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
        return filterEstudiante;
    }

    private boolean areEstudiantesEqual(Evaluacion evaluacion, ComboBox<Estudiante> filterEstudiante) {
        String estudianteFilterValue = filterEstudiante.getValue().getStringNombreApellidos();
        if (estudianteFilterValue != null) {
            return StringUtils.equals(evaluacion.getEstudiante().getStringNombreApellidos(), estudianteFilterValue);
        }
        return true;
    }

    private ComboBox<Tarea> FiltarTarea() {

        ComboBox<Tarea> filterTarea = new ComboBox<>();
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
        return filterTarea;
    }

    private boolean areTareasEqual(Evaluacion evaluacion, ComboBox<Tarea> filterTarea) {
        String tareaFilterValue = filterTarea.getValue().getNombre();
        if (tareaFilterValue != null) {
            return StringUtils.equals(evaluacion.getTarea().getNombre(), tareaFilterValue);
        }
        return true;
    }

    private ComboBox<String> FiltarStatus() {

        ComboBox<String> statusFilter = new ComboBox<>();
        statusFilter.setItems(Arrays.asList("Pendiente", "Completada", "No Completada"));
        statusFilter.setPlaceholder("Filter");
        statusFilter.setClearButtonVisible(true);
        statusFilter.setWidth("100%");
        statusFilter.addValueChangeListener(
                event -> gridListDataView
                        .addFilter(evaluacion -> areStatusesEqual(evaluacion, statusFilter))
        );

        return statusFilter;
    }

    private boolean areStatusesEqual(Evaluacion evaluacion, ComboBox<String> statusFilter) {
        String statusFilterValue = statusFilter.getValue();
        if (statusFilterValue != null) {
            return StringUtils.equals(evaluacion.getStatus(), statusFilterValue);
        }
        return true;
    }

}
