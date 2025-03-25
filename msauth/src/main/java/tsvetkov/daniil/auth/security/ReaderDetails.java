package tsvetkov.daniil.auth.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import tsvetkov.daniil.auth.entity.Reader;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@Getter
public final class ReaderDetails implements UserDetails {
    private final Reader reader;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return reader.getPassword();
    }

    @Override
    public String getUsername() {
        return reader.getUsername();
    }
}
