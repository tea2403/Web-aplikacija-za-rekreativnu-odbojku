package fer.zavrsni.Odbojka.service.impl;

import fer.zavrsni.Odbojka.dao.TreniraoRepository;
import fer.zavrsni.Odbojka.domain.*;
import fer.zavrsni.Odbojka.service.RequestDeniedException;
import fer.zavrsni.Odbojka.service.TreniraoService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class TreniraoServiceJpa implements TreniraoService {
    @Autowired
    private TreniraoRepository trRepo;
    @Override
    @Transactional
    public void createTrenirao(TreniraoKljuc tr, String od_godina, String do_godina, Osoba osoba, Klub klub) {
        if (!od_godina.isEmpty() && !do_godina.isEmpty() && Integer.parseInt(od_godina) > Integer.parseInt(do_godina)) {
            throw new RequestDeniedException(
                    "Početak treniranja mora biti prije kraja treniranja."
            );
        }
        if (!od_godina.isEmpty() && (Integer.parseInt(od_godina) < (LocalDate.now().getYear()-100) || Integer.parseInt(od_godina) > LocalDate.now().getYear())){
            throw new RequestDeniedException(
                    "Godina početka treniranja nije u ispravnom formatu."
            );
        }
        if (!do_godina.isEmpty() && (Integer.parseInt(do_godina) < (LocalDate.now().getYear()-100) || Integer.parseInt(do_godina) > LocalDate.now().getYear())){
            throw new RequestDeniedException(
                    "Godina kraja treniranja nije u ispravnom formatu."
            );
        }
        TreniraoKljuc klj = new TreniraoKljuc();
        klj.setŠifra_kluba(klub.getŠifra_kluba());
        klj.setŠifra_osobe(osoba.getŠifra_osobe());
        Optional<Trenirao> unos = trRepo.findById(klj);
        Trenirao tren = new Trenirao();
        if (unos.isPresent()){
            tren = unos.get();
        } else {
            tren.setId(tr);
            osoba.getTreniranja().add(tren);
            klub.getTreniranja().add(tren);
            tren.setIgrac(osoba);
            tren.setKlub(klub);
        }
        if(!do_godina.isEmpty()) tren.setDo_godina(Integer.valueOf(do_godina));
        else tren.setDo_godina(null);
        if(!od_godina.isEmpty()) tren.setOd_godina(Integer.valueOf(od_godina));
        else tren.setOd_godina(null);
    }

    @Override
    @Transactional
    public void izbrisiTrenirao(Trenirao tr, Osoba igrac, Klub kl) {
        igrac.getTreniranja().remove(tr);
        kl.getTreniranja().remove(tr);
        trRepo.delete(tr);
    }
}
