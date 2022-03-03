/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.application.views.vicedecano.profesor;

import com.example.application.data.service.*;
import com.example.application.data.entity.*;
import com.example.application.views.MainLayout;
import com.example.application.views.vicedecano.estudiante.EstudiantesView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.contextmenu.MenuItem;
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
import com.vaadin.flow.data.value.ValueChangeMode;
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
@PageTitle("Profesor")
@Route(value = "profesor-view", layout = MainLayout.class)
@Uses(Icon.class)
@RolesAllowed("vicedecano")
public class ProfesorView extends VerticalLayout {

    Grid<Profesor> grid = new Grid<>(Profesor.class, false);

    private UserService userService;
    private AreaService areaService;
    private EstudianteService estudianteService;
    private ProfesorService profesorService;
    private EvaluacionService evaluacionService;
    private GrupoService grupoService;
    private TareaService tareaService;

    ProfesorForm form;

    private GridListDataView<Profesor> gridListDataView;

    private Grid.Column<Profesor> nombreColumn = grid.addColumn(Profesor::getNombre).setHeader("Nombre").setAutoWidth(true).setFlexGrow(0).setSortable(true);
    private Grid.Column<Profesor> apellidosColumn = grid.addColumn(Profesor::getApellidos).setHeader("Apellidos").setAutoWidth(true).setFlexGrow(0).setSortable(true);
    private Grid.Column<Profesor> userColumn = grid.addColumn(profesor -> profesor.getUser().getName()).setHeader("Usuario").setAutoWidth(true).setFlexGrow(0).setSortable(true);
    private Grid.Column<Profesor> emailColumn = grid.addColumn(Profesor::getEmail).setHeader("Correo").setAutoWidth(true).setFlexGrow(0).setSortable(true);
    private Grid.Column<Profesor> solapinColumn = grid.addColumn(Profesor::getSolapin).setHeader("Solapín").setAutoWidth(true).setFlexGrow(0).setSortable(true);
    private Grid.Column<Profesor> areaColumn = grid.addColumn(profesor -> profesor.getA().getNombre()).setHeader("Área").setAutoWidth(true).setFlexGrow(0).setSortable(true);
    private Grid.Column<Profesor> editColumn = grid.addComponentColumn(profesor -> {
        HorizontalLayout layout = new HorizontalLayout();
        Button editButton = new Button(VaadinIcon.EDIT.create());
        editButton.addClickShortcut(Key.F2);
        editButton.addClickListener(event -> this.editProfesor(profesor));
        Button removeButton = new Button(VaadinIcon.TRASH.create());
        removeButton.addClickShortcut(Key.DELETE);
        removeButton.addClickListener(event -> this.deleteProfesor(profesor));
        layout.add(editButton, removeButton);
        return layout;
    }).setFlexGrow(0);

    private HorizontalLayout toolbar;
    private Html total;

