package fer.zavrsni.Odbojka.rest;

import fer.zavrsni.Odbojka.domain.KomentarT;
import fer.zavrsni.Odbojka.domain.Termin;
import fer.zavrsni.Odbojka.service.KomentarTService;
import fer.zavrsni.Odbojka.service.RequestDeniedException;
import fer.zavrsni.Odbojka.service.TerminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/termin/komentari")
public class KomentarTController {
    @Autowired
    private KomentarTService komService;
    @Autowired
    private TerminService terminService;

    @PostMapping("/")
    public void createKomT(@RequestParam("termin") String termin, @RequestParam("kom") String kom, @AuthenticationPrincipal User user) {
        komService.createKomT(termin, kom, user.getUsername());
    }
    @DeleteMapping("/")
    public void izbrisiKomT(@RequestParam("termin") String termin, @RequestParam("kom") String id, @AuthenticationPrincipal User user) {
        komService.izbrisiKomT(termin, id, user.getUsername(), user.getAuthorities().stream().findFirst().get().getAuthority());
    }


    @GetMapping("/{termin}")
    public List<Map<String, String>> prikaziKomentare(@PathVariable String termin) {
        List<Map<String, String>> rez = new ArrayList<>();
        Optional<Termin> t = terminService.findByŠifraTermina(Long.valueOf(termin));
        if (t.isPresent()) {
            Termin term = t.get();
            Map<String, String> map = new HashMap<>();
            map.put("naslov", term.getPocetak().toString());
            rez.add(map);
            Set<KomentarT> kom = term.getKomentari();
            Set<KomentarT> sortirani = new TreeSet<>(Comparator.comparing(KomentarT::getVrijeme_komentaraT));
            sortirani.addAll(kom);
            for (KomentarT k : sortirani) {
                Map<String, String> mapa = new HashMap<>();
                mapa.put("tekst", k.getTekstT());
                mapa.put("autor", k.getOsoba().getEmail());
                mapa.put("vrijeme", String.valueOf(k.getVrijeme_komentaraT()));
                mapa.put("id", String.valueOf(k.getŠifra_komentaraT()));
                rez.add(mapa);
            }
        } else {
            throw new RequestDeniedException(
                    "Termin ne postoji."
            );
        }
        return rez;
    }
}
