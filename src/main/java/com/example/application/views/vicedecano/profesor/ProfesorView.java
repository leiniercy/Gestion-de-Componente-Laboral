/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.application.views.vicedecano.profesor;

import com.example.application.data.DataService;
import com.example.application.data.entity.*;
import com.example.application.views.MainLayout;
import com.example.application.views.vicedecano.estudiante.EstudiantesView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import javax.annotation.security.RolesAllowed;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Leinier
 */

@PageTitle("Profesor")
@Route(value = "profesor-view", layout = MainLayout.class)
@Uses(Icon.class)
@RolesAllowed("vicedecano")
public class ProfesorView extends VerticalLayout{
    
    private Grid<Profesor> grid = new Grid<>(Profesor.class, false);
    
    private DataService dataService;

    ProfesorForm form;
    
    private GridListDataView<Profesor> gridListDataView;
    
    private Grid.Column<Profesor> nombreColumn = grid.addColumn(Profesor::getNombre).setHeader("Nombre").setAutoWidth(true).setFlexGrow(0);
    private Grid.Column<Profesor> apellidosColumn = grid.addColumn(Profesor::getApellidos).setHeader("Apellidos").setAutoWidth(true).setFlexGrow(0);
    private Grid.Column<Profesor> userColumn = grid.addColumn(profesor -> profesor.getUser().getName()).setHeader("Usuario").setAutoWidth(true).setFlexGrow(0);
    private Grid.Column<Profesor> emailColumn = grid.addColumn(Profesor::getEmail).setHeader("Correo").setAutoWidth(true).setFlexGrow(0);
    private Grid.Column<Profesor> solapinColumn = grid.addColumn(Profesor::getSolapin).setHeader("Solapín").setAutoWidth(true).setFlexGrow(0);
    private Grid.Column<Profesor> areaColumn = grid.addColumn(profesor -> profesor.getA().getNombre()).setHeader("Área").setAutoWidth(true).setFlexGrow(0);
    private Grid.Column<Profesor> editColumn = grid.addComponentColumn(profesor -> {
        HorizontalLayout layout = new HorizontalLayout();
        Button editButton = new Button(VaadinIcon.EDIT.create());
        editButton.addClickShortcut(Key.F2);
        editButton.addClickListener(event -> this.editProfesor(profesor));
        Button removeButton = new Button(VaadinIcon.TRASH.create());
        removeButton.addClickShortcut(Key.DELETE);
        removeButton.addClickListener(event -> this.deleteProfesor(profesor));
        layout.add(editButton,removeButton);
        return layout;
    }).setFlexGrow(0);
    

    public ProfesorView( @Autowired DataService service) {
     
        this.dataService = service;
        addClassNames("profesor-view", "flex", "flex-col", "h-full");
        setSizeFull();
        configureGrid();

        form = new ProfesorForm(service.findAllUser(), service.findAllArea());
        form.setWidth("25em");
        form.addListener(ProfesorForm.SaveEvent.class, this::saveProfesor);
        form.addListener(ProfesorForm.CloseEvent.class, e -> closeEditor());

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
        

        gridListDataView = grid.setItems(dataService.findAllProfesor());
        grid.addClassNames("profesor-grid");
        grid.setSizeFull();
        grid.setHeightFull();
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.addThemeVariants(GridVariant.LUMO_COMPACT);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_COLUMN_BORDERS);
    }

    private HorizontalLayout getToolbar() {

        addClassName("menu-items");
        Html total = new Html("<span>Total: <b>" + dataService.countProfesor()+ "</b> profesores</span>");

        Button menuButton = new Button("Mostar/Ocultar Columnas");
        menuButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        ProfesorView.ColumnToggleContextMenu columnToggleContextMenu = new ProfesorView.ColumnToggleContextMenu(
                menuButton);
        columnToggleContextMenu.addColumnToggleItem("Nombre", nombreColumn);
        columnToggleContextMenu.addColumnToggleItem("Apellidos", apellidosColumn);
        columnToggleContextMenu.addColumnToggleItem("Usuario", userColumn);
        columnToggleContextMenu.addColumnToggleItem("Correo", emailColumn);
        columnToggleContextMenu.addColumnToggleItem("Solapín", solapinColumn);
        columnToggleContextMenu.addColumnToggleItem("Área", areaColumn);


        Button addButton = new Button("Añadir Profesor", VaadinIcon.USER.create());
        addButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        addButton.addClickListener(click -> addProfesor());

        HorizontalLayout toolbar = new HorizontalLayout(total,menuButton,addButton);
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

        void addColumnToggleItem(String label, Grid.Column<Profesor> column) {
            MenuItem menuItem = this.addItem(label, e -> {
                column.setVisible(e.getSource().isChecked());
            });
            menuItem.setCheckable(true);
            menuItem.setChecked(column.isVisible());
        }
    }

    private void saveProfesor(ProfesorForm.SaveEvent event) {
        dataService.saveProfesor(event.getProfesor());
        updateList();
        closeEditor();
    }

    private void deleteProfesor(Profesor profesor) {
        if (profesor == null)
            return;
        dataService.deleteProfesor(profesor);
        this.refreshGrid();
    }

    private void refreshGrid() {
        if (dataService.findAllArea().size() > 0) {
            grid.setVisible(true);
            grid.setItems(dataService.findAllProfesor());
        } else {
            grid.setVisible(false);
        }
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
        grid.setItems(dataService.findAllProfesor());
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
        areaFilter.setItems(dataService.findAllArea());
        areaFilter.setItemLabelGenerator(Area::getNombre);
        areaFilter.setPlaceholder("Filtrar");
        areaFilter.setClearButtonVisible(true);
        areaFilter.setWidth("100%");
        areaFilter.addValueChangeListener(
                event -> gridListDataView
                        .addFilter(profesor -> areAreaEqual(profesor, areaFilter) )
        );
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
        userFilter.setItems(dataService.findAllUser());
        userFilter.setItemLabelGenerator(User::getName);
        userFilter.setPlaceholder("Filtrar");
        userFilter.setClearButtonVisible(true);
        userFilter.setWidth("100%");
        userFilter.addValueChangeListener(
                event -> gridListDataView
                        .addFilter(profesor -> areUserEqual(profesor, userFilter))
        );

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
