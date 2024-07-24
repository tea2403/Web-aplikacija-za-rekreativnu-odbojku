package fer.zavrsni.Odbojka.service;

import fer.zavrsni.Odbojka.domain.Teren;
import fer.zavrsni.Odbojka.domain.Termin;
import fer.zavrsni.Odbojka.rest.TerminDTO;

import java.util.List;
import java.util.Optional;

public interface TerminService {
    boolean createTermin(Teren teren, List<TerminDTO> termini, String username, String uloga);

    List<Termin> findAllGrad(Long aLong);

    Optional<Termin> findByŠifraTermina(Long aLong);

    void izbrisiRezervaciju(Long aLong, Long tim, String username);
}
