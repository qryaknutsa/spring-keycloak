package org.example.springkeycloak.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SecurityRequirement(name = "Keycloak")
public class HelloController {

    @GetMapping("hello")
    public String hello() {
        return "Hello World";
    }

    @GetMapping("admin")
    public String admin() {
        return "Hello World - admin";
    }

    @GetMapping("user")
    public String user() {
        return "Hello World - user";
    }

}
