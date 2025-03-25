package tsvetkov.daniil.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tsvetkov.daniil.auth.dto.AuthResponse;
import tsvetkov.daniil.auth.entity.Reader;
import tsvetkov.daniil.util.mapper.Mapper;
import tsvetkov.daniil.auth.security.JwtUtil;
import tsvetkov.daniil.auth.security.ReaderDetails;
import tsvetkov.daniil.auth.security.ReaderDetailsProvider;
import tsvetkov.daniil.dto.ReaderDTO;
import tsvetkov.daniil.auth.exception.EmailAlreadyTakenException;
import tsvetkov.daniil.auth.exception.EmailNotVerifiedException;
import tsvetkov.daniil.exception.InvalidTokenException;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final ReaderService readerService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailVerificationService emailVerificationService;
    private final JwtUtil jwtUtil;
    private final ReaderDetailsProvider userDetailsService;
    private final Mapper mapper;

    @Transactional
    public ReaderDTO register(ReaderDTO readerDTO) {
        Optional<Reader> existingReader = readerService.findByEmail(readerDTO.getEmail());
        if (existingReader.isPresent()) {
            if (existingReader.get().isActive()) {
                throw new EmailAlreadyTakenException();
            } else {
                readerService.deleteById(existingReader.get().getId());
            }
        }

        Reader reader = mapper.map(readerDTO, Reader.class);
        reader.setPassword(passwordEncoder.encode(reader.getPassword()));

        Reader savedReader = readerService.save(reader);
        emailVerificationService.sendConfirmation(savedReader.getEmail());

        return mapper.map(savedReader, ReaderDTO.class);
    }

    @Transactional
    public AuthResponse login(String email, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        ReaderDetails userDetails = userDetailsService.loadUserByUsername(email);

        if (!userDetails.getReader().isActive()) {
            throw new EmailNotVerifiedException();
        }

        String accessToken = jwtUtil.generateAccessToken(userDetails);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);
        ReaderDTO readerDTO = mapper.map(userDetails.getReader(), ReaderDTO.class);

        return new AuthResponse(accessToken, refreshToken, readerDTO);
    }

    @Transactional(readOnly = true)
    public AuthResponse refreshToken(String authorizationHeader) {
        String refreshToken = extractTokenFromHeader(authorizationHeader);
        if (refreshToken == null) {
            throw new InvalidTokenException();
        }

        String username = jwtUtil.extractUsername(refreshToken);
        ReaderDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (!jwtUtil.validateRefreshToken(refreshToken, userDetails)) {
            throw new InvalidTokenException();
        }

        String newAccessToken = jwtUtil.generateAccessToken(userDetails);
        String newRefreshToken = jwtUtil.generateRefreshToken(userDetails);
        ReaderDTO readerDTO = mapper.map(userDetails.getReader(), ReaderDTO.class);

        return new AuthResponse(newAccessToken, newRefreshToken, readerDTO);
    }

    private String extractTokenFromHeader(String header) {
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}
