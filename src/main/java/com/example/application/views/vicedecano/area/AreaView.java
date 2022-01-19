/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.application.views.vicedecano.area;

import com.example.application.data.DataService;
import com.example.application.data.entity.Area;
import com.example.application.data.service.AreaService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
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
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Leinier
 */
@PageTitle("Area")
@Route(value = "area-view", layout = MainLayout.class)
@RolesAllowed("vicedecano")
public class AreaView extends VerticalLayout {

    private Grid<Area> grid = new Grid<>(Area.class, false);

    AreaForm form;

    TextField filter = new TextField();

    private AreaService areaService;

    private DataService dataService;

    private Grid.Column<Area> nombreColumn = grid.addColumn(Area::getNombre).setHeader("Nombre").setAutoWidth(true);
    private Grid.Column<Area> descripcionColumn = grid.addColumn(Area::getDescripcion).setHeader("Descripción").setAutoWidth(true);

    public AreaView(
            @Autowired AreaService areaService,
            @Autowired DataService dataService
    ) {
        this.areaService = areaService;
        this.dataService = dataService;
        addClassNames("area-view", "flex", "flex-col", "h-full");
        setSizeFull();
        configureGrid();

        form = new AreaForm();
        form.setWidth("25em");
        form.addListener(AreaForm.SaveEvent.class, this::saveArea);
        form.addListener(AreaForm.DeleteEvent.class, this::deleteArea);
        form.addListener(AreaForm.CloseEvent.class, e -> closeEditor());
        
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
                -> editArea(event.getValue()));

    }

    private void configureGrid() {

        HeaderRow headerRow = grid.appendHeaderRow();
        headerRow.getCell(nombreColumn).setComponent(FiltrarNombre());
        headerRow.getCell(descripcionColumn).setComponent(FiltrarDescripcion());

        grid.addClassNames("estudiante-grid");
        grid.setSizeFull();
        grid.setHeightFull();
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private HorizontalLayout getToolbar() {

        addClassName("menu-items");
        Html total = new Html("<span>Total: <b>" + dataService.countArea()+ "</b> areas</span>");

        Button addButton = new Button("Añadir Área", VaadinIcon.USER.create());
        addButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        addButton.addClickListener(click -> addArea());

        HorizontalLayout toolbar = new HorizontalLayout(total, addButton);
        toolbar.setAlignItems(FlexComponent.Alignment.CENTER);
        toolbar.setFlexGrow(1, toolbar);
        toolbar.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        toolbar.setSpacing(false);

        return toolbar;
    }

    private void saveArea(AreaForm.SaveEvent event) {
        dataService.saveArea(event.getArea());
        updateList();
        closeEditor();
    }

    private void deleteArea(AreaForm.DeleteEvent event) {
        dataService.deleteArea(event.getArea());
        updateList();
        closeEditor();
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
        grid.setItems(dataService.searchArea(filter.getValue()));
    }

    //Filtros
    private TextField FiltrarNombre() {

        TextField filterNombre = new TextField();
        filterNombre.setPlaceholder("Filtrar");
        filterNombre.setPrefixComponent(VaadinIcon.SEARCH.create());
        filterNombre.setClearButtonVisible(true);
        filterNombre.setWidth("100%");
        filterNombre.setValueChangeMode(ValueChangeMode.LAZY);
        filterNombre.addValueChangeListener(e -> {
            grid.setItems(dataService.searchAreaByName(filterNombre.getValue()));
        });

        return filterNombre;
    }

    private TextField FiltrarDescripcion() {
        TextField filterDescripcion = new TextField();
        filterDescripcion.setPlaceholder("Filtrar");
        filterDescripcion.setPrefixComponent(VaadinIcon.SEARCH.create());
        filterDescripcion.setClearButtonVisible(true);
        filterDescripcion.setWidth("100%");
        filterDescripcion.setValueChangeMode(ValueChangeMode.LAZY);
        filterDescripcion.addValueChangeListener(e -> {
            grid.setItems(dataService.searchAreaByDescripcion(filterDescripcion.getValue()));
        });
        return filterDescripcion;
    }

}
