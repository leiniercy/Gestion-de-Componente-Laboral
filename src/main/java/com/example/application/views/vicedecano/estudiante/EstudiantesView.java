package com.example.application.views.vicedecano.estudiante;

import com.example.application.data.DataService;
import com.example.application.data.entity.Area;
import com.example.application.data.entity.Estudiante;
import com.example.application.data.entity.Grupo;
import com.example.application.data.entity.User;
import com.example.application.data.service.EstudianteService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;

import java.util.Optional;
import javax.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

@PageTitle("Estudiantes")
@Route(value = "estudiantes/:estudianteID?/:action?(edit)", layout = MainLayout.class)
@RolesAllowed("vicedecano")
public class EstudiantesView extends Div implements BeforeEnterObserver {

    private final String ESTUDIANTE_ID = "estudianteID";
    private final String ESTUDIANTE_EDIT_ROUTE_TEMPLATE = "estudiantes/%d/edit";

    private Grid<Estudiante> grid = new Grid<>(Estudiante.class, false);

    private TextField nombre;
    private TextField apellidos;
    private ComboBox<User>user;
    private EmailField email;
    private TextField solapin;
    private IntegerField anno_repitencia;
    private IntegerField cantidad_asignaturas;
    private ComboBox<Area> area;
    private ComboBox<Grupo> grupo;

    private Button save = new Button("Añadir");
    private Button cancel = new Button("Cancelar");
    private Button delete = new Button("Eliminar");

    private BeanValidationBinder<Estudiante> binder;

    private Estudiante estudiante;

    private EstudianteService estudianteService;

    private DataService dataService;

    private Grid.Column<Estudiante> nombreColumn = grid.addColumn(Estudiante::getNombre).setHeader("Nombre").setAutoWidth(true);
    private Grid.Column<Estudiante> apellidosColumn = grid.addColumn(Estudiante::getApellidos).setHeader("Apellidos").setAutoWidth(true);
    private Grid.Column<Estudiante> userColumn = grid.addColumn(estudiante -> estudiante.getUser().getName()).setHeader("Grupo").setAutoWidth(true);
    private Grid.Column<Estudiante> emailColumn = grid.addColumn(Estudiante::getEmail).setHeader("Correo").setAutoWidth(true);
    private Grid.Column<Estudiante> solapinColumn = grid.addColumn(Estudiante::getSolapin).setHeader("Solapín").setAutoWidth(true);
    private Grid.Column<Estudiante> anno_repitenciaColumn = grid.addColumn(Estudiante::getAnno_repitencia).setHeader("Año de repitencia").setAutoWidth(true);
    private Grid.Column<Estudiante> cantidad_asignaturasColumn = grid.addColumn(Estudiante::getCantidad_asignaturas).setHeader("Cantidad de asignaturas").setAutoWidth(true);
    private Grid.Column<Estudiante> areaColumn = grid.addColumn(estudiante -> estudiante.getArea().getNombre()).setHeader("Área").setAutoWidth(true);
    private Grid.Column<Estudiante> grupoColumn = grid.addColumn(estudiante -> estudiante.getGrupo().getNumero()).setHeader("Grupo").setAutoWidth(true);

