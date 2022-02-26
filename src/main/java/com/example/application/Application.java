package com.example.application;

import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.event.EventListener;

import com.example.application.data.repository.*;
import com.example.application.data.entity.*;
import com.example.application.data.entity.*;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * The entry point of the Spring Boot application.
 *
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 *
 */
@SpringBootApplication
@Theme(value = "myapp")
@PWA(name = "My App", shortName = "My App", offlineResources = {"images/logo.png"})
@NpmPackage(value = "line-awesome", version = "1.3.0")
public class Application extends SpringBootServletInitializer implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        System.setProperty("spring.devtools.restart.enabled", "true");
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @EventListener(ApplicationReadyEvent.class)
    public void fillDB() {
        Logger logger = LoggerFactory.getLogger(getClass());
        if (userRepository.count() != 0L) {
            logger.info("Using existing database");
            return;
        }

        logger.info("Generating demo data");
        logger.info("... generating 21 Users entities...");

        //Estudiantes
        createUser2("Alexander", "alexanderrs", "Alexanderrs.1234"); //1
        createUser2("Addlin", "addlindlcvm", "Addlindlcvm.1234"); //2
        createUser2("Abel Ernesto", "abelega", "Abelega.1234"); //3
        createUser2("Amelia", "ameliafb", "Ameliafb.1234"); //4 
        createUser2("Adrianys", "adrianyspl", "Adrianyspl.1234"); //5
        createUser2("José Luis", "jldenis", "Jldenis.1234"); //6
        createUser2("Angel Luis", "alfumero", "Alfumero.1234"); //7
        createUser2("Alejandro", "alejsosa", "Alejsosa.1234"); //8
        createUser2("Arian", "arianlb", "Arianlb.1234"); //9
        createUser2("Adnarim De La Caridad", "adnarimdlctp", "Adnarimdlctp.1234");//10
        createUser2("Alejandro", "alejandrorv", "Alejandrorv.1234");//11
        createUser2("Adrian", "adriansr", "Adriansr.1234");//12
        createUser2("Adrian", "adriancs", "Adriancs.1234");//13 
        createUser2("Alejandro", "alejandromb", "Alejandromb.1234"); //14
        createUser2("Andrea", "andreasrm", "Andreasrm.1234"); //15
        createUser2("Alejandro", "alejandrodmt", "Alejandrodmt.1234"); //16

          //Jefe de Área
        createUser3("Osés Rico", "aoses", "Aoses.1234");//17
        createUser3("Arronte Fernández", "aaaf", "Aaaf.1234");//18
        createUser3("Borjas Mir", "aborjas", "Aborjas.1234");//19
        createUser3("Yordankis", "yluguen", "Yluguen.1234");//20
        
        //Vicedecano
        createUser4("Yadira", "yramirezr", "Yramirezr.1234");//21
    }

    private User createUser1(String name, String username, String password) {
        User user = new User();
        user.setName(name);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(Stream.of(Rol.ESTUDIANTE, Rol.JEFE_AREA, Rol.VICEDECANO, Rol.ADMIN).collect(Collectors.toSet()));
        userRepository.saveAndFlush(user);
        return user;
    }

    private User createUser2(String name, String username, String password) {
        User user = new User();
        user.setName(name);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(Collections.singleton(Rol.ESTUDIANTE));
        userRepository.saveAndFlush(user);
        return user;
    }

    private User createUser3(String name, String username, String password) {
        User user = new User();
        user.setName(name);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(Collections.singleton(Rol.JEFE_AREA));
        userRepository.saveAndFlush(user);
        return user;
    }

    private User createUser4(String name, String username, String password) {
        User user = new User();
        user.setName(name);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(Collections.singleton(Rol.VICEDECANO));
        userRepository.saveAndFlush(user);
        return user;
    }

}
