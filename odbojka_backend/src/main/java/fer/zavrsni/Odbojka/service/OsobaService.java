package fer.zavrsni.Odbojka.service;

import fer.zavrsni.Odbojka.domain.Osoba;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Optional;

public interface OsobaService {

    Osoba createOsoba(Osoba osoba);
    Optional<Osoba> findByEmail(String email);

    String ime(String email);

    String prezime(String email);

    void spremi(Osoba o);

    Map<String, String> urediOsoba(String ime, String prezime, String broj, MultipartFile profilna, String brisi, String pbr, String email);
}
