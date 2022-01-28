/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.application.views.vicedecano.estudiante;


import com.example.application.data.entity.*;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.example.application.data.DataService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import javax.annotation.security.RolesAllowed;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Leinier
 */
@PageTitle("Estudiante")
@Route(value = "estudiantes-view", layout = MainLayout.class)
@RolesAllowed("vicedecano")
public class EstudiantesView extends VerticalLayout {

    private Grid<Estudiante> grid = new Grid<>(Estudiante.class, false);

    EstudianteForm form;

    private DataService dataService;

    private GridListDataView<Estudiante> gridListDataView;

    private Grid.Column<Estudiante> nombreColumn = grid.addColumn(Estudiante::getNombre).setHeader("Nombre").setAutoWidth(true).setFlexGrow(0);
    private Grid.Column<Estudiante> apellidosColumn = grid.addColumn(Estudiante::getApellidos).setHeader("Apellidos").setAutoWidth(true).setFlexGrow(0);
    private Grid.Column<Estudiante> userColumn = grid.addColumn(estudiante -> estudiante.getUser().getName()).setHeader("Usuario").setAutoWidth(true).setFlexGrow(0);
    private Grid.Column<Estudiante> emailColumn = grid.addColumn(Estudiante::getEmail).setHeader("Correo").setAutoWidth(true).setFlexGrow(0);
    private Grid.Column<Estudiante> solapinColumn = grid.addColumn(Estudiante::getSolapin).setHeader("Solapín").setAutoWidth(true).setFlexGrow(0);
    private Grid.Column<Estudiante> anno_repitenciaColumn = grid.addColumn(Estudiante::getAnno_repitencia).setHeader("Año de repitencia").setAutoWidth(true).setFlexGrow(0);
    private Grid.Column<Estudiante> cantidad_asignaturasColumn = grid.addColumn(Estudiante::getCantidad_asignaturas).setHeader("Cantidad de asignaturas").setAutoWidth(true).setFlexGrow(0);
    private Grid.Column<Estudiante> areaColumn = grid.addColumn(estudiante -> estudiante.getArea().getNombre()).setHeader("Área").setAutoWidth(true).setFlexGrow(0);
    private Grid.Column<Estudiante> grupoColumn = grid.addColumn(estudiante -> estudiante.getGrupo().getNumero()).setHeader("Grupo").setAutoWidth(true).setFlexGrow(0);
    private Grid.Column<Estudiante> editColumn = grid.addComponentColumn(estudiante -> {
        HorizontalLayout layout = new HorizontalLayout();
        Button editButton = new Button(VaadinIcon.EDIT.create());
        editButton.addClickShortcut(Key.F2);
        editButton.addClickListener(event -> this.editEstudiante(estudiante));
        Button removeButton = new Button(VaadinIcon.TRASH.create());
        removeButton.addClickShortcut(Key.DELETE);
        removeButton.addClickListener(event -> this.deleteEstudiante(estudiante));
        layout.add(editButton,removeButton);
        return layout;
    }).setFlexGrow(0);


    public EstudiantesView( @Autowired DataService service ) {
        
        this.dataService = service;
        addClassNames("list-est-view", "flex", "flex-col", "h-full");
        setSizeFull();
        configureGrid();

        form = new EstudianteForm(service.findAllUser(), service.findAllArea(), service.findAllGrupo());
        form.setWidth("25em");
        form.addListener(EstudianteForm.SaveEvent.class, this::saveEstudiante);
        form.addListener(EstudianteForm.CloseEvent.class, e -> closeEditor());

        FlexLayout content = new FlexLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.setFlexShrink(0, form);
        content.addClassNames("content", "gap-m");
        content.setSizeFull();

        add(getToolbar(), content);
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

        gridListDataView = grid.setItems(dataService.findAllEstudiante());
        grid.addClassNames("estudiante-grid");
        grid.setAllRowsVisible(true);
        grid.setSizeFull();
        grid.setHeightFull();
        //grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
        grid.addThemeVariants(GridVariant.LUMO_COMPACT);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_COLUMN_BORDERS);
    }

    private HorizontalLayout getToolbar() {

        addClassName("menu-items");
        Html total = new Html("<span>Total: <b>" + dataService.countEstudiante() + "</b> estudiantes</span>");

        Button menuButton = new Button("Mostar/Ocultar Columnas");
        menuButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
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
        addButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        addButton.addClickListener(click -> addEstudiante());

        HorizontalLayout toolbar = new HorizontalLayout(total, menuButton,addButton);
        toolbar.setAlignItems(FlexComponent.Alignment.BASELINE);
        toolbar.setWidth("100%");
        toolbar.expand(total);


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
        dataService.saveEstudiante(event.getEstudiante());
        updateList();
        closeEditor();
    }

    private void deleteEstudiante(Estudiante estudiante) {
        if (estudiante == null)
            return;
        dataService.deleteEstudiante(estudiante);
        Notification.show("Estudiante eliminado");
        this.refreshGrid();
    }

    private void refreshGrid() {
        if (dataService.findAllArea().size() > 0) {
            grid.setVisible(true);
            grid.setItems(dataService.findAllEstudiante());
        } else {
            grid.setVisible(false);
        }
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
        grid.setItems(dataService.findAllEstudiante());
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
        areaFilter.setItems(dataService.findAllArea());
        areaFilter.setItemLabelGenerator(Area::getNombre);
        areaFilter.setPlaceholder("Filtrar");
        areaFilter.setClearButtonVisible(true);
        areaFilter.setWidth("100%");
        areaFilter.addValueChangeListener(
                 event -> gridListDataView
                        .addFilter(estudiante -> areAreaEqual(estudiante, areaFilter) )
        );
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
        grupoFilter.setItems(dataService.findAllGrupo());
        grupoFilter.setItemLabelGenerator(Grupo::getNumero);
        grupoFilter.setPlaceholder("Filtrar");
        grupoFilter.setClearButtonVisible(true);
        grupoFilter.setWidth("100%");
        grupoFilter.addValueChangeListener(
                event -> gridListDataView
                        .addFilter(estudiante ->  areGrupoEqual(estudiante , grupoFilter) )
                            
        );
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
        userFilter.setItems(dataService.findAllUser());
        userFilter.setItemLabelGenerator(User::getName);
        userFilter.setPlaceholder("Filtrar");
        userFilter.setClearButtonVisible(true);
        userFilter.setWidth("100%");
        userFilter.addValueChangeListener(
             event -> gridListDataView
                        .addFilter(estudiante -> areUserEqual(estudiante, userFilter))
        );
        
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
