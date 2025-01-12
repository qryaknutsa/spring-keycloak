package org.example.springkeycloak.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.example.springkeycloak.service.KeycloakRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SecurityRequirement(name = "Keycloak")
public class HelloController {

    @GetMapping("hello")
    public String hello() {
        return "Hello World";
    }

    @GetMapping("admin")
    @PreAuthorize("hasRole('admin')")
    public String admin() {
        return "Hello World - admin";
    }

    @GetMapping("user")
    @PreAuthorize("hasRole('user')")
    public String user() {
        return "Hello World - user";
    }


    @Autowired
    private KeycloakRegistrationService keycloakRegistrationService;
    // ... (остальные эндпоинты)
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegistrationRequest registrationRequest) {

        //проверки
        if (registrationRequest.getUsername() == null || registrationRequest.getUsername().isBlank()
                || registrationRequest.getPassword() == null || registrationRequest.getPassword().isBlank()
                || registrationRequest.getEmail() == null || registrationRequest.getEmail().isBlank())
        {
            return ResponseEntity.badRequest().body("Username, password and email cannot be empty");
        }
        String result = keycloakRegistrationService.registerUser(
                registrationRequest.getUsername(),
                registrationRequest.getPassword(),
                registrationRequest.getEmail(),
                registrationRequest.getFirstName(),
                registrationRequest.getLastName()
        );
        return ResponseEntity.ok(result);
    }


    // Вспомогательный класс для тела запроса
    static class RegistrationRequest {
        private String username;
        private String password;
        private String email;
        private String firstName;
        private String lastName;

        // Геттеры и сеттеры
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }
    }

}
