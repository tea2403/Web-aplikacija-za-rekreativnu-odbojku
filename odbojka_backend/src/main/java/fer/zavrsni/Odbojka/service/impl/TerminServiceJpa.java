package fer.zavrsni.Odbojka.service.impl;

import fer.zavrsni.Odbojka.dao.TerminRepository;
import fer.zavrsni.Odbojka.domain.*;
import fer.zavrsni.Odbojka.rest.TerminDTO;
import fer.zavrsni.Odbojka.service.IgracService;
import fer.zavrsni.Odbojka.service.RequestDeniedException;
import fer.zavrsni.Odbojka.service.TerminService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
public class TerminServiceJpa implements TerminService {
    @Autowired
    private TerminRepository terminRepo;
    @Autowired
    private IgracService igracSer;

    @Override
    @Transactional
    public boolean createTermin(Teren t, List<TerminDTO> termini, String username, String uloga) {
        if (!uloga.equals("admin") && !t.getIznajmljivac().getEmail().equals(username))
            throw new RequestDeniedException(
                    "Niste vlasnik terena sa šifrom \"" + t.getŠifra_terena() + "\" te ga zato ne smijete uređivati."
            );
        termini.removeIf(termin -> termin.getPocetak().isEmpty() || termin.getKraj().isEmpty() || termin.getCijena().isEmpty());
        List<Termin> termini2 = new ArrayList<>();
        Osoba o = t.getIznajmljivac();
        Set<Termin> pom = new HashSet<>(t.getTermini());
        pom.removeIf(term -> term.getPocetak().before(new Date()));
        for (TerminDTO terminDTO : termini) {
            Timestamp od = Timestamp.valueOf(terminDTO.getPocetak().replace("T", " ") + ":00");
            Timestamp doo = Timestamp.valueOf(terminDTO.getKraj().replace("T", " ")+ ":00");
            Float cijena = Float.valueOf(terminDTO.getCijena());
            if (cijena < 0) continue;
            if (od.after(doo)) continue;
            if (od.before(new Date())) continue;
            if (doo.before(new Date())) continue;
            Termin termin;
            if(!(terminDTO.getId() == null) && pom.stream().anyMatch(term -> Objects.equals(term.getŠifra_termina(), Long.valueOf(terminDTO.getId()))))termin =terminRepo.findByŠifraTermina(Long.valueOf(terminDTO.getId())).get();
            else {
                termin = new Termin();
            }
            termin.setCijena(cijena);
            termin.setKraj(doo);
            termin.setPocetak(od);
            termin.setTeren(t);
            termini2.add(termin);
        }
        Set<Termin> termini3 = new HashSet<>();
        Set<Termin> termini4 = new HashSet<>(termini2);
        for (Termin trenutni : termini2) {
            if (!termini4.contains(trenutni)) continue;
            for (Termin termin : termini4) {
                if (!trenutni.equals(termin) && ((trenutni.getPocetak().before(termin.getKraj()) && termin.getPocetak().before(trenutni.getKraj())) ||
                        (trenutni.getPocetak().after(termin.getPocetak()) && termin.getKraj().after(trenutni.getKraj())))) {
                    termini4.remove(termin);
                    break;
                }
            }
            termini3.add(trenutni);
        }
        if (termini3.isEmpty() && t.getTermini().isEmpty()) {
            throw new RequestDeniedException(
                    "Za teren mora biti upisan barem jedan valjani termin."
            );
        }
        for (Termin term : termini3){
            if (pom.contains(term)) continue;
            terminRepo.save(term);
            t.getTermini().add(term);
        }
        for (Termin term : pom) {
            if(!termini3.contains(term)) {
                t.getTermini().remove(term);
                for (Osoba i : term.getIgraci()){
                    i.getTermini().remove(term);
                }
                for (Tim i : term.getTimovi()){
                    i.getTermini().remove(term);
                }
                terminRepo.delete(terminRepo.findByŠifraTermina(term.getŠifra_termina()).get());
            }
        }
        return true;
    }

    @Override
    @Transactional
    public List<Termin> findAllGrad(Long pbr) {
        return terminRepo.findAllSortedByPocetakGradAsc(pbr);
    }

    @Override
    @Transactional
    public Optional<Termin> findByŠifraTermina(Long id) {
        return terminRepo.findByŠifraTermina(id);
    }

    @Override
    @Transactional
    public void izbrisiRezervaciju(Long termin, Long tim_id, String username) {
        if (findByŠifraTermina(termin).isEmpty())
            throw new RequestDeniedException(
                    "Termin ne postoji.");
        Termin term = findByŠifraTermina(termin).get();
        Osoba i = igracSer.findByEmail(username).get();
        if (tim_id == -1) {
            if (term.getIgraci().contains(i)){
                term.getIgraci().remove(i);
                i.getTermini().remove(term);
                terminRepo.save(term);
            }
        } else {
            for (Tim tim : term.getTimovi()){
                if (!tim.getŠifra_tima().equals(tim_id)) continue;
                if (tim.getKapetan().getEmail().equals(username)) {
                    term.getTimovi().remove(tim);
                    tim.getTermini().remove(term);
                    terminRepo.save(term);
                    return;
                }
                if (tim.getIgraci().contains(i)){
                    throw new RequestDeniedException(
                            "U ovom terminu ste kao dio tima " + tim.getNaziv_tima() + ". Ako želite odustati od termina prvo morate izaći iz tima."
                    );
                }
            }
        }
    }

}
