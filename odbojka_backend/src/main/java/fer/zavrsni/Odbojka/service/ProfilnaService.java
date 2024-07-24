package fer.zavrsni.Odbojka.service;

import fer.zavrsni.Odbojka.domain.Profilna;

public interface ProfilnaService {
    Profilna createProfilna(Profilna profilna);

    void izbrisi(String naziv);
}
