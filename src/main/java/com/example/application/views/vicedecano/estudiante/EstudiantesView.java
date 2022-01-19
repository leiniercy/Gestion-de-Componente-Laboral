/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.application.views.vicedecano.estudiante;

import com.example.application.views.Prueba.Contact;
import com.example.application.views.Prueba.ContactForm;
import com.example.application.views.Prueba.CrmService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.example.application.data.DataService;
import com.example.application.data.entity.Area;
import com.example.application.data.entity.Estudiante;
import com.example.application.data.entity.Grupo;
import com.example.application.data.entity.User;
import com.example.application.data.service.EstudianteService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import javax.annotation.security.RolesAllowed;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.crudui.crud.Crud;

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

    TextField filter = new TextField();

    private DataService dataService;

    private EstudianteService estudianteService;

    private GridListDataView<Estudiante> gridListDataView;

    private Grid.Column<Estudiante> nombreColumn = grid.addColumn(Estudiante::getNombre).setHeader("Nombre").setAutoWidth(true);
    private Grid.Column<Estudiante> apellidosColumn = grid.addColumn(Estudiante::getApellidos).setHeader("Apellidos").setAutoWidth(true);
    private Grid.Column<Estudiante> userColumn = grid.addColumn(estudiante -> estudiante.getUser().getName()).setHeader("Usuario").setAutoWidth(true);
    private Grid.Column<Estudiante> emailColumn = grid.addColumn(Estudiante::getEmail).setHeader("Correo").setAutoWidth(true);
    private Grid.Column<Estudiante> solapinColumn = grid.addColumn(Estudiante::getSolapin).setHeader("Solapín").setAutoWidth(true);
    private Grid.Column<Estudiante> anno_repitenciaColumn = grid.addColumn(Estudiante::getAnno_repitencia).setHeader("Año de repitencia").setAutoWidth(true);
    private Grid.Column<Estudiante> cantidad_asignaturasColumn = grid.addColumn(Estudiante::getCantidad_asignaturas).setHeader("Cantidad de asignaturas").setAutoWidth(true);
    private Grid.Column<Estudiante> areaColumn = grid.addColumn(estudiante -> estudiante.getArea().getNombre()).setHeader("Área").setAutoWidth(true);
    private Grid.Column<Estudiante> grupoColumn = grid.addColumn(estudiante -> estudiante.getGrupo().getNumero()).setHeader("Grupo").setAutoWidth(true);

    public EstudiantesView(
            @Autowired DataService service,
            @Autowired EstudianteService estudianteService
    ) {
        this.dataService = service;
        this.estudianteService = estudianteService;
        addClassNames("list-est-view", "flex", "flex-col", "h-full");
        setSizeFull();
        configureGrid();

        form = new EstudianteForm(service.findAllUser(), service.findAllArea(), service.findAllGrupo());
        form.setWidth("25em");
        form.addListener(EstudianteForm.SaveEvent.class, this::saveEstudiante);
        form.addListener(EstudianteForm.DeleteEvent.class, this::deleteEstudiante);
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
        grid.setSizeFull();
        grid.setHeightFull();
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private HorizontalLayout getToolbar() {

        addClassName("menu-items");
        Html total = new Html("<span>Total: <b>" + dataService.countEstudiante() + "</b> estudiantes</span>");

        Button addButton = new Button("Añadir Estudiante", VaadinIcon.USER.create());
        addButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        addButton.addClickListener(click -> addEstudiante());

        HorizontalLayout toolbar = new HorizontalLayout(total, addButton);
        toolbar.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        toolbar.setWidth("100%");
        toolbar.expand(total);

        return toolbar;
    }

    private void saveEstudiante(EstudianteForm.SaveEvent event) {
        dataService.saveEstudiante(event.getEstudiante());
        updateList();
        closeEditor();
    }

    private void deleteEstudiante(EstudianteForm.DeleteEvent event) {
        dataService.deleteEstudiante(event.getEstudiante());
        updateList();
        closeEditor();
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
                        .addFilter(estudiante -> StringUtils.containsIgnoreCase(estudiante.getArea().getNombre(), areaFilter.getValue().getNombre() ))
        );
        return areaFilter;
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
                        .addFilter(estudiante -> StringUtils.containsIgnoreCase( estudiante.getGrupo().getNumero() , grupoFilter.getValue().getNumero() ))
                            
        );
        return grupoFilter;
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
                        .addFilter(estudiante -> StringUtils.containsIgnoreCase(estudiante.getUser().getName() , userFilter.getValue().getName() ))
        );
        
        return userFilter;
    }

}
