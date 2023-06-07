package pochemon.auth.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import pochemon.auth.service.AuthService;
import pochemon.dto.AuthDTO;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public boolean addLogin(@RequestBody AuthDTO loginRequest) {
        log.info("AuthController : New register request for {}", loginRequest.getUsername());
        return authService.addLogin(loginRequest.getUsername(), loginRequest.getPassword());
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthDTO loginRequest) {
        log.info("AuthController : New login request for {}", loginRequest.getUsername());
        // Vérifier les informations d'identification de l'utilisateur
        if (authService.authenticate(loginRequest.getUsername(), loginRequest.getPassword())) {
            // Générer un token JWT
            String token = authService.generateToken(loginRequest.getUsername());
            // Retourner le token dans la réponse
            return ResponseEntity.ok(token);
        } else {
            // Les informations d'identification sont incorrectes
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Identifiants invalides");
        }
    }

    @PostMapping
    @GetMapping
    public ResponseEntity<Boolean> authenticate(@RequestHeader("Authorization") String token) {
        if(authService.validateToken(token)) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
