/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.application.data;

import com.example.application.data.entity.*;
import com.example.application.data.repository.*;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Leinier
 */
@Service
public class DataService {

    private AreaRepository areaRepository;
    private EstudianteRepository estudianteRepository;
    private EvaluacionRepository evaluacionRepository;
    private PersonRepository personRepository;
    private ProfesorRepository profesorRepository;
    private GrupoRepository grupoRepository;
    private TareaRepository tareaRepository;

    public DataService(
            @Autowired AreaRepository areaRepository,
            @Autowired EstudianteRepository estudianteRepository,
            @Autowired EvaluacionRepository evaluacionRepository,
            @Autowired PersonRepository personRepository,
            @Autowired ProfesorRepository profesorRepository,
            @Autowired GrupoRepository samplePersonRepository,
            @Autowired TareaRepository tareaRepository) {

        this.areaRepository = areaRepository;
        this.estudianteRepository = estudianteRepository;
        this.evaluacionRepository = evaluacionRepository;
        this.personRepository = personRepository;
        this.profesorRepository = profesorRepository;
        this.grupoRepository = samplePersonRepository;
        this.tareaRepository = tareaRepository;
    }

    //Area
    public List<Area> findAllArea() {
        return areaRepository.findAll();
    }
    
    public List<Area> searchArea(String stringFilter) {
        return areaRepository.search(stringFilter);
    }
    
    public List<Area> searchAreaByName(String stringFilter) {
        return areaRepository.searchByNombre(stringFilter);
    }
    
    public List<Area> searchAreaByDescripcion(String stringFilter) {
        return areaRepository.searchByDescripcion(stringFilter);
    }

    public long countArea() {
        return areaRepository.count();
    }

    public void deleteArea(Area area) {
        areaRepository.delete(area);
    }

    public void saveArea(Area area) {
        if (area == null) {
            System.err.println("This field is null. Are you sure you have connected your form to the application?");
            return;
        }
        areaRepository.save(area);
    }

    //Estudiante
    public List<Estudiante> findAllEstudiante() {
        return estudianteRepository.findAll();
    }

    public List<Person> searchEstudianteByNombre(String searchTerm) {
        return personRepository.searchEstudianteNombre(searchTerm);
    }
    
    public List<Person> searchEstudianteByApellidos(String searchTerm) {
        return personRepository.searchEstudianteApellidos(searchTerm);
    }
    
    public List<Person> searchEstudianteByEmail(String searchTerm) {
        return personRepository.searchEstudianteEmail(searchTerm);
    }
    
    public List<Person> searchEstudianteBySolapin(String searchTerm) {
        return personRepository.searchEstudianteSolapin(searchTerm);
    }
    
    public List<Person> searchEstudianteByAnno_repitencia(String searchTerm) {
        return personRepository.searchEstudianteAnno_repitencia(searchTerm);
    }
    
    public List<Person> searchEstudianteByCantidad_asignaturas(String searchTerm) {
        return personRepository.searchEstudianteCantidad_asignaturas(searchTerm);
    }
    
    public List<Person> searchEstudianteByArea(String searchTerm) {
        return personRepository.searchEstudianteArea(searchTerm);
    }
    
    public List<Person> searchEstudianteByGrupo(String searchTerm) {
        return personRepository.searchEstudianteGrupo(searchTerm);
    }

    public long countEstudiante() {
        return estudianteRepository.count();
    }

    public void deleteEstudiante(Estudiante estudiante) {
        estudianteRepository.delete(estudiante);
    }

    public void saveEstudiante(Estudiante estudiante) {
        if (estudiante == null) {
            System.err.println("This field is null. Are you sure you have connected your form to the application?");
            return;
        }
        estudianteRepository.save(estudiante);
    }

    //Evaluacion
    public List<Evaluacion> findAllEvaluacion() {
        return evaluacionRepository.findAll();
    }
    
     public List<Evaluacion> searchEvaluacionByNota(String searchTerm) {
        return evaluacionRepository.searchByNota(searchTerm);
    }
     
     public List<Evaluacion> searchEvaluacionByDescripcion(String searchTerm) {
        return evaluacionRepository.searchByDescripcion(searchTerm);
    }
     
