package fer.zavrsni.Odbojka.rest;

import fer.zavrsni.Odbojka.domain.*;
import fer.zavrsni.Odbojka.service.IgracService;
import fer.zavrsni.Odbojka.service.TimService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.text.Collator;
import java.util.*;

@RestController
@RequestMapping("/tim")
public class TimController {
    @Autowired
    private TimService timService;
    @Autowired
    private IgracService igracSer;

    @PostMapping("/")
    public void createTim(@RequestParam("naziv") String naziv, @AuthenticationPrincipal User user) {
        timService.createTim(naziv, user.getUsername());
    }

    @PostMapping("/dodaj")
    public void dodajIgraca(@RequestParam("naziv") String naziv, @RequestParam("igrac") String igrac, @AuthenticationPrincipal User user) {
        timService.dodajIgraca(naziv, igrac.toLowerCase(), user.getUsername(), user.getAuthorities().stream().findFirst().get().getAuthority());
    }

    @DeleteMapping("/izbrisi")
    public void izbrisiIgraca(@RequestParam("naziv") String naziv, @RequestParam("igrac") String igrac, @AuthenticationPrincipal User user) {
        timService.izbrisiIgraca(naziv, igrac.toLowerCase(), user.getUsername(), user.getAuthorities().stream().findFirst().get().getAuthority());
    }

    @GetMapping("/")
    public List<Map<String, String>> prikaziTimove(@AuthenticationPrincipal User user) {
        List<Map<String, String>> rez = new ArrayList<>();
        Set<Tim> trazi;
        if(user.getAuthorities().stream().findFirst().get().getAuthority().equals("admin")) trazi = timService.findAll();
        else {
            if (igracSer.findByEmail(user.getUsername()).isEmpty()) return rez;
            Osoba i = igracSer.findByEmail(user.getUsername()).get();
            trazi = i.getTimovi();
        }
        Collator collator = Collator.getInstance(new Locale("hr", "HR"));
        Comparator<Tim> comp = Comparator.comparing(Tim::getNaziv_tima, collator);
        TreeSet<Tim> sortirani = new TreeSet<>(comp);
        sortirani.addAll(trazi);
        for (Tim t : sortirani) {
            Map<String, String> mapa = new HashMap<>();
            mapa.put("naziv", t.getNaziv_tima());
            mapa.put("kapetan", t.getKapetan().getEmail());
            mapa.put("id", String.valueOf(t.getŠifra_tima()));
            StringBuilder igraci = new StringBuilder();
            for (Osoba igrac : t.getIgraci()) {
                igraci.append(igrac.getEmail()).append(" ");
            }
            mapa.put("igraci", igraci.toString().trim());
            rez.add(mapa);
        }
        return rez;
    }
    @GetMapping("/kapetan")
    public List<Map<String, String>> prikaziTimoveKapetan(@AuthenticationPrincipal User user) {
        List<Map<String, String>> rez = new ArrayList<>();
        if (igracSer.findByEmail(user.getUsername()).isPresent()){
            Osoba i = igracSer.findByEmail(user.getUsername()).get();
            for (Tim t : i.getKapetanTimova()) {
                Map<String, String> mapa = new HashMap<>();
                mapa.put("naziv", t.getNaziv_tima());
                mapa.put("kapetan", t.getKapetan().getEmail());
                mapa.put("id", String.valueOf(t.getŠifra_tima()));
                StringBuilder igraci = new StringBuilder();
                for (Osoba igrac : t.getIgraci()) {
                    igraci.append(igrac.getEmail()).append(" ");
                }
                mapa.put("igraci", igraci.toString().trim());
                rez.add(mapa);
            }
        }
        return rez;
    }
    @PostMapping("/rezerviraj")
    public void rezerviraj(@RequestParam("termin") String termin, @RequestParam("tim") String tim, @AuthenticationPrincipal User user) {
        timService.rezerviraj(Long.valueOf(termin), Long.valueOf(tim), user.getUsername());
    }
}
