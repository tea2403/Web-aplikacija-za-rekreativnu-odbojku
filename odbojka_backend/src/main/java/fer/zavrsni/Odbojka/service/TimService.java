package fer.zavrsni.Odbojka.service;

import fer.zavrsni.Odbojka.domain.Tim;

import java.util.Set;

public interface TimService {
    void createTim(String naziv, String username);

    Set<Tim> findAll();

    void dodajIgraca(String naziv, String igrac, String username, String uloga);

    void izbrisiIgraca(String naziv, String igrac, String username, String uloga);

    void rezerviraj(Long termin, Long tim, String username);
}
