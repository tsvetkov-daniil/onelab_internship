package tsvetkov.daniil.auth.exception;

public class RoleNotFoundException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Роль не найдена";

    public RoleNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public RoleNotFoundException(String message) {
        super(message);
    }
}
