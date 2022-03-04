package com.example.application.views.vicedecano.profesor;



import com.example.application.data.entity.Area;
import com.example.application.data.entity.Profesor;
import com.example.application.data.entity.User;
import com.example.application.data.service.ProfesorService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.ListDataProvider;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
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

    private List<Profesor> profesores;
    private List<Area> areas;
    private List<User> users;
    private Profesor profesor1;
    private Profesor profesor2;
    private User u1;
    private User u2;
    private Area a1;
    private Area a2;


    @Before
    public void setupData() {

        //Profesor 1
        profesores = new ArrayList<>();
        areas = new ArrayList<>();
        users = new ArrayList<>();

        profesor1 = new Profesor();
        profesor1.setNombre("Leinier");
        profesor1.setApellidos("Caraballo Yanes");

        u1 = new User();
        u1.setName("Leinier");
        u1.setUsername("leiniercy");
        u1.setPassword("1234");
        users.add(u1);

        profesor1.setUser(u1);
        profesor1.setEmail("leiniercy@uci.cu");
        profesor1.setSolapin("E170587");

        a1 = new Area();
        a1.setNombre("Area 1");
        a1.setDescripcion("Mi Area1");
        areas.add(a1);

        profesor1.setA(a1);
        profesores.add(profesor1);


        //Estudiante 2
        profesor2 = new Profesor();
        profesor2.setNombre("Elegualdy");
        profesor2.setApellidos("Perez Perez");

        u2 = new User();
        u2.setName("Elegualdy");
        u2.setUsername("elegualdy");
        u2.setPassword("1234");
        users.add(u2);

        profesor2.setUser(u2);
        profesor2.setEmail("elegualdy@uci.cu");
        profesor2.setSolapin("E170588");

        a2 = new Area();
        a2.setNombre("Area 2");
        a2.setDescripcion("Mi Area2");
        areas.add(a2);

        profesor2.setA(a2);

    }


    //Modificar profesor
    @Test
    public void SetEstudianteSelected() {

        grid = new Grid<>(Profesor.class);
        grid.setItems(profesores);
        form = new ProfesorForm(users,areas);
        //seleccionar profesor 1
        grid.asSingleSelect().setValue(profesor1);

        form.setProfesor(profesor1);
        form.nombre.setValue("prueba");

        AtomicReference<Profesor> saveProfesorRef = new AtomicReference<>(null);
        form.addListener(ProfesorForm.SaveEvent.class, e -> {
            saveProfesorRef.set(e.getProfesor());
        });
        form.save.click();

        Profesor saveProfesor = saveProfesorRef.get();

        Assert.assertEquals("prueba", saveProfesor.getNombre());
    }

    //Delete Estudiante

    @Test
    public void DeleteProfesorSelected() {
        grid = new Grid<>(Profesor.class);
        grid.setItems(profesores);
        form = new ProfesorForm(users,areas);

        grid.asSingleSelect().setValue(profesor1);

        AtomicReference<Profesor> deleteProfesorRef = new AtomicReference<>(null);
        form.addListener(ProfesorForm.DeleteEvent.class, e -> {
            deleteProfesorRef.set(e.getProfesor());
        });

    }

}
