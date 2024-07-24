package fer.zavrsni.Odbojka.rest;

import fer.zavrsni.Odbojka.domain.Osoba;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import fer.zavrsni.Odbojka.service.OsobaService;

import static org.springframework.security.core.authority.AuthorityUtils.commaSeparatedStringToAuthorityList;

@Service
public class KorisnikUserDetailsService implements UserDetailsService {

    @Autowired
    private OsobaService osobaService;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Osoba korisnik = osobaService.findByEmail(username.toLowerCase()).orElseThrow(
                () -> new UsernameNotFoundException("No user '" + username + "'")
        );
        if ("admin@email.com".equalsIgnoreCase(username)) {
            return new User(
                    username.toLowerCase(),
                    korisnik.getLozinka(),
                    commaSeparatedStringToAuthorityList("admin")
            );}

        return new User(
                korisnik.getEmail(),
                korisnik.getLozinka(),
                commaSeparatedStringToAuthorityList("korisnik")
        );
    }
}

