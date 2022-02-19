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
        logger.info("... generating Users entities...");

        createUser1("leiniercy", "admin", "Lol");

        createUser2("User", "user", "user");
        createUser2("Alexander", "alexanderrs", "alexanderrs");
        createUser2("Amelia", "ameliafb", "ameliafb");
        createUser2("Arlín", "arlinvc", "arlinvc");
        createUser2("Christian", "christiansj", "christiansj");
        createUser2("Félix A.", "felixacg", "felixacg");

        createUser3("JefeArea", "jefeArea", "jefeArea");
        createUser3("Yordankis", "yluguen", "yluguen");

        createUser4("Vicedecano", "vicedecano", "vicedecano");
        createUser4("Yadira", "yramirezr", "yramirezr");
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