    public ProfesorView(
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
        addClassNames("profesor-view", "flex", "flex-col", "h-full");
        setSizeFull();
        configureGrid();

        form = new ProfesorForm(userService.findAllUser(), areaService.findAllArea());
        form.setWidth("25em");
        form.addListener(ProfesorForm.SaveEvent.class, this::saveProfesor);
        form.addListener(ProfesorForm.CloseEvent.class, e -> closeEditor());

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
                -> editProfesor(event.getValue()));

    }

    private void configureGrid() {

        HeaderRow headerRow = grid.appendHeaderRow();
        headerRow.getCell(nombreColumn).setComponent(FiltrarNombre());
        headerRow.getCell(apellidosColumn).setComponent(FiltrarApellidos());
        headerRow.getCell(emailColumn).setComponent(FiltrarEmail());
        headerRow.getCell(userColumn).setComponent(FiltrarUser());
        headerRow.getCell(solapinColumn).setComponent(FiltrarSolapin());
        headerRow.getCell(areaColumn).setComponent(FiltrarArea());

        gridListDataView = grid.setItems(profesorService.findAllProfesor());
        grid.addClassNames("profesor-grid");
        grid.setAllRowsVisible(true);
        grid.setSizeFull();
        grid.setHeightFull();
        grid.setWidthFull();
        grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
    }

    private HorizontalLayout getToolbar() {

        addClassName("menu-items");
        total = new Html("<span>Total: <b>" + profesorService.countProfesor() + "</b> profesores</span>");

        Button menuButton = new Button("Mostar/Ocultar Columnas");
        //menuButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        ProfesorView.ColumnToggleContextMenu columnToggleContextMenu = new ProfesorView.ColumnToggleContextMenu(
                menuButton);
        columnToggleContextMenu.addColumnToggleItem("Nombre", nombreColumn);
        columnToggleContextMenu.addColumnToggleItem("Apellidos", apellidosColumn);
        columnToggleContextMenu.addColumnToggleItem("Usuario", userColumn);
        columnToggleContextMenu.addColumnToggleItem("Correo", emailColumn);
        columnToggleContextMenu.addColumnToggleItem("Solapín", solapinColumn);
        columnToggleContextMenu.addColumnToggleItem("Área", areaColumn);

        Button addButton = new Button("Añadir Profesor", VaadinIcon.USER.create());
        //   addButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        addButton.addClickListener(click -> addProfesor());

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

        void addColumnToggleItem(String label, Grid.Column<Profesor> column) {
            MenuItem menuItem = this.addItem(label, e -> {
                column.setVisible(e.getSource().isChecked());
            });
            menuItem.setCheckable(true);
            menuItem.setChecked(column.isVisible());
        }
    }

    private void saveProfesor(ProfesorForm.SaveEvent event) {

        List<Profesor> listProfesores = profesorService.findAllProfesor();

        listProfesores = listProfesores.parallelStream()
                .filter(profe -> profe.getNombre().equals(event.getProfesor().getNombre())
                && profe.getApellidos().equals(event.getProfesor().getApellidos())
                && profe.getUser().equals(event.getProfesor().getUser())
                && profe.getEmail().equals(event.getProfesor().getEmail())
                && profe.getSolapin().equals(event.getProfesor().getSolapin())
                && profe.getA().equals(event.getProfesor().getA())
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
        dialog.setText(new H3("El profesor ya existe"));
        dialog.setConfirmText("Aceptar");
        dialog.setConfirmButtonTheme("error primary");
        dialog.addConfirmListener(new ComponentEventListener<ConfirmDialog.ConfirmEvent>() {
            @Override
            public void onComponentEvent(ConfirmDialog.ConfirmEvent event) {
                ProfesorView.this.refreshGrid();
            }
        });

        if (listProfesores.size() != 0) {
            dialog.open();
            throw new RuntimeException("El profesor ya existe");
        } else {
            profesorService.saveProfesor(event.getProfesor());
            toolbar.remove(total);
            total = new Html("<span>Total: <b>" + profesorService.countProfesor() + "</b> profesores</span>");
            toolbar.addComponentAtIndex(0, total);
            toolbar.expand(total);
            updateList();
            closeEditor();
        }
    }

    private void deleteProfesor(Profesor profesor) {

        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader(String.format("Eliminar %s", profesor.getStringNombreApellidos()));
        dialog.setText("¿Está seguro/a de que quiere eliminar a este profesor?");

        dialog.setCancelText("Cancelar");
        dialog.setCancelable(true);
        dialog.addCancelListener(event -> {
            this.refreshGrid();
        });

        dialog.setConfirmText("Eliminar");
        dialog.setConfirmButtonTheme("error primary");
        dialog.addConfirmListener(event -> {

            profesorService.deleteProfesor(profesor);

            toolbar.remove(total);
            total = new Html("<span>Total: <b>" + profesorService.countProfesor() + "</b> profesores</span>");
            toolbar.addComponentAtIndex(0, total);
            toolbar.expand(total);

            Notification.show("Profesor eliminado");
            this.refreshGrid();

        });

        if (profesor == null) {
            return;
        } else {
            dialog.open();
        }

    }

    private void refreshGrid() {
        grid.setVisible(true);
        grid.setItems(profesorService.findAllProfesor());
    }

    public void editProfesor(Profesor profesor) {
        if (profesor == null) {
            closeEditor();
        } else {
            form.setProfesor(profesor);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    void addProfesor() {
        grid.asSingleSelect().clear();
        editProfesor(new Profesor());
    }

    private void closeEditor() {
        form.setProfesor(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(profesorService.findAllProfesor());
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
                        .addFilter(profesor -> StringUtils.containsIgnoreCase(profesor.getNombre(), nombreFilter.getValue()))
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
                        .addFilter(profesor -> StringUtils.containsIgnoreCase(profesor.getApellidos(), apellidosFilter.getValue()))
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
                        .addFilter(profesor -> StringUtils.containsIgnoreCase(profesor.getEmail(), emailFilter.getValue()))
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
                        .addFilter(profesor -> StringUtils.containsIgnoreCase(profesor.getSolapin(), solapinFilter.getValue()))
        );
        return solapinFilter;
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
                gridListDataView = grid.setItems(profesorService.findAllProfesor());
            } else {
                gridListDataView.addFilter(profesor -> areAreaEqual(profesor, areaFilter));
            }
        });
        return areaFilter;
    }

    private boolean areAreaEqual(Profesor profesor, ComboBox<Area> areaFilter) {
        String areaFilterValue = areaFilter.getValue().getNombre();
        if (areaFilterValue != null) {
            return StringUtils.equals(profesor.getA().getNombre(), areaFilterValue);
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
                gridListDataView = grid.setItems(profesorService.findAllProfesor());
            } else {
                gridListDataView.addFilter(profesor -> areUserEqual(profesor, userFilter));
            }
        });
        return userFilter;
    }

    private boolean areUserEqual(Profesor profesor, ComboBox<User> userFilter) {
        String userFilterValue = userFilter.getValue().getName();
        if (userFilterValue != null) {
            return StringUtils.equals(profesor.getUser().getName(), userFilterValue);
        }
        return true;
    }

}
