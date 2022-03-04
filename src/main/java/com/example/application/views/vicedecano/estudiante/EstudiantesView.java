/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.application.views.vicedecano.estudiante;

import com.example.application.data.entity.*;
import com.example.application.data.service.*;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.security.RolesAllowed;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

/**
 * @author Leinier
 */
@org.springframework.stereotype.Component
@Scope("prototype")
@PageTitle("Estudiante")
@Route(value = "estudiantes-view", layout = MainLayout.class)
@RolesAllowed("vicedecano")
public class EstudiantesView extends VerticalLayout {

    Grid<Estudiante> grid = new Grid<>(Estudiante.class, false);

    EstudianteForm form;

    private UserService userService;
    private AreaService areaService;
    private EstudianteService estudianteService;
    private ProfesorService profesorService;
    private EvaluacionService evaluacionService;
    private GrupoService grupoService;
    private TareaService tareaService;

    private GridListDataView<Estudiante> gridListDataView;

    private Grid.Column<Estudiante> nombreColumn = grid.addColumn(Estudiante::getNombre).setHeader("Nombre").setAutoWidth(true).setFlexGrow(0).setSortable(true);
    private Grid.Column<Estudiante> apellidosColumn = grid.addColumn(Estudiante::getApellidos).setHeader("Apellidos").setAutoWidth(true).setFlexGrow(0).setSortable(true);
    private Grid.Column<Estudiante> userColumn = grid.addColumn(estudiante -> estudiante.getUser().getName()).setHeader("Usuario").setAutoWidth(true).setFlexGrow(0).setSortable(true);
    private Grid.Column<Estudiante> emailColumn = grid.addColumn(Estudiante::getEmail).setHeader("Correo").setAutoWidth(true).setFlexGrow(0).setSortable(true);
    private Grid.Column<Estudiante> solapinColumn = grid.addColumn(Estudiante::getSolapin).setHeader("Solapín").setAutoWidth(true).setFlexGrow(0).setSortable(true);
    private Grid.Column<Estudiante> anno_repitenciaColumn = grid.addColumn(Estudiante::getAnno_repitencia).setHeader("Año de repitencia").setAutoWidth(true).setFlexGrow(0).setSortable(true);
    private Grid.Column<Estudiante> cantidad_asignaturasColumn = grid.addColumn(Estudiante::getCantidad_asignaturas).setHeader("Cantidad de asignaturas").setAutoWidth(true).setFlexGrow(0).setSortable(true);
    private Grid.Column<Estudiante> areaColumn = grid.addColumn(estudiante -> estudiante.getArea().getNombre()).setHeader("Área").setAutoWidth(true).setFlexGrow(0).setSortable(true);
    private Grid.Column<Estudiante> grupoColumn = grid.addColumn(estudiante -> estudiante.getGrupo().getNumero()).setHeader("Grupo").setAutoWidth(true).setFlexGrow(0).setSortable(true);
    private Grid.Column<Estudiante> editColumn = grid.addComponentColumn(estudiante -> {
        HorizontalLayout layout = new HorizontalLayout();
        Button editButton = new Button(VaadinIcon.EDIT.create());
        editButton.addClickShortcut(Key.F2);
        editButton.addClickListener(event -> this.editEstudiante(estudiante));
        Button removeButton = new Button(VaadinIcon.TRASH.create());
        removeButton.addClickShortcut(Key.DELETE);
        removeButton.addClickListener(event -> this.deleteEstudiante(estudiante));
        layout.add(editButton, removeButton);
        return layout;
    }).setFlexGrow(0);

    private HorizontalLayout toolbar;
    private Html total;

