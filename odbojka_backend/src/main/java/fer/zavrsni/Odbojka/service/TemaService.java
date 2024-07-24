package fer.zavrsni.Odbojka.service;

import fer.zavrsni.Odbojka.domain.Tema;

import java.util.List;
import java.util.Optional;

public interface TemaService {
    void createTema(String tema, String username);

    void izbrisiTemu(String lowerCase, String username, String uloga);

    List<Tema> findAll();

    int countByNaslov(String naslov);

    Optional<Tema> findByNaslov(String naslov);

    void obavijestiU(String naslov, String username);

    void obavijestiI(String naslov, String username);

}
