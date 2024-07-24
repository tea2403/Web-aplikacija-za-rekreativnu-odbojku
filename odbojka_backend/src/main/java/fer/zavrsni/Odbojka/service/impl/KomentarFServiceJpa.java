package fer.zavrsni.Odbojka.service.impl;

import fer.zavrsni.Odbojka.dao.KomentarFRepository;
import fer.zavrsni.Odbojka.domain.KomentarF;
import fer.zavrsni.Odbojka.domain.Tema;
import fer.zavrsni.Odbojka.service.KomentarFService;
import fer.zavrsni.Odbojka.service.OsobaService;
import fer.zavrsni.Odbojka.service.RequestDeniedException;
import fer.zavrsni.Odbojka.service.TemaService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class KomentarFServiceJpa implements KomentarFService {
    @Autowired
    private KomentarFRepository komRepo;
    @Autowired
    private TemaService temaSer;
    @Autowired
    private OsobaService osobaSer;
    @Autowired
    private Email slanje;
    @Override
    @Transactional
    public void createKomF(String naslov, String kom, String username) {
        Assert.hasText(naslov, "Naslov mora biti upisan.");
        Assert.hasText(kom, "Komentar mora biti upisan.");
        if (temaSer.countByNaslov(naslov) == 0)
            throw new RequestDeniedException(
                    "Tema s naslovom " + naslov + " ne postoji."
            );
        KomentarF k = new KomentarF();
        Tema t = temaSer.findByNaslov(naslov).get();
        k.setTema(t);
        k.setOsoba(osobaSer.findByEmail(username).get());
        k.setTekstF(kom);
        k.setVrijeme_komentaraF(Timestamp.valueOf(LocalDateTime.now()));
        t.getKomentari().add(k);
        komRepo.save(k);

        String poruka = "Korisnik " + username + " objavio je:\n" + kom;
        slanje.salji(poruka, "[" + naslov + "]", new HashSet<>(temaSer.findByNaslov(naslov).get().getObavijesti()));
    }

    @Override
    @Transactional
    public void izbrisiKom(String naslov, String id, String username, String uloga) {
        Assert.hasText(id, "ID komentara mora biti naveden.");
        Assert.hasText(naslov, "Naslov mora biti naveden.");
        if (temaSer.countByNaslov(naslov) == 0)
            throw new RequestDeniedException(
                    "Tema s naslovom " + naslov + " ne postoji."
            );
        Optional<KomentarF> k = komRepo.findById(Long.valueOf(id));
        if (k.isEmpty())
            throw new RequestDeniedException(
                    "Komentar ne postoji."
            );
        if (!uloga.equals("admin") && !k.get().getOsoba().getEmail().equals(username))
                throw new RequestDeniedException("Ne možete izbrisati komentar koji niste Vi napisali.");
        Tema t = temaSer.findByNaslov(naslov).get();
        t.getKomentari().remove(k.get());
        komRepo.delete(k.get());
        if (t.getKomentari().isEmpty()) {
            temaSer.izbrisiTemu(naslov, username, uloga);
        }
    }

}
