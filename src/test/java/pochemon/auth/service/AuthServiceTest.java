package pochemon.auth.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import pochemon.auth.repository.LoginRepository;
import pochemon.auth.util.JwtUtil;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AuthServiceTest {
	
    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private LoginRepository loginRepository;

    @Autowired
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authService = new AuthService(jwtUtil, loginRepository);
    }

    @Test
    void testValidateToken() {
        // Mock the behavior of JwtUtil
        String token = "testToken";
        when(jwtUtil.validateToken(token)).thenReturn(true);

        // Call the validateToken method
        boolean result = authService.validateToken(token);

        // Verify the result
        Assertions.assertTrue(result);
        verify(jwtUtil).validateToken(token);
    }

    @Test
    void testAddLogin() {
        // Mock the behavior of LoginRepository
        String username = "testUser";
        String password = "testPassword";

        // Call the addLogin method
        boolean result = authService.addLogin(username, password);

        // Verify the result
        Assertions.assertTrue(result);
    }
}
