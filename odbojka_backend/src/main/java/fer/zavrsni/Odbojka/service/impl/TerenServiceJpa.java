package fer.zavrsni.Odbojka.service.impl;

import fer.zavrsni.Odbojka.dao.TerenRepository;
import fer.zavrsni.Odbojka.domain.*;
import fer.zavrsni.Odbojka.rest.TerenDTO;
import fer.zavrsni.Odbojka.rest.TerminDTO;
import fer.zavrsni.Odbojka.service.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TerenServiceJpa implements TerenService {
    @Autowired
    private TerenRepository terenRepo;
    @Autowired
    private MjestoService mjestoSer;
    @Autowired
    private OsobaService osobaSer;
    @Autowired
    private SlikaService slikaSer;
    @Autowired
    private TerminService terminSer;
    @Override
    @Transactional
    public Long createTeren(TerenDTO zahtjev, List<MultipartFile> slike, String username, String uloga, Long teren, List<TerminDTO> x) {
        Assert.notNull(zahtjev, "Podaci o terenu moraju biti navedeni.");
        String adresa = zahtjev.getAdresa();
        Assert.hasText(adresa, "Adresa mora biti upisana.");
        String pbr = zahtjev.getPbr();
        Assert.hasText(pbr, "Mjesto u kojem se teren nalazi mora biti odabrano.");
        Optional<Mjesto> mj = mjestoSer.findByPbr(Long.valueOf(pbr));
        if (mj.isEmpty()) throw new RequestDeniedException(
                "Mjesto s poštanskim brojem " + pbr + " ne postoji."
        );
        String max = zahtjev.getBroj_ljudi();
        Assert.hasText(max, "Maksimalan broj igrača mora biti upisan.");
        try {
            int brojIgraca = Integer.parseInt(max);
            if (brojIgraca < 0) {
                throw new RequestDeniedException("Maksimalan broj igrača mora biti pozitivan broj.");
            }
        } catch (NumberFormatException e) {
            throw new RequestDeniedException("Maksimalan broj igrača mora biti pozitivan broj.");
        }
        Teren t;
        if (teren == -1){
            if (slike == null) {
                throw new RequestDeniedException("Za teren mora biti dostavljena barem jedna slika.");
            }
            t = new Teren();
            Assert.notNull(x, "Za teren mora biti upisan barem jedan valjani termin.");
            t.setIznajmljivac(osobaSer.findByEmail(username).get());
            osobaSer.findByEmail(username).get().getTereni().add(t);
        } else {
            Optional<Teren> ter = terenRepo.findByŠifraTerena(teren);
            if (ter.isEmpty()) throw new RequestDeniedException("Teren sa šifrom \"" + teren + "\" ne postoji.");
            t = ter.get();
            if (!uloga.equals("admin") && !t.getIznajmljivac().getEmail().equals(username))
                throw new RequestDeniedException(
                        "Niste vlasnik terena te ga zato ne možete uređivati."
                );
        }
        t.setAdresa(adresa);
        t.setMjesto(mj.get());
        t.setNapomena(zahtjev.getNapomena());
        t.setRekviziti(Boolean.parseBoolean(zahtjev.getRekviziti()));
        t.setBroj_ljudi(Integer.parseInt(max));
        terenRepo.save(t);
        terminSer.createTermin(t, x, username, uloga);
        if (t.getTermini().isEmpty()){
            t.getIznajmljivac().getTereni().remove(t);
            izbrisiTeren(t);
            return -1l;
        }
        slikaSer.createSlika(slike, t);
        return t.getŠifra_terena();
    }

    @Override
    @Transactional
    public int countByŠifraTerena(Long teren) {
        return terenRepo.countByŠifraTerena(teren);
    }

    @Override
    @Transactional
    public void izbrisiTeren(Teren t) {
        Set<Slika> pom = new HashSet<>(t.getSlike());
        for (Slika sl : pom){
            slikaSer.delete(sl);
        }
        t.getTermini().clear();
        terenRepo.delete(t);
    }

    @Override
    @Transactional
    public Optional<Teren> findByŠifraTerena(Long teren) {
        return terenRepo.findByŠifraTerena(teren);
    }

    @Override
    @Transactional
    public void izbrisiSliku(Long slika, String username, String uloga) {
        Optional<Slika> s = slikaSer.findByŠifraSlike(slika);
        if (s.isEmpty()) throw new RequestDeniedException("Slika ne postoji.");
        Slika slik = s.get();
        Teren t = slik.getTeren();
        if (!uloga.equals("admin") && !t.getIznajmljivac().getEmail().equals(username))
            throw new RequestDeniedException(
                    "Niste vlasnik terena čija je to slika te ju zato ne možete izbrisati."
            );
        if (t.getSlike().size() == 1 && !t.getTermini().isEmpty())
            throw new RequestDeniedException(
                    "Za teren mora postojati barem jedna slika."
            );
        slikaSer.delete(slik);
        if (t.getSlike().isEmpty()){
            t.getIznajmljivac().getTereni().remove(t);
            for (Termin term : t.getTermini()) {
                for (Osoba i : term.getIgraci()) {
                    i.getTermini().remove(term);
                }
                for (Tim i : term.getTimovi()) {
                    i.getTermini().remove(term);
                }
            }
            terenRepo.delete(t);
        }
    }

    @Override
    @Transactional
    public Set<Teren> findAll() {
        return new HashSet<>(terenRepo.findAll());
    }
}
