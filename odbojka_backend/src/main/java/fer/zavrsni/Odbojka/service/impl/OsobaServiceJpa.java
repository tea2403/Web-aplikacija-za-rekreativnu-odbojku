package fer.zavrsni.Odbojka.service.impl;

import fer.zavrsni.Odbojka.dao.OsobaRepository;
import fer.zavrsni.Odbojka.domain.Mjesto;
import fer.zavrsni.Odbojka.domain.Osoba;
import fer.zavrsni.Odbojka.domain.Profilna;
import fer.zavrsni.Odbojka.service.MjestoService;
import fer.zavrsni.Odbojka.service.OsobaService;
import fer.zavrsni.Odbojka.service.ProfilnaService;
import fer.zavrsni.Odbojka.service.RequestDeniedException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class OsobaServiceJpa implements OsobaService {
    @Autowired
    private OsobaRepository osobaRepo;
    @Autowired
    private ProfilnaService profilnaSer;

    @Autowired
    private MjestoService mjestoSer;
    private static final String EMAIL_FORMAT = "(?i)[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]+";
    private static final String LOZINKA_FORMAT= "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$";
    private static final String PREZIME_FORMAT= "^[a-zA-ZčČćĆžŽšŠđĐ]+([ '-][a-zA-ZčČćĆžŽšŠđĐ]+)*$";

    @Autowired
    private PasswordEncoder pswdEncoder;

    @Override
    @Transactional
    public Osoba createOsoba(Osoba osoba){
        Assert.notNull(osoba, "Podaci o korisniku moraju biti navedeni.");
        osoba.setEmail(osoba.getEmail().toLowerCase());
        String email = osoba.getEmail();
        Assert.hasText(email, "E-mail mora biti upisan.");
        Assert.isTrue(email.matches(EMAIL_FORMAT),
                "Email mora biti u ispravnom obliku, npr. user@example.com, a ne '" + email + "'."
        );
        if (osobaRepo.countByEmail(osoba.getEmail()) > 0){
            throw new RequestDeniedException(
                    "Osoba s e-mailom " + osoba.getEmail() + " već postoji."
            );
        }
        Assert.hasText(osoba.getIme(), "Ime mora biti upisano.");
        Assert.isTrue(osoba.getIme().matches(PREZIME_FORMAT),
                "Ime smije sadržavati samo slova."
        );
        Assert.hasText(osoba.getPrezime(), "Prezime mora biti upisano.");
        Assert.isTrue(osoba.getPrezime().matches(PREZIME_FORMAT),
                "Prezime smije sadržavati samo slova."
        );
        String lozinka = osoba.getLozinka();
        Assert.hasText(lozinka, "Lozinka mora biti upisana.");
        Assert.isTrue(lozinka.matches(LOZINKA_FORMAT),
                "Lozinka mora biti u pravilnom obliku - barem jedan broj, jedno veliko slovo, jedno malo slovo " +
                        "i mora sadržavati barem osam znakova."
        );
        String kodiranaLozinka = pswdEncoder.encode(osoba.getLozinka());
        osoba.setLozinka(kodiranaLozinka);
        return osobaRepo.save(osoba);
    }
    @Override
    @Transactional
    public Optional<Osoba> findByEmail(String email) {
        return osobaRepo.findByEmail(email);
    }

    @Override
    @Transactional
    public String ime(String email) {
        Optional<Osoba> osoba = osobaRepo.findByEmail(email);
        return osoba.isPresent() ? osoba.get().getIme() : "";
    }

    @Override
    @Transactional
    public String prezime(String email) {
        Optional<Osoba> osoba = osobaRepo.findByEmail(email);
        return osoba.isPresent() ? osoba.get().getPrezime() : "";
    }

    @Override
    @Transactional
    public void spremi(Osoba o) {
        osobaRepo.save(o);
    }

    @Override
    @Transactional
    public Map<String, String> urediOsoba(String ime, String prezime, String broj, MultipartFile profilna, String brisi, String pbr, String email) {
        Optional<Osoba> osoba = findByEmail(email);
        Map<String, String> rezultat = new HashMap<>();
        if (osoba.isPresent()) {
            if (!broj.isEmpty() && (broj.length() < 9 || !broj.matches("\\d+"))){
                throw new RequestDeniedException(
                        "Broj mobitela mora biti duljine barem 9 brojeva i ne smije sadržavati druge znakove osim brojeva."
                );
            }
            Assert.hasText(ime, "Ime mora biti upisano.");
            Assert.isTrue(ime.matches(PREZIME_FORMAT),
                    "Ime smije sadržavati samo slova."
            );
            Assert.hasText(prezime, "Prezime mora biti upisano.");
            Assert.isTrue(prezime.matches(PREZIME_FORMAT),
                    "Prezime smije sadržavati samo slova."
            );
            Osoba osoba1 = osoba.get();
            osoba1.setIme(ime);
            osoba1.setPrezime(prezime);
            if (pbr.equals("0")) osoba1.setMjesto(null);
            else if (!pbr.isEmpty()) {
                Optional<Mjesto> mjesto = mjestoSer.findByPbr(Long.parseLong(pbr));
                mjesto.ifPresent(osoba1::setMjesto);
            }
            if (broj.isEmpty()) broj = null;
            osoba1.setBroj_mobitela(broj);
            rezultat.put("ime", ime);
            rezultat.put("prezime", prezime);
            rezultat.put("broj", broj);
            if (pbr.isEmpty() || osoba1.getMjesto() == null) {
                pbr = "0";
            } else {
                pbr = String.valueOf(osoba1.getMjesto().getPbr());
            }
            rezultat.put("pbr", pbr);
            if (!brisi.equals("on") && profilna!=null){
                if (osoba1.getProfilna() != null) profilnaSer.izbrisi(osoba1.getProfilna().getIme_profilne());
                Profilna prof = new Profilna();
                prof.setIme_profilne(prof.upload(profilna, email, "profilne"));
                prof.setOsoba(osoba1);
                osoba1.setProfilna(profilnaSer.createProfilna(prof));
            }
            if (brisi.equals("on") && osoba1.getProfilna() != null) {
                profilnaSer.izbrisi(osoba1.getProfilna().getIme_profilne());
                osoba1.setProfilna(null);
            }
            osobaRepo.save(osoba1);
        }
        return rezultat;
    }


}
