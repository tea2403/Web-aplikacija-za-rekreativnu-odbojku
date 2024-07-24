package fer.zavrsni.Odbojka.rest;

import fer.zavrsni.Odbojka.domain.*;
import fer.zavrsni.Odbojka.service.TerminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/termin")
public class TerminController {
    @Autowired
    private TerminService terminService;

    @GetMapping("/{pbr}")
    public List<Map<String, String>> prikaziTermine(@PathVariable String pbr) throws IOException {
        List<Map<String, String>> rez = new ArrayList<>();
        for (Termin term : terminService.findAllGrad(Long.valueOf(pbr))) {
            if(term.getPocetak().before(new Date())) continue;
            Teren t = term.getTeren();
            Map<String, String> mapa = new HashMap<>();
            mapa.put("adresa", t.getAdresa());
            mapa.put("id", String.valueOf(t.getŠifra_terena()));
            mapa.put("idTerm", String.valueOf(term.getŠifra_termina()));
            mapa.put("maxbroj", String.valueOf(t.getBroj_ljudi()));
            mapa.put("napomena", t.getNapomena());
            mapa.put("rekviziti", String.valueOf(t.getRekviziti()));
            mapa.put("pbr", String.valueOf(t.getMjesto().getPbr()));
            mapa.put("mjesto", t.getMjesto().getNaziv_mjesta());
            mapa.put("iznajmljivac", t.getIznajmljivac().getEmail());
            String mob = t.getIznajmljivac().getBroj_mobitela();
            mapa.put("mob", Objects.requireNonNullElse(mob, ""));
            Set<Osoba> svi = new HashSet<>(term.getIgraci());
            for (Tim tim : term.getTimovi()) {
                svi.addAll(tim.getIgraci());
            }
            String poruka = "{\"pocetak\": \"" + term.getPocetak() + "\", \"zauzeto\": \"" + svi.size() + "\", \"kraj\": \"" + term.getKraj() + "\", \"id\": \"" + term.getŠifra_termina() + "\", \"cijena\": \"" + term.getCijena() + "\"}";
            mapa.put("termin", poruka.trim());
            rez.add(mapa);
        }
        return rez;
    }

    @DeleteMapping("/")
    public void izbrisiRezervaciju(@RequestParam("termin") String termin, @RequestParam("tim") String tim, @AuthenticationPrincipal User user) {
        terminService.izbrisiRezervaciju(Long.valueOf(termin), Long.valueOf(tim), user.getUsername());
    }

    @GetMapping("/igraci/{termin}")
    public List<Map<String, String>> prikaziLjude(@PathVariable String termin) throws IOException {
        List<Map<String, String>> rez = new ArrayList<>();
        Optional<Termin> term = terminService.findByŠifraTermina(Long.valueOf(termin));
        if (term.isPresent()) {
            for (Osoba i : term.get().getIgraci()) {
                Map<String, String> mapa = new HashMap<>();
                mapa.put("email", i.getEmail());
                StringBuilder poz = new StringBuilder();
                for (Pozicija p : i.getPozicije()){
                    poz.append(p.getNaziv_pozicije());
                    poz.append(" ");
                }
                mapa.put("pozicije", String.valueOf(poz).trim());
                rez.add(mapa);
            }
            Set<Osoba> svi = new HashSet<>();
            for (Tim tim : term.get().getTimovi()) {
                svi.addAll(tim.getIgraci());
            }
            for (Osoba i : svi) {
                Map<String, String> mapa = new HashMap<>();
                mapa.put("email", i.getEmail());
                StringBuilder poz = new StringBuilder();
                for (Pozicija p : i.getPozicije()) {
                    poz.append(p.getNaziv_pozicije());
                    poz.append(" ");
                }
                mapa.put("pozicije", String.valueOf(poz).trim());
                rez.add(mapa);
            }
        }
        return rez;
    }
}
