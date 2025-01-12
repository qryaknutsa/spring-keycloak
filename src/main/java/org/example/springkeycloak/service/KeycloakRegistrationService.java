package org.example.springkeycloak.service;

import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class KeycloakRegistrationService {

    @Autowired
    @Qualifier("keycloakAdmin") // Инжектируем бин Keycloak Admin Client
    private Keycloak keycloakAdmin;
    @Autowired
    @Qualifier("keycloakClient")
    private Keycloak keycloakClient;

    @Value("${keycloak.realm}")
    private String realm;

    public String registerUser(String username, String password, String email, String firstName, String lastName) {

        // Получаем ресурс realm'а
        RealmResource realmResource = keycloakAdmin.realm(realm);
        UsersResource usersResource = realmResource.users();

        // Создаем представление пользователя
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(username);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmailVerified(false); // Можно сделать подтверждение по email

        // Устанавливаем пароль
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(password);
        credentialRepresentation.setTemporary(false);
        user.setCredentials(Collections.singletonList(credentialRepresentation));


        // Создаем пользователя в Keycloak
        Response response = usersResource.create(user);

        if (response.getStatus() == 201) {
            //  String userId = CreatedResponseUtil.getCreatedId(response);
            String location = response.getLocation().getPath();
            String userId = location.substring(location.lastIndexOf("/") + 1);
            return "User created successfully with ID: " + userId;

        } else {
            return "Error creating user: " + response.getStatusInfo().getReasonPhrase();
        }
    }
}