    public EstudiantesView(
            @Autowired UserService userService,
            @Autowired AreaService areaService,
            @Autowired EstudianteService estudianteService,
            @Autowired ProfesorService profesorService,
            @Autowired EvaluacionService evaluacionService,
            @Autowired GrupoService grupoService,
            @Autowired TareaService tareaService
    ) {

        this.userService = userService;
        this.grupoService = grupoService;
        this.areaService = areaService;
        this.estudianteService = estudianteService;
        this.profesorService = profesorService;
        this.evaluacionService = evaluacionService;
        this.evaluacionService = evaluacionService;
        this.tareaService = tareaService;
        addClassNames("list-est-view", "flex", "flex-col", "h-full");
        setSizeFull();
        configureGrid();

        form = new EstudianteForm(userService.findAllUser(), areaService.findAllArea(), grupoService.findAllGrupo());
        form.setWidth("25em");
        form.addListener(EstudianteForm.SaveEvent.class, this::saveEstudiante);
        form.addListener(EstudianteForm.CloseEvent.class, e -> closeEditor());

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
                -> editEstudiante(event.getValue()));
    }

    private void configureGrid() {

        HeaderRow headerRow = grid.appendHeaderRow();
        headerRow.getCell(nombreColumn).setComponent(FiltrarNombre());
        headerRow.getCell(apellidosColumn).setComponent(FiltrarApellidos());
        headerRow.getCell(emailColumn).setComponent(FiltrarEmail());
        headerRow.getCell(userColumn).setComponent(FiltrarUser());
        headerRow.getCell(solapinColumn).setComponent(FiltrarSolapin());
        headerRow.getCell(anno_repitenciaColumn).setComponent(FiltrarAnno_repitencia());
        headerRow.getCell(cantidad_asignaturasColumn).setComponent(FiltrarCantidad_asignaturas());
        headerRow.getCell(areaColumn).setComponent(FiltrarArea());
        headerRow.getCell(grupoColumn).setComponent(FiltrarGrupo());

        gridListDataView = grid.setItems(estudianteService.findAllEstudiante());
        grid.addClassNames("estudiante-grid");
        grid.setAllRowsVisible(true);
        grid.setSizeFull();
        grid.setHeightFull();
        grid.setWidthFull();
        grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
        // grid.addThemeVariants(GridVariant.LUMO_COMPACT);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
        //grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_COLUMN_BORDERS);
    }

    private HorizontalLayout getToolbar() {

        addClassName("menu-items");
        total = new Html("<span>Total: <b>" + estudianteService.countEstudiante() + "</b> estudiantes</span>");

        Button menuButton = new Button("Mostar/Ocultar Columnas");
        //menuButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        ColumnToggleContextMenu columnToggleContextMenu = new ColumnToggleContextMenu(
                menuButton);
        columnToggleContextMenu.addColumnToggleItem("Nombre", nombreColumn);
        columnToggleContextMenu.addColumnToggleItem("Apellidos", apellidosColumn);
        columnToggleContextMenu.addColumnToggleItem("Usuario", userColumn);
        columnToggleContextMenu.addColumnToggleItem("Correo", emailColumn);
        columnToggleContextMenu.addColumnToggleItem("Solapín", solapinColumn);
        columnToggleContextMenu.addColumnToggleItem("Año de repitencia", anno_repitenciaColumn);
        columnToggleContextMenu.addColumnToggleItem("Cantidad de asignaturas", cantidad_asignaturasColumn);
        columnToggleContextMenu.addColumnToggleItem("Área", areaColumn);
        columnToggleContextMenu.addColumnToggleItem("Grupo", grupoColumn);

        Button addButton = new Button("Añadir Estudiante", VaadinIcon.USER.create());
        //addButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        addButton.addClickListener(click -> addEstudiante());

        toolbar = new HorizontalLayout(total, menuButton, addButton);
        toolbar.setAlignItems(FlexComponent.Alignment.BASELINE);
        toolbar.setWidth("100%");
        toolbar.expand(total);
        toolbar.getStyle()
                .set("padding", "var(--lumo-space-wide-m)");

        return toolbar;
    }

    private static class ColumnToggleContextMenu extends ContextMenu {

        public ColumnToggleContextMenu(Component target) {
            super(target);
            setOpenOnClick(true);
        }

        void addColumnToggleItem(String label, Grid.Column<Estudiante> column) {
            MenuItem menuItem = this.addItem(label, e -> {
                column.setVisible(e.getSource().isChecked());
            });
            menuItem.setCheckable(true);
            menuItem.setChecked(column.isVisible());
        }
    }

    private void saveEstudiante(EstudianteForm.SaveEvent event) {

        List<Profesor> listProfesores = profesorService.findAllProfesor();
        List<Estudiante> listEstudiantes = estudianteService.findAllEstudiante();

        listEstudiantes = listEstudiantes.parallelStream()
                .filter(est -> est.getNombre().equals(event.getEstudiante().getNombre())
                && est.getApellidos().equals(event.getEstudiante().getApellidos())
                && est.getUser().equals(event.getEstudiante().getUser())
                && est.getEmail().equals(event.getEstudiante().getEmail())
                && est.getSolapin().equals(event.getEstudiante().getSolapin())
                && est.getAnno_repitencia().equals(event.getEstudiante().getAnno_repitencia())
                && est.getCantidad_asignaturas().equals(event.getEstudiante().getCantidad_asignaturas())
                && est.getArea().equals(event.getEstudiante().getArea())
                && est.getGrupo().equals(event.getEstudiante().getGrupo())
                )
                .collect(Collectors.toList());

        listProfesores = listProfesores.parallelStream()
                .filter(profe -> profe.getUser().equals(event.getEstudiante().getUser()))
                .collect(Collectors.toList());

        ConfirmDialog dialog = new ConfirmDialog();
        Icon icon = new Icon(VaadinIcon.WARNING);
        icon.setColor("red");
        icon.getStyle().set("width", "var(--lumo-icon-size-l)");
        icon.getStyle().set("height", "var(--lumo-icon-size-xl)");

        HorizontalLayout ly = new HorizontalLayout(icon, new H1("Error:"));
        ly.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
        dialog.setHeader(ly);
        dialog.setConfirmText("Aceptar");
        dialog.setConfirmButtonTheme("error primary");
        dialog.addConfirmListener(new ComponentEventListener<ConfirmDialog.ConfirmEvent>() {
            @Override
            public void onComponentEvent(ConfirmDialog.ConfirmEvent event) {
                EstudiantesView.this.refreshGrid();
            }
        });

        if (listProfesores.size() == 0) {
            dialog.setText(new H3("El usuario pertenece a un jefe de área"));
            dialog.open();
        } else if (listEstudiantes.size() != 0) {
            dialog.setText(new H3("El estudiante ya existe"));
            dialog.open();
        } else {
            estudianteService.saveEstudiante(event.getEstudiante());
            toolbar.remove(total);
            total = new Html("<span>Total: <b>" + estudianteService.countEstudiante() + "</b> estudiantes</span>");
            toolbar.addComponentAtIndex(0, total);
            toolbar.expand(total);
            updateList();
            closeEditor();
        }
    }

    private void deleteEstudiante(Estudiante estudiante) {

        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader(String.format("Eliminar %s", estudiante.getStringNombreApellidos()));
        dialog.setText("¿Está seguro/a de que quiere eliminar a este estudiante?");

        dialog.setCancelText("Cancelar");
        dialog.setCancelable(true);
        dialog.addCancelListener(event -> {
            this.refreshGrid();
        });

        dialog.setConfirmText("Eliminar");
        dialog.setConfirmButtonTheme("error primary");
        dialog.addConfirmListener(event -> {

            estudianteService.deleteEstudiante(estudiante);

            toolbar.remove(total);
            total = new Html("<span>Total: <b>" + estudianteService.countEstudiante() + "</b> estudiantes</span>");
            toolbar.addComponentAtIndex(0, total);
            toolbar.expand(total);

            Notification.show("Estudiante eliminado");
            this.refreshGrid();
        });

        if (estudiante == null) {
            return;
        } else {
            dialog.open();
        }

    }

    private void refreshGrid() {
        grid.setVisible(true);
        grid.setItems(estudianteService.findAllEstudiante());
    }

    public void editEstudiante(Estudiante estudiante) {
        if (estudiante == null) {
            closeEditor();
        } else {
            form.setEstudiante(estudiante);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    void addEstudiante() {
        grid.asSingleSelect().clear();
        editEstudiante(new Estudiante());
    }

    private void closeEditor() {
        form.setEstudiante(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(estudianteService.findAllEstudiante());
    }

    // Filtros
    private TextField FiltrarNombre() {

        TextField nombreFilter = new TextField();
        nombreFilter.setPlaceholder("Filtrar");
        nombreFilter.setPrefixComponent(VaadinIcon.SEARCH.create());
        nombreFilter.setClearButtonVisible(true);
        nombreFilter.setWidth("100%");
        nombreFilter.setValueChangeMode(ValueChangeMode.LAZY);
        nombreFilter.addValueChangeListener(
                event -> gridListDataView
                        .addFilter(estudiante -> StringUtils.containsIgnoreCase(estudiante.getNombre(), nombreFilter.getValue()))
        );

        return nombreFilter;
    }

    private TextField FiltrarApellidos() {
        TextField apellidosFilter = new TextField();
        apellidosFilter.setPlaceholder("Filtrar");
        apellidosFilter.setPrefixComponent(VaadinIcon.SEARCH.create());
        apellidosFilter.setClearButtonVisible(true);
        apellidosFilter.setWidth("100%");
        apellidosFilter.setValueChangeMode(ValueChangeMode.LAZY);
        apellidosFilter.addValueChangeListener(
                event -> gridListDataView
                        .addFilter(estudiante -> StringUtils.containsIgnoreCase(estudiante.getApellidos(), apellidosFilter.getValue()))
        );
        return apellidosFilter;
    }

    private TextField FiltrarEmail() {
        TextField emailFilter = new TextField();
        emailFilter.setPlaceholder("Filtrar");
        emailFilter.setPrefixComponent(VaadinIcon.SEARCH.create());
        emailFilter.setClearButtonVisible(true);
        emailFilter.setWidth("100%");
        emailFilter.setValueChangeMode(ValueChangeMode.LAZY);
        emailFilter.addValueChangeListener(
                event -> gridListDataView
                        .addFilter(estudiante -> StringUtils.containsIgnoreCase(estudiante.getEmail(), emailFilter.getValue()))
        );
        return emailFilter;
    }

    private TextField FiltrarSolapin() {
        TextField solapinFilter = new TextField();
        solapinFilter.setPlaceholder("Filtrar");
        solapinFilter.setPrefixComponent(VaadinIcon.SEARCH.create());
        solapinFilter.setClearButtonVisible(true);
        solapinFilter.setWidth("100%");
        solapinFilter.setValueChangeMode(ValueChangeMode.LAZY);
        solapinFilter.addValueChangeListener(
                event -> gridListDataView
                        .addFilter(estudiante -> StringUtils.containsIgnoreCase(estudiante.getSolapin(), solapinFilter.getValue()))
        );
        return solapinFilter;
    }

    private IntegerField FiltrarAnno_repitencia() {
        IntegerField anno_repitenciaFilter = new IntegerField();
        anno_repitenciaFilter.setPlaceholder("Filtrar");
        anno_repitenciaFilter.setPrefixComponent(VaadinIcon.SEARCH.create());
        anno_repitenciaFilter.setClearButtonVisible(true);
        anno_repitenciaFilter.setWidth("100%");
        anno_repitenciaFilter.setValueChangeMode(ValueChangeMode.LAZY);
        anno_repitenciaFilter.addValueChangeListener(
                event -> gridListDataView
                        .addFilter(estudiante -> StringUtils.containsIgnoreCase(Integer.toString(estudiante.getAnno_repitencia()), Integer.toString(anno_repitenciaFilter.getValue())))
        );
        return anno_repitenciaFilter;
    }

    private IntegerField FiltrarCantidad_asignaturas() {
        IntegerField cantidad_asignaturasFilter = new IntegerField();
        cantidad_asignaturasFilter.setPlaceholder("Filtrar");
        cantidad_asignaturasFilter.setPrefixComponent(VaadinIcon.SEARCH.create());
        cantidad_asignaturasFilter.setClearButtonVisible(true);
        cantidad_asignaturasFilter.setWidth("100%");
        cantidad_asignaturasFilter.setValueChangeMode(ValueChangeMode.LAZY);
        cantidad_asignaturasFilter.addValueChangeListener(
                event -> gridListDataView
                        .addFilter(estudiante -> StringUtils.containsIgnoreCase(Integer.toString(estudiante.getCantidad_asignaturas()), Integer.toString(cantidad_asignaturasFilter.getValue())))
        );
        return cantidad_asignaturasFilter;
    }

    private ComboBox<Area> FiltrarArea() {
        ComboBox<Area> areaFilter = new ComboBox<>();
        areaFilter.setItems(areaService.findAllArea());
        areaFilter.setItemLabelGenerator(Area::getNombre);
        areaFilter.setPlaceholder("Filtrar");
        areaFilter.setClearButtonVisible(true);
        areaFilter.setWidth("100%");
        areaFilter.addValueChangeListener(event -> {
            if (areaFilter.getValue() == null) {
                gridListDataView = grid.setItems(estudianteService.findAllEstudiante());
            } else {
                gridListDataView.addFilter(estudiante -> areAreaEqual(estudiante, areaFilter));
            }
        });
        return areaFilter;
    }

    private boolean areAreaEqual(Estudiante estudiante, ComboBox<Area> areaFilter) {
        String areaFilterValue = areaFilter.getValue().getNombre();
        if (areaFilterValue != null) {
            return StringUtils.equals(estudiante.getArea().getNombre(), areaFilterValue);
        }
        return true;
    }

    private ComboBox<Grupo> FiltrarGrupo() {
        ComboBox<Grupo> grupoFilter = new ComboBox<>();
        grupoFilter.setItems(grupoService.findAllGrupo());
        grupoFilter.setItemLabelGenerator(Grupo::getNumero);
        grupoFilter.setPlaceholder("Filtrar");
        grupoFilter.setClearButtonVisible(true);
        grupoFilter.setWidth("100%");
        grupoFilter.addValueChangeListener(event -> {
            if (grupoFilter.getValue() == null) {
                gridListDataView = grid.setItems(estudianteService.findAllEstudiante());
            } else {
                gridListDataView.addFilter(estudiante -> areGrupoEqual(estudiante, grupoFilter));
            }
        });
        return grupoFilter;
    }

    private boolean areGrupoEqual(Estudiante estudiante, ComboBox<Grupo> grupoFilter) {
        String grupoFilterValue = grupoFilter.getValue().getNumero();
        if (grupoFilterValue != null) {
            return StringUtils.equals(estudiante.getGrupo().getNumero(), grupoFilterValue);
        }
        return true;
    }

    private ComboBox<User> FiltrarUser() {
        ComboBox<User> userFilter = new ComboBox<>();
        userFilter.setItems(userService.findAllUser());
        userFilter.setItemLabelGenerator(User::getName);
        userFilter.setPlaceholder("Filtrar");
        userFilter.setClearButtonVisible(true);
        userFilter.setWidth("100%");
        userFilter.addValueChangeListener(event -> {
            if (userFilter.getValue() == null) {
                gridListDataView = grid.setItems(estudianteService.findAllEstudiante());
            } else {
                gridListDataView.addFilter(estudiante -> areUserEqual(estudiante, userFilter));
            }
        });

        return userFilter;
    }

    private boolean areUserEqual(Estudiante estudiante, ComboBox<User> userFilter) {
        String userFilterValue = userFilter.getValue().getName();
        if (userFilterValue != null) {
            return StringUtils.equals(estudiante.getUser().getName(), userFilterValue);
        }
        return true;
    }

}
