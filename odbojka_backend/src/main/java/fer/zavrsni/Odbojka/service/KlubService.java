package fer.zavrsni.Odbojka.service;

import fer.zavrsni.Odbojka.domain.Klub;
import fer.zavrsni.Odbojka.rest.KlubDTO;

import java.util.List;

public interface KlubService {
    void dodajKlubove(List<KlubDTO> zahtjev, String username);

    void izbriKlub(Klub kl);
}
