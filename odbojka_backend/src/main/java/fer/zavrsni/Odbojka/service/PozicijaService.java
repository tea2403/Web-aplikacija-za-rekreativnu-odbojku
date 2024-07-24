package fer.zavrsni.Odbojka.service;

import fer.zavrsni.Odbojka.domain.Osoba;
import fer.zavrsni.Odbojka.domain.Pozicija;

import java.util.Optional;
import java.util.Set;

public interface PozicijaService {
    Optional<Pozicija> findByNazivPozicije(String poz);

    void igraPoziciju(Set<Pozicija> pozicije, Osoba igrac);
}
