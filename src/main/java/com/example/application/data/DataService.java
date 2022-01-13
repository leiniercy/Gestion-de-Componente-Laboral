/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.application.data;

import com.example.application.data.entity.*;
import com.example.application.data.repository.*;
import java.util.List;
import org.hibernate.annotations.Formula;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
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
    private GrupoRepository grupoRepository;
    private TareaRepository tareaRepository;

    public DataService(
            @Autowired AreaRepository areaRepository,
            @Autowired EstudianteRepository estudianteRepository,
            @Autowired EvaluacionRepository evaluacionRepository,
            @Autowired GrupoRepository grupoRepository,
            @Autowired TareaRepository tareaRepository) {

        this.areaRepository = areaRepository;
        this.estudianteRepository = estudianteRepository;
        this.evaluacionRepository = evaluacionRepository;
        this.grupoRepository = grupoRepository;
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

    public List<Estudiante> searchEstudianteByNombre(String searchTerm) {
        return estudianteRepository.searchEstudianteNombre(searchTerm);
    }

    public List<Estudiante> searchEstudianteByApellidos(String searchTerm) {
        return estudianteRepository.searchEstudianteApellidos(searchTerm);
    }

    public List<Estudiante> searchEstudianteByEmail(String searchTerm) {
        return estudianteRepository.searchEstudianteEmail(searchTerm);
    }

    public List<Estudiante> searchEstudianteBySolapin(String searchTerm) {
        return estudianteRepository.searchEstudianteSolapin(searchTerm);
    }

    public List<Estudiante> searchEstudianteByAnno_repitencia(String searchTerm) {
        return estudianteRepository.searchEstudianteAnno_repitencia(searchTerm);
    }

    public List<Estudiante> searchEstudianteByCantidad_asignaturas(String searchTerm) {
        return estudianteRepository.searchEstudianteCantidad_asignaturas(searchTerm);
    }

    public List<Estudiante> searchEstudianteByArea(String searchTerm) {
        return estudianteRepository.searchEstudianteArea(searchTerm);
    }

    public List<Estudiante> searchEstudianteByGrupo(String searchTerm) {
        return estudianteRepository.searchEstudianteGrupo(searchTerm);
    }

    public long countEstudiante() {
        return estudianteRepository.count();
    }

    //Cantidiad estudiantes evaluados de Mal/Regular/Bien 
    public long countEstudianteEvaluaciones(String nota) {

        List<Evaluacion> list = evaluacionRepository.findAll();
        long cont = 0;
        switch (nota) {
            
            case "M":
                
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getNota().equals("m") || list.get(i).getNota().equals("M")) {
                        cont++;
                    }
                }
                
                break;
                
            case "R":
                 for (int i = 0; i < list.size(); i++) {
                     if (list.get(i).getNota().equals("r") || list.get(i).getNota().equals("R")) {
                     cont++;
                    }
                }
                 break;
                
            case "B":
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getNota().equals("b") || list.get(i).getNota().equals("M")) {
                        cont++;
                    }
                }
                break;

            default: cont=-1;

        }
        return cont;
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
    
    public List<Evaluacion> searchEvaluacionByTarea(String searchTerm) {
        return evaluacionRepository.searchByTarea(searchTerm);
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

    //Grupo
    public List<Grupo> findAllGrupo() {
        return grupoRepository.findAll();
    }

    public List<Grupo> searchGrupoByNumero(String searchTerm) {
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

    public List<Tarea> searchTareaByNombre(String searchTerm) {
        return tareaRepository.searchByNombre(searchTerm);
    }

    public List<Tarea> searchTareaByDescripcion(String searchTerm) {
        return tareaRepository.searchByDescripcion(searchTerm);
    }

    public List<Tarea> searchTareaByDuracion(String searchTerm) {
        return tareaRepository.searchByDuracion(searchTerm);
    }

    public List<Tarea> searchTareaByEstudiante(String searchTerm) {
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
