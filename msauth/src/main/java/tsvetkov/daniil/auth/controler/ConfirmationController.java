package tsvetkov.daniil.auth.controler;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tsvetkov.daniil.auth.service.EmailVerificationService;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/confirm")
public class ConfirmationController {

    private final EmailVerificationService emailVerificationService;

    @PostMapping("/email")
    public ResponseEntity<String> email(@RequestBody String token){
        emailVerificationService.verificate(token);
        return ResponseEntity.ok("OK");
    }
}
