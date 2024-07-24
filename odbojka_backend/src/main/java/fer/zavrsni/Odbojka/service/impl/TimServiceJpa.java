package fer.zavrsni.Odbojka.service.impl;

import fer.zavrsni.Odbojka.dao.TimRepository;
import fer.zavrsni.Odbojka.domain.*;
import fer.zavrsni.Odbojka.service.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.Set;

@Service
public class TimServiceJpa implements TimService {
    @Autowired
    private TimRepository timRepo;
    @Autowired
    private IgracService igracSer;
    @Autowired
    private TerminService terminSer;

    @Override
    @Transactional
    public void createTim(String naziv, String username) {
        Assert.hasText(naziv, "Naziv tima mora biti naveden.");
        if (!igracSer.findByEmail(username).isPresent()){
            throw new RequestDeniedException(
                    "Da biste napravili svoj tim morate biti igrač. Uredite svoje podatke kako biste postali igrač."
            );
        }
        if (timRepo.countByNaziv_timaIgnoreCase(naziv) > 0)
            throw new RequestDeniedException(
                    "Tim s nazivom \"" + naziv + "\" već postoji."
            );
        Tim t = new Tim();
        Osoba i = igracSer.findByEmail(username).get();
        t.setNaziv_tima(naziv);
        t.setKapetan(i);
        timRepo.save(t);
        i.getTimovi().add(t);
        i.getKapetanTimova().add(t);
        t.getIgraci().add(i);
    }

    @Override
    @Transactional
    public Set<Tim> findAll() {
        return new HashSet<>(timRepo.findAll());
    }

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private Environment env;
    @Override
    @Transactional
    public void dodajIgraca(String naziv, String igrac, String username, String uloga) {
        Assert.hasText(naziv, "Naziv tima mora biti naveden.");
        if (timRepo.countByNaziv_timaIgnoreCase(naziv) == 0)
            throw new RequestDeniedException(
                    "Tim s nazivom \"" + naziv + "\" ne postoji."
            );
        Tim t = timRepo.findByNazivTima(naziv).get();
        if (!uloga.equals("admin") && !t.getKapetan().getEmail().equals(username)){
            throw new RequestDeniedException(
                    "Ne možete uređivati tim čiji Vi niste kapetan."
            );
        }
        if (t.getIgraci().size() == 10)
            throw new RequestDeniedException(
                    "Maksimalan broj igrača u timu je 10."
            );
        if (igracSer.findByEmail(igrac).isEmpty()){
            throw new RequestDeniedException(
                    "Osoba koju želite dodati u tim ne postoji."
            );
        }
        Osoba i = igracSer.findByEmail(igrac).get();
        if (t.getIgraci().contains(i)){
            throw new RequestDeniedException(
                    "Igrač \"" + igrac + "\" je već dio tima."
            );
        }
        for (Termin term : t.getTermini()){
            Set<Osoba> svi = new HashSet<>(term.getIgraci());
            for (Tim tim : term.getTimovi()) {
                svi.addAll(tim.getIgraci());
            }
            if (svi.size() + 1 > term.getTeren().getBroj_ljudi()){
                throw new RequestDeniedException(
                        "Nažalost ne možete dodati igrača u ovaj tim zato što je tim rezervirao termin u kojemu više nema dovoljno mjesta za novog igrača."
                );
            }
        }
        t.getIgraci().add(i);
        i.getTimovi().add(t);
        for(Termin term : t.getTermini()){
            if (i.getTermini().contains(term)){
                i.getTermini().remove(term);
                term.getIgraci().remove(i);
            }
        }
        timRepo.save(t);

        String poruka = "Dodani ste u tim: " + naziv + ".";
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject("Postali ste dio novoga tima");
        email.setText(poruka);
        email.setFrom(env.getProperty("support.email"));
        email.setTo(i.getEmail());
        mailSender.send(email);
    }

