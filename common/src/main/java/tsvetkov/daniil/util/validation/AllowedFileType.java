package tsvetkov.daniil.util.validation;

public enum AllowedFileType {
    IMAGE("image/"),
    PDF("application/pdf");

    private final String mimeTypePrefix;

    AllowedFileType(String mimeTypePrefix) {
        this.mimeTypePrefix = mimeTypePrefix;
    }

    public String getMimeTypePrefix() {
        return mimeTypePrefix;
    }
}
