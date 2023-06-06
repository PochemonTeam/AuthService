package pochemon.auth.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.grpc.ManagedChannel;
import pochemon.auth.entity.Login;
import pochemon.auth.repository.LoginRepository;
import pochemon.auth.util.JwtUtil;
import pochemon.log.LogServiceGrpc;

@Service
public class AuthService {
	
	@Value("${rpc.host}")
    private String rpcUrl;
	
	@Value("${rpc.port}")
    private String rpcPort;
	
	private final JwtUtil jwtUtil;

	private final LoginRepository loginRepository;

	private final PasswordEncoder passwordEncoder;

	private ManagedChannel channel;

	private LogServiceGrpc.LogServiceBlockingStub stub;

	@Autowired
	public AuthService(JwtUtil jwtUtil, LoginRepository loginRepository) {
		//this.channel = ManagedChannelBuilder.forAddress(this.rpcUrl, Integer.parseInt(this.rpcPort)).usePlaintext().build();
		this.jwtUtil = jwtUtil;
		this.loginRepository = loginRepository;
		this.passwordEncoder = new BCryptPasswordEncoder();
		//this.stub = LogServiceGrpc.newBlockingStub(channel);
	}

	public String generateToken(String username) {
		return jwtUtil.generateToken(username);
	}

	// Méthode de validation d'authentification fictive
	@SuppressWarnings("unused")
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
			Boolean exists = passwordEncoder.matches(password, login.getPassword());
			
//			Info request = null;
//			if (exists) {
//				//On log la tentative de connexion avec RCP
//				request = Info.newBuilder().setDate(LocalDateTime.now().toString()).setUsername(username).setSuccess(true).build();
//			} else {
//				request = Info.newBuilder().setDate(LocalDateTime.now().toString()).setUsername(username).setSuccess(false).build();
//			}
//			// Dans notre système on ne faire rien de spécial si l'authentification n'a pas
//			// été logged
//			Saved response = stub.logAuth(request);
//			@SuppressWarnings("unused")
//			boolean validation = response.getValidation();
			
			return exists;
		}
		return false;
	}

	public boolean validateToken(String token) {
		return jwtUtil.validateToken(token);
	}

    public boolean addLogin(String username, String password) {
        loginRepository.save(new Login(username, password));
        return true;
    }
}
