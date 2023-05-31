package pochemon.auth.controller;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pochemon.auth.service.AuthService;
import pochemon.dto.AuthDTO;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthDTO loginRequest) {
        // Vérifier les informations d'identification de l'utilisateur
        if (authService.authenticate(loginRequest.getUsername(), loginRequest.getPassword())) {
            // Générer un token JWT
            String token = authService.generateToken(loginRequest.getUsername());
            // Retourner le token dans la réponse
            return ResponseEntity.ok(token);
        } else {
            // Les informations d'identification sont incorrectes
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Identifiants invalides");
        }
    }

    @PostMapping("/verify")
    public boolean validateToken(String token) {
        return authService.validateToken(token);
    }

}
