package fer.zavrsni.Odbojka.service.impl;

import fer.zavrsni.Odbojka.dao.KomentarTRepository;
import fer.zavrsni.Odbojka.domain.*;
import fer.zavrsni.Odbojka.service.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class KomentarTServiceJpa implements KomentarTService {
    @Autowired
    private KomentarTRepository komRepo;
    @Autowired
    private TerminService terminSer;
    @Autowired
    private OsobaService osobaSer;

    @Autowired
    private Email slanje;
    @Override
    @Transactional
    public void createKomT(String termin, String kom, String username) {
        Assert.hasText(termin, "Termin mora biti upisan.");
        Assert.hasText(kom, "Komentar mora biti upisan.");
        if (terminSer.findByŠifraTermina(Long.valueOf(termin)).isEmpty())
            throw new RequestDeniedException(
                    "Termin ne postoji."
            );
        KomentarT k = new KomentarT();
        Termin t = terminSer.findByŠifraTermina(Long.valueOf(termin)).get();
        k.setTermin(t);
        k.setOsoba(osobaSer.findByEmail(username).get());
        k.setTekstT(kom);
        k.setVrijeme_komentaraT(Timestamp.valueOf(LocalDateTime.now()));
        t.getKomentari().add(k);
        komRepo.save(k);

        String poruka = "Korisnik " + username + " objavio je:\n" + kom;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String naslov = "[Termin " + t.getŠifra_termina() + "] - " + t.getPocetak().toLocalDateTime().format(formatter);
        Set<Osoba> svi = new HashSet<>(t.getIgraci());
        for (Tim tim : t.getTimovi()) {
            svi.addAll(tim.getIgraci());
        }
        slanje.salji(poruka, naslov, svi);
    }

    @Override
    @Transactional
    public void izbrisiKomT(String termin, String id, String username, String uloga) {
        Assert.hasText(id, "ID komentara mora biti naveden.");
        Assert.hasText(termin, "Termin mora biti naveden.");
        if (terminSer.findByŠifraTermina(Long.valueOf(termin)).isEmpty())
            throw new RequestDeniedException(
                    "Termin ne postoji."
            );
        Optional<KomentarT> k = komRepo.findById(Long.valueOf(id));
        if (k.isEmpty())
            throw new RequestDeniedException(
                    "Komentar ne postoji."
            );
        if (!uloga.equals("admin") && !k.get().getOsoba().getEmail().equals(username))
            throw new RequestDeniedException("Ne možete izbrisati komentar koji niste Vi napisali.");
        Termin t = terminSer.findByŠifraTermina(Long.valueOf(termin)).get();
        t.getKomentari().remove(k.get());
        komRepo.delete(k.get());
    }
}
