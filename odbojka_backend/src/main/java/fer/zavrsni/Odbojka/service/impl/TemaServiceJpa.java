package fer.zavrsni.Odbojka.service.impl;

import fer.zavrsni.Odbojka.dao.TemaRepository;
import fer.zavrsni.Odbojka.domain.Osoba;
import fer.zavrsni.Odbojka.domain.Tema;
import fer.zavrsni.Odbojka.service.OsobaService;
import fer.zavrsni.Odbojka.service.RequestDeniedException;
import fer.zavrsni.Odbojka.service.TemaService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TemaServiceJpa implements TemaService {
    @Autowired
    private TemaRepository temaRepo;
    @Autowired
    private OsobaService osobaSer;
    @Override
    @Transactional
    public void createTema(String naslov, String username) {
        Assert.hasText(naslov, "Naslov mora biti upisan.");
        if (temaRepo.countByNaslov(naslov) > 0)
            throw new RequestDeniedException(
                    "Tema s naslovom \"" + naslov + "\" već postoji."
            );
        Tema t = new Tema();
        t.setNaslov(naslov);
        t.setVrijeme_stvaranja(Timestamp.valueOf(LocalDateTime.now()));
        t.setOsoba(osobaSer.findByEmail(username).get());
        temaRepo.save(t);
    }

    @Override
    @Transactional
    public void izbrisiTemu(String naslov, String username, String uloga) {
        Optional<Tema> t = temaRepo.findByNaslov(naslov);
        if (t.isPresent()) {
            Tema tema = t.get();
            if (uloga.equals("admin") || tema.getOsoba().getEmail().equals(username)) {
                tema.getOsoba().getTeme().remove(tema);
                for (Osoba o : tema.getObavijesti()){
                    o.getObavijesti().remove(tema);
                }
                tema.getObavijesti().clear();
                temaRepo.delete(tema);
            }else {
                throw new RequestDeniedException(
                        "Ne možete izbrisati temu koju niste Vi stvorili."
                );
            }
        } else {
            throw new RequestDeniedException(
                    "Tema s naslovom " + naslov + " ne postoji."
            );
        }
    }

    @Override
    @Transactional
    public List<Tema> findAll() {
        return temaRepo.findAllSortedByVrijemeStvaranjaDesc();
    }

    @Override
    @Transactional
    public int countByNaslov(String naslov) {
        return temaRepo.countByNaslov(naslov);
    }

    @Override
    @Transactional
    public Optional<Tema> findByNaslov(String naslov) {
        return temaRepo.findByNaslov(naslov);
    }

    @Override
    @Transactional
    public void obavijestiU(String naslov, String username) {
        Optional<Tema> t = findByNaslov(naslov);
        if (t.isPresent()) {
            Tema tema = t.get();
            Osoba o = osobaSer.findByEmail(username).get();
            o.getObavijesti().add(tema);
            tema.getObavijesti().add(o);
            temaRepo.save(tema);
        } else {
            throw new RequestDeniedException(
                    "Tema s naslovom " + naslov + " ne postoji."
            );
        }
    }

    @Override
    @Transactional
    public void obavijestiI(String naslov, String username) {
        Optional<Tema> t = findByNaslov(naslov);
        if (t.isPresent()) {
            Tema tema = t.get();
            Osoba o = osobaSer.findByEmail(username).get();
            o.getObavijesti().remove(tema);
            tema.getObavijesti().remove(o);
            temaRepo.save(tema);
        } else {
            throw new RequestDeniedException(
                    "Tema s naslovom " + naslov + " ne postoji."
            );
        }
    }
}
