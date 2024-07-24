package fer.zavrsni.Odbojka.rest;

import fer.zavrsni.Odbojka.domain.*;
import fer.zavrsni.Odbojka.service.IgracService;
import fer.zavrsni.Odbojka.service.OsobaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.*;

@RestController
@RequestMapping("/igrac")
public class IgracController {
    @Autowired
    private IgracService igracService;
    @Autowired
    private OsobaService osobaService;


    @PostMapping("/uredi")
    public Map<String, String> urediIgrac(@RequestParam("spol") String spol,
                                          @RequestParam("iskustvo") String iskustvo,
                                          @RequestParam("rodenje") String rodenje,

                                          @AuthenticationPrincipal User user) throws ParseException {
        return igracService.urediIgrac(spol, iskustvo, rodenje,  user.getUsername());
    }

    @GetMapping("/{igrac2}")
    public Map<String, String> info(@PathVariable String igrac2){
        Optional<Osoba> igrac = osobaService.findByEmail(igrac2);
        Map<String, String> rezultat = new HashMap<>();
        if (igrac.isPresent()) {
            Osoba igrac1 = igrac.get();
            rezultat.put("spol", igrac1.getSpol());
            Integer isk = igrac1.getPočetak_igranja();
            if (isk != null) {
                rezultat.put("iskustvo", String.valueOf(isk));
            }else rezultat.put("iskustvo","");
            Date dat = igrac1.getDatum_rođenja();
            if (dat != null) {
                rezultat.put("rodenje", String.valueOf(dat));
            }else rezultat.put("rodenje","");
        } else{
            rezultat.put("spol", null);
            rezultat.put("iskustvo",null);
            rezultat.put("rodenje",null);
        }
        return rezultat;
    }

    @GetMapping("/termini")
    public List<Map<String, String>> termini(@AuthenticationPrincipal User user){
        Optional<Osoba> igrac = osobaService.findByEmail(user.getUsername());
        List<Map<String, String>> rez = new ArrayList<>();
        if (igrac.isEmpty()) return rez;
        Set<Termin> sortirani = new TreeSet<>(Comparator.comparing(Termin::getPocetak));
        sortirani.addAll(igrac.get().getTermini());
        for (Tim tim : igrac.get().getTimovi()) {
            sortirani.addAll(tim.getTermini());
        }
        for (Termin term : sortirani) {
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
            StringBuilder timovi = new StringBuilder();
            for (Tim tim : term.getTimovi()){
                if (tim.getIgraci().contains(igrac.get())){
                    timovi.append(tim.getNaziv_tima()).append(";;;").append(tim.getŠifra_tima()).append(":::");
                }
            }
            mapa.put("tim", timovi.toString());
            rez.add(mapa);
        }
        return rez;
    }

}
