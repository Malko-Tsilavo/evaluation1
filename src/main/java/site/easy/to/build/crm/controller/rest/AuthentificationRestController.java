package site.easy.to.build.crm.controller.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import site.easy.to.build.crm.entity.User;
import site.easy.to.build.crm.util.AuthenticationUtils;

@RestController
@CrossOrigin
public class AuthentificationRestController {

    private final AuthenticationUtils authenticationUtils;

    public AuthentificationRestController(AuthenticationUtils authenticationUtils) {
        this.authenticationUtils = authenticationUtils;
    }

    @PostMapping("/api/authenticate")
    public ResponseEntity<?> authenticateUser(
            @RequestParam String email,
            @RequestParam String password) {

        // Authentifier l'utilisateur
        User user = authenticationUtils.authenticate(email, password);


        if (user != null) {
            // Si l'authentification réussit, retourner l'utilisateur sous forme JSON
            return ResponseEntity.ok(user);
        } else {
            // Si l'authentification échoue, retourner une réponse avec un statut d'erreur
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
}