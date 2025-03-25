package tsvetkov.daniil.auth.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import tsvetkov.daniil.auth.controler.UserContoller;
import tsvetkov.daniil.auth.dto.AuthResponse;
import tsvetkov.daniil.auth.dto.LoginRequest;
import tsvetkov.daniil.auth.repository.ReaderRepository;
import tsvetkov.daniil.auth.service.AuthService;
import tsvetkov.daniil.dto.ReaderDTO;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@AutoConfigureMockMvc
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private final TestRestTemplate restTemplate;
    private final ReaderRepository readerRepository;
    private final AuthService authService;
    private final UserContoller authControler;
    private String token;


    private String baseUrl = "/api/v1";

    @Autowired
    AuthControllerTest(TestRestTemplate restTemplate, ReaderRepository readerRepository, AuthService authService, UserContoller authControler) {
        this.restTemplate = restTemplate;
        this.readerRepository = readerRepository;
        this.authService = authService;
        this.authControler = authControler;
    }

    @BeforeEach
    void setup() {
        mockMvc = standaloneSetup(authControler).build();
    }

    @BeforeEach
    void testRegister() {
        ReaderDTO reader = ReaderDTO.builder()
                .firstName("Иван")
                .email("test@example.com")
                .username("testuser")
                .password("password123")
                .build();

        HttpEntity<ReaderDTO> request = new HttpEntity<>(reader);
        ResponseEntity<AuthResponse> response = restTemplate.exchange(baseUrl + "/auth/register", HttpMethod.POST, request, AuthResponse.class);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        token = response.getBody().getAccessToken();
        assertThat(response.getBody().getReader().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void testLogin() {
        LoginRequest loginRequest = new LoginRequest("test@example.com", "password123");

        HttpEntity<LoginRequest> request = new HttpEntity<>(loginRequest);
        ResponseEntity<AuthResponse> response = restTemplate.exchange(baseUrl + "/auth/login", HttpMethod.POST, request, AuthResponse.class);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getAccessToken()).isNotBlank();
    }

    @Test
    void testUploadPhoto() throws Exception {
        // Подготовка тестового файла
        MockMultipartFile file = new MockMultipartFile(
                "file", // Имя параметра должно совпадать с @RequestParam("file")
                "test-photo.jpg",
                "image/jpeg",
                "test content".getBytes()
        );

        // Выполнение POST-запроса
        mockMvc.perform(MockMvcRequestBuilders.multipart(baseUrl + "/readers/1/upload-photo")
                        .file(file))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("File uploaded successfully"));

        // Проверка, что файл сохранён
        Path uploadDir = Path.of("uploaded-photos");
        boolean fileExists = Files.list(uploadDir)
                .anyMatch(path -> path.getFileName().toString().startsWith("2") && path.getFileName().toString().endsWith("test-photo.jpg"));
        assertTrue(fileExists, "Файл должен быть сохранён в директории uploaded-photos");
    }
}



