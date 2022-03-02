package com.example.application.views.jefe_area.tarea;



import com.example.application.data.entity.*;
import com.example.application.data.service.TareaService;
import com.vaadin.flow.component.grid.Grid;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author Leinier
 **/

@RunWith(SpringRunner.class)
@SpringBootTest
public class TareaViewTest {

    @Autowired
    private TareaView tareaView;
    @Autowired
    TareaService tareaService;

    private Grid<Tarea> grid;

    private List<Tarea>tareas;

    @Test
    public void formShownWhenTareaSelected() {}
    @Test
    public void SetTareaSelected() {}

    @Test
    public void DeleteTareaSelected() {
        grid = tareaView.grid;
        Tarea firstTarea = getFirtsItemToGrid();
        //si la primer tarea fue seleccionada elimninala
        grid.asSingleSelect().setValue(firstTarea);
        tareaService.deleteTarea(firstTarea);
    }

    private Tarea getFirtsItemToGrid() {
        tareas = tareaService.findAllTareas();
        return tareas.get(0);
    }




}