     public List<Evaluacion> searchEvaluacionByEstudiante(String searchTerm) {
        return evaluacionRepository.searchByEstudiante(searchTerm);
    }

    public long countEvaluacion() {
        return evaluacionRepository.count();
    }

    public void deleteEvaluacion(Evaluacion evaluacion) {
        evaluacionRepository.delete(evaluacion);
    }

    public void saveEvaluacion(Evaluacion evaluacion) {
        if (evaluacion == null) {
            System.err.println("This field is null. Are you sure you have connected your form to the application?");
            return;
        }
        evaluacionRepository.save(evaluacion);
    }

    //Person
    public List<Person> findAllPerson() {
        return personRepository.findAll();
    }

    public long countPerson() {
        return personRepository.count();
    }

    public void deletePerson(Person person) {
        personRepository.delete(person);
    }

    public void savePerson(Person person) {
        if (person == null) {
            System.err.println("This field is null. Are you sure you have connected your form to the application?");
            return;
        }
        personRepository.save(person);
    }

    //Profesor
    public List<Profesor> findAllProfesor() {
        return profesorRepository.findAll();
    }
    
    public List<Person> searchProfesorByNombre(String searchTerm) {
        return personRepository.searchProfesorNombre(searchTerm);
    }
    
    public List<Person> searchProfesorByApellidos(String searchTerm) {
        return personRepository.searchProfesorApellidos(searchTerm);
    }
    
    public List<Person> searchProfesorByEmail(String searchTerm) {
        return personRepository.searchEstudianteEmail(searchTerm);
    }
    
    public List<Person> searchProfesorBySolapin(String searchTerm) {
        return personRepository.searchEstudianteSolapin(searchTerm);
    }
       
    public List<Person> searchProfesorByJefe_area(String searchTerm) {
        return personRepository.searchProfesorJefe_area(searchTerm);
    }
    
    public List<Person> searchProfesorByArea(String searchTerm) {
        return personRepository.searchProfesorArea(searchTerm);
    }
    

    public long countProfesor() {
        return profesorRepository.count();
    }

    public void deleteProfesor(Profesor profesor) {
        profesorRepository.delete(profesor);
    }

    public void saveProfesor(Profesor profesor) {
        if (profesor == null) {
            System.err.println("This field is null. Are you sure you have connected your form to the application?");
            return;
        }
        profesorRepository.save(profesor);
    }

    //Grupo
    public List<Grupo> findAllGrupo() {
        return grupoRepository.findAll();
    }
      
    public List<Grupo> searchGrupoByNumero (String searchTerm) {
        return grupoRepository.searchByNumero(searchTerm);
    }
    
    public long countGrupo() {
        return grupoRepository.count();
    }

    public void deleteGrupo(Grupo grupo) {
        grupoRepository.delete(grupo);
    }

    public void saveGrupo(Grupo grupo) {
        if (grupo == null) {
            System.err.println("This field is null. Are you sure you have connected your form to the application?");
            return;
        }
        grupoRepository.save(grupo);
    }

    //Tarea
    public List<Tarea> findAllTareas() {
        return tareaRepository.findAll();
    }
    
    public List<Tarea> searchTareaByNombre (String searchTerm) {
        return tareaRepository.searchByNombre(searchTerm);
    }
    
    public List<Tarea> searchTareaByDescripcion (String searchTerm) {
        return tareaRepository.searchByDescripcion(searchTerm);
    }
    
    public List<Tarea> searchTareaByDuracion (String searchTerm) {
        return tareaRepository.searchByDuracion(searchTerm);
    }
    
    public List<Tarea> searchTareaByEstudiante (String searchTerm) {
        return tareaRepository.searchByEstudiante(searchTerm);
    }

    public long countTarea() {
        return tareaRepository.count();
    }

    public void deleteTarea(Tarea tarea) {
        tareaRepository.delete(tarea);
    }

    public void saveTarea(Tarea tarea) {
        if (tarea == null) {
            System.err.println("This field is null. Are you sure you have connected your form to the application?");
            return;
        }
        tareaRepository.save(tarea);
    }

}
