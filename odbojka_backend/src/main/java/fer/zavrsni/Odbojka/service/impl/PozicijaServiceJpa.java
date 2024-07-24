package fer.zavrsni.Odbojka.service.impl;

import fer.zavrsni.Odbojka.dao.PozicijaRepository;
import fer.zavrsni.Odbojka.domain.Osoba;
import fer.zavrsni.Odbojka.domain.Pozicija;
import fer.zavrsni.Odbojka.service.PozicijaService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class PozicijaServiceJpa implements PozicijaService {
    @Autowired
    private PozicijaRepository pozRepo;

    @Override
    @Transactional
    public Optional<Pozicija> findByNazivPozicije(String poz) {
        return pozRepo.findByNazivPozicije(poz);
    }

    @Override
    @Transactional
    public void igraPoziciju(Set<Pozicija> pozicije, Osoba igrac){
        for (Pozicija poz : igrac.getPozicije()){
            poz.getIgraci().remove(igrac);
        }
        for (Pozicija pozicija : pozicije) {
            pozicija.getIgraci().add(igrac);
            pozRepo.save(pozicija);
        }
    }
}