    public EstudiantesView(
            @Autowired EstudianteService estudianteService,
            @Autowired DataService dataService
    ) {

        this.estudianteService = estudianteService;
        this.dataService = dataService;
        addClassNames("estudiantes-view", "flex", "flex-col", "h-full");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
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
        
        grid.setItems(query -> estudianteService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(ESTUDIANTE_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(EstudiantesView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Estudiante.class);

        // Bind fields. This where you'd define e.g. validation rules


        binder.bindInstanceFields(this);

        area.setItems(dataService.findAllArea());
        area.setItemLabelGenerator(Area::getNombre);

        grupo.setItems(dataService.findAllGrupo());
        grupo.setItemLabelGenerator(Grupo::getNumero);

        //Button
        save.addClickShortcut(Key.ENTER);
        save.addClickListener(e -> {
            try {
                if (this.estudiante == null) {
                    this.estudiante = new Estudiante();
                }
                binder.writeBean(this.estudiante);
                estudianteService.update(this.estudiante);
                clearForm();
                refreshGrid();
                Notification.show("Estudiante añadido correctamente.");
                UI.getCurrent().navigate(EstudiantesView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the estudiante details.");
            }
        });

        cancel.addClickShortcut(Key.ESCAPE);
        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        delete.addClickShortcut(Key.DELETE);
        delete.addClickListener(e -> {
            try {
                if (this.estudiante == null) {
                    this.estudiante = new Estudiante();
                }
                binder.writeBean(this.estudiante);
                estudianteService.delete(this.estudiante);
                clearForm();
                refreshGrid();
                Notification.show("Estudiante eliminado correctamente.");
                UI.getCurrent().navigate(EstudiantesView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the estudiante details.");
            }
        });

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Integer> estudianteId = event.getRouteParameters().getInteger(ESTUDIANTE_ID);
        if (estudianteId.isPresent()) {
            Optional<Estudiante> estudianteFromBackend = estudianteService.get(estudianteId.get());
            if (estudianteFromBackend.isPresent()) {
                populateForm(estudianteFromBackend.get());
            } else {
                Notification.show(String.format("The requested estudiante was not found, ID = %d", estudianteId.get()),
                        3000, Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(EstudiantesView.class);
            }
        }
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("flex flex-col");
        editorLayoutDiv.setWidth("400px");

        Div editorDiv = new Div();
        editorDiv.setClassName("p-l flex-grow");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        
        nombre = new TextField("Nombre");
        
        apellidos = new TextField("Apellidos");
        
        user =  new ComboBox<>("Usuario");
        
        email = new EmailField("Email");
        email.setPlaceholder("usuario@estdiantes.uci.cu");
        email.setPattern("^.+@estudiantes.uci\\.cu$");
        email.setErrorMessage("Por favor escriba un correo válido");
        email.setClearButtonVisible(true);
        
        solapin = new TextField("Solapin");
        
        anno_repitencia = new IntegerField("Año de repitencia");
        anno_repitencia.setHelperText("Máximo 5");
        anno_repitencia.setValue(1);
        anno_repitencia.setHasControls(true);
        anno_repitencia.setMin(1);   
        anno_repitencia.setMax(5);   
        
        cantidad_asignaturas = new IntegerField("Cantidad de asignaturas");
        cantidad_asignaturas.setHelperText("Máximo 16");
        cantidad_asignaturas.setValue(2);
        cantidad_asignaturas.setHasControls(true);
        cantidad_asignaturas.setMin(2);   
        cantidad_asignaturas.setMax(16); 
        
        grupo = new ComboBox<>("Grupo");
        area = new ComboBox<>("Area");
        Component[] fields = new Component[]{nombre, apellidos, user, email, solapin, anno_repitencia, cantidad_asignaturas, grupo, area};

        for (Component field : fields) {
            ((HasStyle) field).addClassName("full-width");
        }
        formLayout.add(fields);
        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("w-full flex-wrap bg-contrast-5 py-s px-l");
        buttonLayout.setSpacing(true);
        buttonLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, delete, cancel);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setId("grid-wrapper");
        wrapper.setWidthFull();
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getLazyDataView().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Estudiante value) {
        this.estudiante = value;
        binder.readBean(this.estudiante);

    }

    // Filtros
    private TextField FiltrarNombre() {

        TextField nombreFilter = new TextField();
        nombreFilter.setPlaceholder("Filtrar");
        nombreFilter.setPrefixComponent(VaadinIcon.SEARCH.create());
        nombreFilter.setClearButtonVisible(true);
        nombreFilter.setWidth("100%");
        nombreFilter.setValueChangeMode(ValueChangeMode.LAZY);
        nombreFilter.addValueChangeListener(e -> {
            grid.setItems(dataService.searchEstudianteByNombre(nombreFilter.getValue()));
        });

        return nombreFilter;
    }

    private TextField FiltrarApellidos() {
        TextField apellidosFilter = new TextField();
        apellidosFilter.setPlaceholder("Filtrar");
        apellidosFilter.setPrefixComponent(VaadinIcon.SEARCH.create());
        apellidosFilter.setClearButtonVisible(true);
        apellidosFilter.setWidth("100%");
        apellidosFilter.setValueChangeMode(ValueChangeMode.LAZY);
        apellidosFilter.addValueChangeListener(e -> {
            grid.setItems(dataService.searchEstudianteByApellidos(apellidosFilter.getValue()));
        });
        return apellidosFilter;
    }

    private TextField FiltrarEmail() {
        TextField emailFilter = new TextField();
        emailFilter.setPlaceholder("Filtrar");
        emailFilter.setPrefixComponent(VaadinIcon.SEARCH.create());
        emailFilter.setClearButtonVisible(true);
        emailFilter.setWidth("100%");
        emailFilter.setValueChangeMode(ValueChangeMode.LAZY);
        emailFilter.addValueChangeListener(e -> {
            grid.setItems(dataService.searchEstudianteByEmail(emailFilter.getValue()));
        });
        return emailFilter;
    }

    private TextField FiltrarSolapin() {
        TextField solapinFilter = new TextField();
        solapinFilter.setPlaceholder("Filtrar");
        solapinFilter.setPrefixComponent(VaadinIcon.SEARCH.create());
        solapinFilter.setClearButtonVisible(true);
        solapinFilter.setWidth("100%");
        solapinFilter.setValueChangeMode(ValueChangeMode.LAZY);
        solapinFilter.addValueChangeListener(e -> {
            grid.setItems(dataService.searchEstudianteBySolapin(solapinFilter.getValue()));
        });
        return solapinFilter;
    }

    private IntegerField FiltrarAnno_repitencia() {
        IntegerField anno_repitenciaFilter = new IntegerField();
        anno_repitenciaFilter.setPlaceholder("Filtrar");
        anno_repitenciaFilter.setPrefixComponent(VaadinIcon.SEARCH.create());
        anno_repitenciaFilter.setClearButtonVisible(true);
        anno_repitenciaFilter.setWidth("100%");
        anno_repitenciaFilter.setValueChangeMode(ValueChangeMode.LAZY);
        anno_repitenciaFilter.addValueChangeListener(e -> {
            grid.setItems(dataService.searchEstudianteByAnno_repitencia(anno_repitenciaFilter.getValue()));
        });
        return anno_repitenciaFilter;
    }

    private IntegerField FiltrarCantidad_asignaturas() {
        IntegerField cantidad_asignaturasFilter = new IntegerField();
        cantidad_asignaturasFilter.setPlaceholder("Filtrar");
        cantidad_asignaturasFilter.setPrefixComponent(VaadinIcon.SEARCH.create());
        cantidad_asignaturasFilter.setClearButtonVisible(true);
        cantidad_asignaturasFilter.setWidth("100%");
        cantidad_asignaturasFilter.setValueChangeMode(ValueChangeMode.LAZY);
        cantidad_asignaturasFilter.addValueChangeListener(e -> {
            grid.setItems(dataService.searchEstudianteByCantidad_asignaturas(cantidad_asignaturasFilter.getValue()));
        });
        return cantidad_asignaturasFilter;
    }

    private ComboBox<Area> FiltrarArea() {
        ComboBox<Area> areaFilter = new ComboBox<>();
        areaFilter.setItems(dataService.findAllArea());
        areaFilter.setItemLabelGenerator(Area::getNombre);
        areaFilter.setPlaceholder("Filtrar");
        areaFilter.setClearButtonVisible(true);
        areaFilter.setWidth("100%");
        areaFilter.addValueChangeListener(e -> {
            grid.setItems(dataService.searchEstudianteByArea( areaFilter.getValue().getNombre() ));
        });
        return areaFilter;
    }

    private ComboBox<Grupo> FiltrarGrupo() {
        ComboBox<Grupo> grupoFilter = new ComboBox<>();
        grupoFilter.setItems(dataService.findAllGrupo());
        grupoFilter.setItemLabelGenerator(Grupo::getNumero);
        grupoFilter.setPlaceholder("Filtrar");
        grupoFilter.setClearButtonVisible(true);
        grupoFilter.setWidth("100%");
        grupoFilter.addValueChangeListener(e -> {
            grid.setItems(dataService.searchEstudianteByGrupo(grupoFilter.getValue().getNumero()));
        });
        return grupoFilter;
    }

    private ComboBox<User> FiltrarUser() {
        ComboBox<User> userFilter = new ComboBox<>();
//        userFilter.setItems();
//        userFilter.setItemLabelGenerator(Grupo::getNumero);
        userFilter.setPlaceholder("Filtrar");
        userFilter.setClearButtonVisible(true);
        userFilter.setWidth("100%");
        userFilter.addValueChangeListener(e -> {
//            grid.setItems(dataService.searchUser(userFilter.getValue().getNombre()));
        });
        return userFilter;
    }

}
