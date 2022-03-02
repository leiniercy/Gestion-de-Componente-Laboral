package com.example.application.views.vicedecano.estudiante;

import com.example.application.data.entity.Estudiante;
import com.example.application.data.service.EstudianteService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.ListDataProvider;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Leinier
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class EstudianteViewTest {

    @Autowired
    private EstudiantesView estudiantesView;

    private Grid<Estudiante> grid;
    private EstudianteForm form;


    @Test
    public void formShownWhenEstudianteSelected() {
        grid = estudiantesView.grid;
        Estudiante firstEstudiante = getFirtsItemToGrid(grid);
        EstudianteForm form = estudiantesView.form;
        Assert.assertFalse(form.isVisible());
        grid.asSingleSelect().setValue(firstEstudiante);
        Assert.assertTrue(form.isVisible());
        Assert.assertEquals(firstEstudiante.getNombre(), form.nombre.getValue());
        Assert.assertEquals(firstEstudiante.getApellidos(), form.apellidos.getValue());
        Assert.assertEquals(firstEstudiante.getUser(), form.user.getValue());
        Assert.assertEquals(firstEstudiante.getEmail(), form.email.getValue());
        Assert.assertEquals(firstEstudiante.getSolapin(), form.solapin.getValue());
        Assert.assertEquals(firstEstudiante.getAnno_repitencia(), form.anno_repitencia.getValue());
        Assert.assertEquals(firstEstudiante.getCantidad_asignaturas(), form.cantidad_asignaturas.getValue());
        Assert.assertEquals(firstEstudiante.getGrupo(), form.grupo.getValue());
        Assert.assertEquals(firstEstudiante.getArea(), form.area.getValue());
    }

    private Estudiante getFirtsItemToGrid(Grid<Estudiante> grid) {
        return ((ListDataProvider<Estudiante>) grid.getDataProvider()).getItems().iterator().next();
    }


    //Modificar estudiante
    @Test
    public void SetEstudianteSelected() {

        grid = estudiantesView.grid;
        Estudiante firstEstudiante = getFirtsItemToGrid(grid);
        form = estudiantesView.form;
        Assert.assertFalse(form.isVisible());
        grid.asSingleSelect().setValue(firstEstudiante);
        Assert.assertTrue(form.isVisible());
        Assert.assertEquals(firstEstudiante.getNombre(), form.nombre.getValue());
        Assert.assertEquals(firstEstudiante.getApellidos(), form.apellidos.getValue());
        Assert.assertEquals(firstEstudiante.getUser(), form.user.getValue());
        Assert.assertEquals(firstEstudiante.getEmail(), form.email.getValue());
        Assert.assertEquals(firstEstudiante.getSolapin(), form.solapin.getValue());
        Assert.assertEquals(firstEstudiante.getAnno_repitencia(), form.anno_repitencia.getValue());
        Assert.assertEquals(firstEstudiante.getCantidad_asignaturas(), form.cantidad_asignaturas.getValue());
        Assert.assertEquals(firstEstudiante.getGrupo(), form.grupo.getValue());
        Assert.assertEquals(firstEstudiante.getArea(), form.area.getValue());

        form.setEstudiante(firstEstudiante);
        form.nombre.setValue("prueba");

        AtomicReference<Estudiante> savAreaRef = new AtomicReference<>(null);
        form.addListener(EstudianteForm.SaveEvent.class, e -> {
            savAreaRef.set(e.getEstudiante());
        });
        form.save.click();

        Estudiante saveEstudiante = savAreaRef.get();

        Assert.assertEquals("prueba", saveEstudiante.getNombre());
    }

    //Delete Estudiante
    @Autowired
    EstudianteService estudianteService;
    @Test
    public void DeleteEstudianteSelected() {
        grid = estudiantesView.grid;
        Estudiante firstEstudiante = getFirtsItemToGrid(grid);
        //si el primer estudiante fue seleccionada elimninala
        grid.asSingleSelect().setValue(firstEstudiante);
        estudianteService.deleteEstudiante(firstEstudiante);
    }

}
