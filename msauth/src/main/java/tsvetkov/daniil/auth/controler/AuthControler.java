package tsvetkov.daniil.auth.controler;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tsvetkov.daniil.auth.dto.AuthResponse;
import tsvetkov.daniil.auth.dto.LoginRequest;
import tsvetkov.daniil.auth.service.AuthService;
import tsvetkov.daniil.dto.ReaderDTO;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthControler {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ReaderDTO> register(@RequestBody @Valid ReaderDTO readerDTO) {
        return ResponseEntity.ok(authService.register(readerDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest.getUsername(), loginRequest.getPassword()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody String refreshToken) throws Exception {
        return ResponseEntity.ok(authService.refreshToken(refreshToken));
    }


}
