package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

@SpringBootApplication
public class User1ManagementApplication {

    @Autowired
    public static Controller controller;

    public User1ManagementApplication(Controller controller) {
        User1ManagementApplication.controller = controller;
    }

    public static void main(String[] args) throws IOException {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(User1ManagementApplication.class);
        builder.headless(false);
        ConfigurableApplicationContext context = builder.run(args);
        controller.beginCommunication();
    }
}