    @Override
    @Transactional
    public void izbrisiIgraca(String naziv, String igrac, String username, String uloga) {
        Assert.hasText(naziv, "Naziv tima mora biti naveden.");
        if (timRepo.countByNaziv_timaIgnoreCase(naziv) == 0)
            throw new RequestDeniedException(
                    "Tim s nazivom \"" + naziv + "\" ne postoji."
            );
        Tim t = timRepo.findByNazivTima(naziv).get();
        if (!uloga.equals("admin") && !t.getKapetan().getEmail().equals(username) && !username.equals(igrac)){
            throw new RequestDeniedException(
                    "Ne možete uređivati tim čiji Vi niste kapetan."
            );
        }
        if (igracSer.findByEmail(igrac).isEmpty() || !t.getIgraci().contains(igracSer.findByEmail(igrac).get())){
            throw new RequestDeniedException(
                    "Osoba koju želite izbrisati nije dio tima."
            );
        }
        Osoba i = igracSer.findByEmail(igrac).get();
        t.getIgraci().remove(i);
        i.getTimovi().remove(t);
        timRepo.save(t);
        if (t.getKapetan().getEmail().equals(igrac)){
            for (Osoba x : t.getIgraci()) x.getTimovi().remove(t);
            t.getIgraci().clear();
            t.getKapetan().getKapetanTimova().remove(t);
            timRepo.delete(t);
        }
    }

    @Override
    @Transactional
    public void rezerviraj(Long termin, Long timID, String username) {
        if (terminSer.findByŠifraTermina(termin).isEmpty())
            throw new RequestDeniedException(
                    "Termin ne postoji.");
        Termin term = terminSer.findByŠifraTermina(termin).get();
        if (timID == -1){
            if (igracSer.findByEmail(username).isEmpty()){
                throw new RequestDeniedException(
                        "Nemate ulogu igrača. Da biste mogli rezervirati teren urediti svoje podatke."
                );
            }
            Osoba i = igracSer.findByEmail(username).get();
            for (Tim tim : term.getTimovi()){
                for (Osoba igr : tim.getIgraci()){
                    if (igr.equals(i))
                        throw new RequestDeniedException(
                                "U ovom terminu već ste zabilježeni kao dio tima " + tim.getNaziv_tima()+ "."
                        );
                }
            }
            if (term.getIgraci().contains(i)) return;
            Set<Osoba> svi = new HashSet<>(term.getIgraci());
            for (Tim tim : term.getTimovi()) {
                svi.addAll(tim.getIgraci());
            }
            if (svi.size() + 1 > term.getTeren().getBroj_ljudi()){
                throw new RequestDeniedException(
                        "Nažalost u ovome terminu više nema slobodnih mjesta."
                );
            }
            term.getIgraci().add(i);
            i.getTermini().add(term);
        } else {
            if (timRepo.findByŠifraTima(timID).isEmpty())
                throw new RequestDeniedException(
                        "Tim ne postoji.");
            Tim tim = timRepo.findByŠifraTima(timID).get();
            if (!tim.getKapetan().getEmail().equals(username)){
                throw new RequestDeniedException(
                        "Tim mora zabilježiti njegov kapetan."
                );
            }
            Set<Osoba> svi = new HashSet<>(term.getIgraci());
            for (Tim t : term.getTimovi()) {
                svi.addAll(t.getIgraci());
            }
            svi.addAll(tim.getIgraci());
            if (svi.size() > term.getTeren().getBroj_ljudi()){
                throw new RequestDeniedException(
                        "Nažalost u ovome terminu nema dovoljno slobodnih mjesta za cijeli tim."
                );
            }
            term.getTimovi().add(tim);
            tim.getTermini().add(term);
            for (Osoba igr : tim.getIgraci()){
                if (term.getIgraci().contains(igr)){
                    term.getIgraci().remove(igr);
                    igr.getTermini().remove(term);
                }
            }
        }

    }
}
