package fer.zavrsni.Odbojka.service.impl;


import fer.zavrsni.Odbojka.dao.ProfilnaRepository;
import fer.zavrsni.Odbojka.domain.Profilna;
import fer.zavrsni.Odbojka.service.ProfilnaService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfilnaServiceJpa implements ProfilnaService {
    @Autowired
    private ProfilnaRepository profilnaRepo;
    @Override
    @Transactional
    public Profilna createProfilna(Profilna profilna) {
        return profilnaRepo.save(profilna);
    }

    @Override
    @Transactional
    public void izbrisi(String naziv) {
        Optional<Profilna> slika = profilnaRepo.findByImeProfilne(naziv);
        if (slika.isPresent()) {
            slika.get().izbrisi(naziv, "profilne");
            profilnaRepo.deleteByImeProfilne(naziv);
        }
    }
}
