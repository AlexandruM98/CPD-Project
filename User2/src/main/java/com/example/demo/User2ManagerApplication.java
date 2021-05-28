package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

@SpringBootApplication
public class User2ManagerApplication {

    @Autowired
    private static Controller controller;

    public User2ManagerApplication(Controller controller) {
        User2ManagerApplication.controller = controller;
    }

    public static void main(String[] args) throws IOException {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(User2ManagerApplication.class);
        builder.headless(false);
        ConfigurableApplicationContext context = builder.run(args);
        controller.beginCommunication();
    }
}
