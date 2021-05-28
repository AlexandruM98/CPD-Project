package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.io.IOException;

@SpringBootApplication
public class User3ManagementApplication {

    @Autowired
    public static Controller controller;

    public User3ManagementApplication(Controller controller) {
        User3ManagementApplication.controller = controller;
    }


    public static void main(String[] args) throws IOException {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(User3ManagementApplication.class);
        builder.headless(false);
        builder.run(args);
        controller.beginCommunication();
    }
}
