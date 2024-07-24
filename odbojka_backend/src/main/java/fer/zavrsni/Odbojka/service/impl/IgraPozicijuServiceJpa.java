package fer.zavrsni.Odbojka.service.impl;

import fer.zavrsni.Odbojka.domain.Osoba;
import fer.zavrsni.Odbojka.domain.Pozicija;
import fer.zavrsni.Odbojka.service.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class IgraPozicijuServiceJpa implements IgraPozicijuService {
    @Autowired
    private OsobaService osobaSer;
    @Autowired
    private IgracService igracSer;
    @Autowired
    private PozicijaService pozSer;
    @Override
    @Transactional
    public void igraPoziciju(List<String> pozicije, String username){
        Optional<Osoba> igrac = osobaSer.findByEmail(username);
        Set<Pozicija> rez = new HashSet<>();
        for (String pozicija : pozicije) {
            Optional<Pozicija> poz2 = pozSer.findByNazivPozicije(pozicija);
            if (poz2.isPresent()) {
                rez.add(poz2.get());
            } else {
                throw new RequestDeniedException(
                        "Pozicija ne postoji."
                );
            }
        }
        igracSer.igraPoziciju(rez, igrac.get());
    }
}
