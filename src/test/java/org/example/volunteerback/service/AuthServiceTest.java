package org.example.volunteerback.service;

import org.example.volunteerback.dto.UserAuthDTO;
import org.example.volunteerback.dto.response.MessageResponse;
import org.example.volunteerback.model.Role;
import org.example.volunteerback.model.user.User;
import org.example.volunteerback.repository.RoleRepository;
import org.example.volunteerback.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthService authService;

    @Test
    void register_Success() throws Exception {
        // Arrange
        UserAuthDTO request = new UserAuthDTO(
                "John", "Doe", "test@example.com",
                "password123", null, null
        );

        when(userRepository.existsByEmail(request.email())).thenReturn(false);

        Role role = new Role();
        role.setName("ROLE_USER");
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(role));

        when(passwordEncoder.encode(request.password())).thenReturn("encodedPassword");

        User savedUser = new User(request.firstName(), request.lastName(), request.email(), "encodedPassword");
        savedUser.setRoles(Set.of(role));
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        when(authenticationService.authenticateAndGenerateToken(request.email(), request.password()))
                .thenReturn("dummyJwtToken");

        // Act
        ResponseEntity<Object> response = authService.register(request);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Register successful", ((MessageResponse) Objects.requireNonNull(response.getBody())).message());
        assertEquals("dummyJwtToken", response.getHeaders().getFirst(HttpHeaders.AUTHORIZATION));
    }


    @Test
    void register_RoleNotFound_Exception() throws Exception {
        UserAuthDTO request = new UserAuthDTO(
                "John", "Doe", "test@example.com",
                "password123",null,null);

        when(userRepository.existsByEmail(request.email())).thenReturn(false);

        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> authService.register(request));
        assertEquals("Role USER not found", exception.getMessage());
    }

    @Test
    void register_EmailExists_Conflict() throws Exception {
        // Arrange
        UserAuthDTO request = new UserAuthDTO("John", "Doe", "test@example.com",
                "password123",null,null);
        when(userRepository.existsByEmail(request.email())).thenReturn(true);
        // Act
        ResponseEntity<Object> response = authService.register(request);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Email exists", ((MessageResponse) Objects
                .requireNonNull(response.getBody())).message());
    }

    @Test
    void login_Success() throws Exception {
        UserAuthDTO request = new UserAuthDTO(
                null,null, "test@example.com",
                "password123", null, null
        );
        when(userRepository.existsByEmail(request.email())).thenReturn(true);
        when(authenticationService.authenticateAndGenerateToken(request.email(),request.password()))
                .thenReturn("dummyJwtToken");

        ResponseEntity<Object> response = authService.login(request);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("dummyJwtToken",response.getHeaders().getFirst(HttpHeaders.AUTHORIZATION));
        assertEquals("Login successful", ((MessageResponse) Objects
                .requireNonNull(response.getBody())).message());
    }

    @Test
    void login_EmailOrPasswordIncorrect_BadRequest() throws Exception {
        UserAuthDTO request = new UserAuthDTO(
                null,null, "test@example.com",
                "password123", null, null
        );
        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        ResponseEntity<Object> response = authService.login(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Email or password is incorrect!", ((MessageResponse) Objects
                .requireNonNull(response.getBody())).message());
    }

    @Test
    void login_AuthenticationFails_ThrowsException() throws Exception {
        UserAuthDTO request = new UserAuthDTO(
                null, null, "test@example.com",
                "wrongpassword", null, null
        );
        when(userRepository.existsByEmail(request.email())).thenReturn(true);
        when(authenticationService.authenticateAndGenerateToken(request.email(), request.password()))
                .thenThrow(new RuntimeException("Authentication failed"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> authService.login(request));
        assertEquals("Authentication failed", exception.getMessage());
    }

}
