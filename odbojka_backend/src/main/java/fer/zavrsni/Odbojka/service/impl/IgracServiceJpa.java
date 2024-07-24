package fer.zavrsni.Odbojka.service.impl;

import fer.zavrsni.Odbojka.dao.OsobaRepository;
import fer.zavrsni.Odbojka.domain.*;
import fer.zavrsni.Odbojka.service.IgracService;
import fer.zavrsni.Odbojka.service.OsobaService;
import fer.zavrsni.Odbojka.service.RequestDeniedException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@Service
public class IgracServiceJpa implements IgracService {
    @Autowired
    private OsobaRepository osobaRepo;
    @Autowired
    private OsobaService osobaSer;

    @Override
    @Transactional
    public Optional<Osoba> findByEmail(String email) {
        return osobaRepo.findByEmail(email);
    }
    @Override
    @Transactional
    public Map<String, String> urediIgrac(String spol, String iskustvo, String rodenje, String email) throws ParseException {
        Optional<Osoba> igrac = osobaSer.findByEmail(email);
        Map<String, String> rezultat = new HashMap<>();
        if (igrac.isPresent()) {
            if (!(spol.isEmpty() || spol.equals("M") || spol.equals("Ž") || spol.equals("Ostalo"))){
                throw new RequestDeniedException(
                        "Spol nije u ispravnom formatu."
                );
            }
            Osoba igrac1 = igrac.get();
            if (spol.isEmpty()) spol = null;
            igrac1.setSpol(spol);
            rezultat.put("spol", spol);

            if (!iskustvo.isEmpty() && (Integer.parseInt(iskustvo) < (LocalDate.now().getYear()-100) || Integer.parseInt(iskustvo) > LocalDate.now().getYear())){
                throw new RequestDeniedException(
                        "Godina početka igranja nije u ispravnom formatu."
                );
            }
            if (!iskustvo.isEmpty()) igrac1.setPočetak_igranja(Integer.valueOf(iskustvo));
            else {
                iskustvo = null;
                igrac1.setPočetak_igranja(null);
            }
            rezultat.put("iskustvo", iskustvo);

            if (rodenje.isEmpty()){
                igrac1.setDatum_rođenja(null);
                rodenje = null;
            } else {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Calendar cal = Calendar.getInstance();
                cal.setTime(format.parse(rodenje));
                Calendar sto = Calendar.getInstance();
                sto.add(Calendar.YEAR, -100);
                if (!cal.before(Calendar.getInstance()) || cal.before(sto)){
                    throw new RequestDeniedException(
                            "Datum rođenja nije u ispravnom formatu."
                    );
                }
                igrac1.setDatum_rođenja(format.parse(rodenje));
            }
            rezultat.put("rodenje", rodenje);
            osobaRepo.save(igrac1);
        }
        return rezultat;
    }

    @Override
    @Transactional
    public void igraPoziciju(Set<Pozicija> pozicije, Osoba igrac){
        for (Pozicija p : igrac.getPozicije()){
            p.getIgraci().remove(igrac);
        }
        igrac.getPozicije().clear();
        for (Pozicija p : pozicije){
            igrac.getPozicije().add(p);
            p.getIgraci().add(igrac);
        }
        osobaRepo.save(igrac);
    }
}
