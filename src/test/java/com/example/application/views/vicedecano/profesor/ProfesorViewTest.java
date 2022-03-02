package com.example.application.views.vicedecano.profesor;


import com.example.application.data.entity.Estudiante;
import com.example.application.data.entity.Profesor;
import com.example.application.data.service.ProfesorService;
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
public class ProfesorViewTest {

    @Autowired
    private ProfesorView profesorView;

    private Grid<Profesor> grid;
    private ProfesorForm form;
    private Profesor firstProfesor;

    @Test
    public void formShownWhenProfesorSelected() {
        grid = profesorView.grid;
        firstProfesor = getFirtsItemToGrid(grid);
        ProfesorForm form = profesorView.form;
        Assert.assertFalse(form.isVisible());
        grid.asSingleSelect().setValue(firstProfesor);
        Assert.assertTrue(form.isVisible());
        Assert.assertEquals(firstProfesor.getNombre(), form.nombre.getValue());
        Assert.assertEquals(firstProfesor.getApellidos(), form.apellidos.getValue());
        Assert.assertEquals(firstProfesor.getUser(), form.user.getValue());
        Assert.assertEquals(firstProfesor.getEmail(), form.email.getValue());
        Assert.assertEquals(firstProfesor.getSolapin(), form.solapin.getValue());
        Assert.assertEquals(firstProfesor.getA(), form.a.getValue());
    }

    private Profesor getFirtsItemToGrid(Grid<Profesor> grid) {
        return ((ListDataProvider<Profesor>) grid.getDataProvider()).getItems().iterator().next();
    }

    //Modificar profesor
    @Test
    public void SetEstudianteSelected() {

        grid = profesorView.grid;
        firstProfesor = getFirtsItemToGrid(grid);
        form = profesorView.form;
        Assert.assertFalse(form.isVisible());
        grid.asSingleSelect().setValue(firstProfesor);
        Assert.assertTrue(form.isVisible());
        Assert.assertEquals(firstProfesor.getNombre(), form.nombre.getValue());
        Assert.assertEquals(firstProfesor.getApellidos(), form.apellidos.getValue());
        Assert.assertEquals(firstProfesor.getUser(), form.user.getValue());
        Assert.assertEquals(firstProfesor.getEmail(), form.email.getValue());
        Assert.assertEquals(firstProfesor.getSolapin(), form.solapin.getValue());
        Assert.assertEquals(firstProfesor.getA(), form.a.getValue());

        form.setProfesor(firstProfesor);
        form.nombre.setValue("prueba");

        AtomicReference<Profesor> savAreaRef = new AtomicReference<>(null);
        form.addListener(ProfesorForm.SaveEvent.class, e -> {
            savAreaRef.set(e.getProfesor());
        });
        form.save.click();

        Profesor saveProfesor = savAreaRef.get();

        Assert.assertEquals("prueba", saveProfesor.getNombre());
    }

    //Delete Estudiante
    @Autowired
    ProfesorService profesorService;
    @Test
    public void DeleteProfesorSelected() {
        grid = profesorView.grid;
        firstProfesor = getFirtsItemToGrid(grid);
        //si el primer profesor fue seleccionada elimninalo
        grid.asSingleSelect().setValue(firstProfesor);
        profesorService.deleteProfesor(firstProfesor);
    }

}
