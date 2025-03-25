package tsvetkov.daniil.auth.security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tsvetkov.daniil.auth.entity.Reader;
import tsvetkov.daniil.auth.service.ReaderService;

@AllArgsConstructor
@Service
public class ReaderDetailsProvider implements UserDetailsService {

    private final ReaderService readerService;

    @Override
    public ReaderDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Reader reader = readerService.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("Пользователь с ником: " + username + " не найден"));
        return new ReaderDetails(reader);
    }
}
