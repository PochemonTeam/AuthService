package pochemon.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pochemon.auth.entity.Login;
import pochemon.auth.repository.LoginRepository;
import pochemon.auth.util.JwtUtil;
import pochemon.service.UserWebService;

import java.util.Optional;

@Service
public class AuthService {
    private final JwtUtil jwtUtil;

    private final LoginRepository loginRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserWebService userWebService;


    @Autowired
    public AuthService(JwtUtil jwtUtil, LoginRepository loginRepository) {
        this.jwtUtil = jwtUtil;
        this.loginRepository = loginRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.userWebService = new UserWebService();
    }

    public String generateToken(String username) {
        return jwtUtil.generateToken(username);
    }

    // Méthode de validation d'authentification fictive
    private boolean fakeAuthenticate(String username, String password) {
        // méthode temporaire pour valider une connexion
        return "admin".equals(username) && "password".equals(password);
    }

    public boolean authenticate(String username, String password) {
        // Rechercher l'utilisateur dans la base de données
        Optional<Login> optionalLogin = loginRepository.findLoginByUsername(username);
        if (optionalLogin.isPresent()) {
            Login login = optionalLogin.get();
            // Vérifier le mot de passe en utilisant le PasswordEncoder
            return passwordEncoder.matches(password, login.getPassword());
        }
        return false;
    }

    public boolean validateToken(String token) {
        return jwtUtil.validateToken(token);
    }
}
