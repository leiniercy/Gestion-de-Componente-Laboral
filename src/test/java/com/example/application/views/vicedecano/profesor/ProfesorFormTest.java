package com.example.application.views.vicedecano.profesor;

import com.example.application.data.entity.*;
import com.example.application.views.vicedecano.estudiante.EstudianteForm;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Leinier
 **/

public class ProfesorFormTest {

    private List<Profesor> profesores;
    private List<Area> areas;
    private List<User> users;
    private Profesor profesor1;
    private Profesor profesor2;
    private User u1;
    private User u2;
    private Area a1;
    private Area a2;

    private ProfesorForm form;

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

    @Test
    public void formFieldsPopulated() {
        form = new ProfesorForm(users,areas);
        form.setProfesor(profesor1);
        Assert.assertEquals("Leinier", form.nombre.getValue());
        Assert.assertEquals("Caraballo Yanes", form.apellidos.getValue());
        Assert.assertEquals("leiniercy", form.user.getValue().getUsername());
        Assert.assertEquals("leiniercy@uci.cu", form.email.getValue());
        Assert.assertEquals("E170587", form.solapin.getValue());
        Assert.assertEquals("Area 1", form.a.getValue().getNombre());
    }
    //Añadir un Profesor
    @Test
    public void saveEventHasCorrectValues() {
        form = new ProfesorForm(users,areas);
        Profesor a = new Profesor();
        form.setProfesor(a);
        form.nombre.setValue("Perez Perez");
        form.apellidos.setValue("El Animal");
        form.user.setValue(u2);
        form.email.setValue("elegualdy@estudiantes.uci.cu");
        form.solapin.setValue("E170588");
        form.a.setValue(a2);

        AtomicReference<Profesor> saveProfesorRef = new AtomicReference<>(null);
        form.addListener(ProfesorForm.SaveEvent.class, e -> {
            saveProfesorRef.set(e.getProfesor());
        });
        form.save.click();

        Profesor saveProfesor = saveProfesorRef.get();
        Assert.assertEquals("Elegualdy", saveProfesor.getNombre());
        Assert.assertEquals("Perez Perez", saveProfesor.getApellidos());
        Assert.assertEquals("elegualdy", saveProfesor.getUser().getUsername());
        Assert.assertEquals("elegualdy@estudiantes.uci.cu", saveProfesor.getEmail());
        Assert.assertEquals("E170588", saveProfesor.getSolapin());
        Assert.assertEquals("Area 2", saveProfesor.getA().getNombre());
    }



}
