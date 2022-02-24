/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.application.views.vicedecano.area;

import com.example.application.data.service.*;
import com.example.application.data.entity.Area;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.RolesAllowed;

/**
 * @author Leinier
 */
@PageTitle("Area")
@Route(value = "area-view", layout = MainLayout.class)
@RolesAllowed("vicedecano")
public class AreaView extends VerticalLayout {

    private Grid<Area> grid = new Grid<>(Area.class, false);

    Editor<Area> editor = grid.getEditor();

    AreaForm form;

    private UserService userService;
    private AreaService areaService;
    private EstudianteService estudianteService;
    private ProfesorService profesorService;
    private EvaluacionService evaluacionService;
    private GrupoService grupoService;
    private TareaService tareaService;

    private GridListDataView<Area> gridListDataView;

    private Grid.Column<Area> nombreColumn = grid.addColumn(Area::getNombre)
            .setHeader("Nombre")
            .setAutoWidth(true).setFlexGrow(1);
    private Grid.Column<Area> descripcionColumn = grid.addColumn(Area::getDescripcion).setHeader("Descripción").setAutoWidth(true);
    private Grid.Column<Area> editColumn = grid.addComponentColumn(area -> {
        HorizontalLayout layout = new HorizontalLayout();
        Button editButton = new Button(VaadinIcon.EDIT.create());
        editButton.addClickShortcut(Key.F2);
        editButton.addClickListener(e -> this.editArea(area));
        Button removeButton = new Button(VaadinIcon.TRASH.create());
        removeButton.addClickShortcut(Key.DELETE);
        removeButton.addClickListener(e -> this.deleteArea(area));
        layout.add(editButton, removeButton);
        return layout;
    }).setFlexGrow(0);

    private Html total;
    private HorizontalLayout toolbar;

    public AreaView(
            @Autowired UserService userService,
            @Autowired AreaService areaService,
            @Autowired EstudianteService estudianteService,
            @Autowired ProfesorService profesorService,
            @Autowired EvaluacionService evaluacionService,
            @Autowired GrupoService grupoService,
            @Autowired TareaService tareaServiceF
    ) {

        this.userService = userService;
        this.areaService = areaService;
        this.estudianteService = estudianteService;
        this.profesorService = profesorService;
        this.evaluacionService = evaluacionService;
        this.evaluacionService = evaluacionService;
        this.tareaService = tareaService;
        addClassNames("area-view", "flex", "flex-col");
        setSizeFull();
        configureGrid();

        form = new AreaForm();
        form.setWidth("25em");
        form.addListener(AreaForm.SaveEvent.class, this::saveArea);
        form.addListener(AreaForm.CloseEvent.class, e -> closeEditor());

        Section section1 = new Section(grid);
        Scroller scroller = new Scroller(new Div(section1));
        scroller.setScrollDirection(Scroller.ScrollDirection.BOTH);
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
                -> editArea(event.getValue()));

    }

    private void configureGrid() {

        HeaderRow headerRow = grid.appendHeaderRow();
        headerRow.getCell(nombreColumn).setComponent(FiltrarNombre());
        headerRow.getCell(descripcionColumn).setComponent(FiltrarDescripcion());

        gridListDataView = grid.setItems(areaService.findAllArea());
        grid.addClassNames("area-grid");
        grid.setAllRowsVisible(true);
        grid.setSizeFull();
        grid.setHeightFull();
        grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
        grid.addThemeVariants(GridVariant.LUMO_COMPACT);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
//        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_COLUMN_BORDERS);

    }

    private Component getToolbar() {

        addClassName("menu-items");
        total = new Html("<span>Total: <b>" + areaService.countArea() + "</b> areas</span>");

        Button addButton = new Button("Añadir Área", VaadinIcon.USER.create());
//        addButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        addButton.addClickListener(click -> addArea());

        toolbar = new HorizontalLayout(total, addButton);
        toolbar.setAlignItems(FlexComponent.Alignment.BASELINE);
        toolbar.setWidth("100%");
        toolbar.expand(total);
        toolbar.getStyle()
                .set("padding", "var(--lumo-space-wide-m)");

        return toolbar;
    }

    private void saveArea(AreaForm.SaveEvent event) {
        
        areaService.saveArea(event.getArea());
        
        toolbar.remove(total);
        total = new Html("<span>Total: <b>" + areaService.countArea() + "</b> areas</span>");
        toolbar.addComponentAtIndex(0, total);
        toolbar.expand(total);
        
        updateList();
        closeEditor();
    }

    private void deleteArea(Area area) {

        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader(String.format("Eliminar %s?", area.getNombre()));
        dialog.setText("Está seguro/a de que quiere eliminar esta área?");

        dialog.setCancelText("Cancelar");
        dialog.setCancelable(true);
        dialog.addCancelListener(event -> {
            this.refreshGrid();
        });

        dialog.setConfirmText("Eliminar");
        dialog.setConfirmButtonTheme("error primary");
        dialog.addConfirmListener(event -> {

            areaService.deleteArea(area);

            toolbar.remove(total);
            total = new Html("<span>Total: <b>" + areaService.countArea() + "</b> areas</span>");
            toolbar.addComponentAtIndex(0, total);
            toolbar.expand(total);

            Notification.show("Área eliminada");
            this.refreshGrid();
        });

        if (area == null) {
            return;
        } else {
            dialog.open();
        }
    }

    private void refreshGrid() {
        grid.setVisible(true);
        grid.setItems(areaService.findAllArea());
    }

    public void editArea(Area area) {
        if (area == null) {
            closeEditor();
        } else {
            form.setArea(area);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    void addArea() {
        grid.asSingleSelect().clear();
        editArea(new Area());
    }

    private void closeEditor() {
        form.setArea(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(areaService.findAllArea());
    }

    //Filtros
    private TextField FiltrarNombre() {

        TextField filterNombre = new TextField();
        filterNombre.setPlaceholder("Filtrar");
        filterNombre.setPrefixComponent(VaadinIcon.SEARCH.create());
        filterNombre.setClearButtonVisible(true);
        filterNombre.setWidth("100%");
        filterNombre.setValueChangeMode(ValueChangeMode.EAGER);
        filterNombre.addValueChangeListener(
                event -> gridListDataView
                        .addFilter(area -> StringUtils.containsIgnoreCase(area.getNombre(), filterNombre.getValue()))
        );

        return filterNombre;
    }

    private TextField FiltrarDescripcion() {
        TextField filterDescripcion = new TextField();
        filterDescripcion.setPlaceholder("Filtrar");
        filterDescripcion.setPrefixComponent(VaadinIcon.SEARCH.create());
        filterDescripcion.setClearButtonVisible(true);
        filterDescripcion.setWidth("100%");
        filterDescripcion.setValueChangeMode(ValueChangeMode.LAZY);
        filterDescripcion.addValueChangeListener(
                event -> gridListDataView
                        .addFilter(area -> StringUtils.containsIgnoreCase(area.getDescripcion(), filterDescripcion.getValue()))
        );
        return filterDescripcion;
    }

}
