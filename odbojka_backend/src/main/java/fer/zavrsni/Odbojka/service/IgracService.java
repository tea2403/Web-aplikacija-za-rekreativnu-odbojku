package fer.zavrsni.Odbojka.service;

import fer.zavrsni.Odbojka.domain.Osoba;
import fer.zavrsni.Odbojka.domain.Pozicija;

import java.text.ParseException;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface IgracService {

    Optional<Osoba> findByEmail(String email);

    Map<String, String> urediIgrac(String spol, String iskustvo, String rodenje, String email) throws ParseException;

    void igraPoziciju(Set<Pozicija> pozicije, Osoba igrac);
}
