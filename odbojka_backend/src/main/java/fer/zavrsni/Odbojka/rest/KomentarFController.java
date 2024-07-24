package fer.zavrsni.Odbojka.rest;

import fer.zavrsni.Odbojka.domain.KomentarF;
import fer.zavrsni.Odbojka.domain.Tema;
import fer.zavrsni.Odbojka.service.KomentarFService;
import fer.zavrsni.Odbojka.service.RequestDeniedException;
import fer.zavrsni.Odbojka.service.TemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/forum/komentari")
public class KomentarFController {
    @Autowired
    private KomentarFService komService;
    @Autowired
    private TemaService temaService;

    @PostMapping("/")
    public void createKomF(@RequestParam("naslov") String naslov, @RequestParam("kom") String kom, @AuthenticationPrincipal User user) {
        komService.createKomF(naslov, kom, user.getUsername());
    }

    @DeleteMapping("/")
    public void izbrisiKom(@RequestParam("naslov") String naslov, @RequestParam("kom") String id, @AuthenticationPrincipal User user) {
        komService.izbrisiKom(naslov, id, user.getUsername(), user.getAuthorities().stream().findFirst().get().getAuthority());
    }

    @GetMapping("/{naslov}")
    public List<Map<String, String>> prikaziKomentare(@PathVariable String naslov) {
        List<Map<String, String>> rez = new ArrayList<>();
        Optional<Tema> t = temaService.findByNaslov(naslov);
        if (t.isPresent()) {
            Tema tema = t.get();
            Set<KomentarF> kom = tema.getKomentari();
            Set<KomentarF> sortirani = new TreeSet<>(Comparator.comparing(KomentarF::getVrijeme_komentaraF));
            sortirani.addAll(kom);
            for (KomentarF k : sortirani) {
                Map<String, String> mapa = new HashMap<>();
                mapa.put("tekst", k.getTekstF());
                mapa.put("autor", k.getOsoba().getEmail());
                mapa.put("vrijeme", String.valueOf(k.getVrijeme_komentaraF()));
                mapa.put("id", String.valueOf(k.getŠifra_komentaraF()));
                rez.add(mapa);
            }
        } else {
            throw new RequestDeniedException(
                    "Tema s naslovom " + naslov + " ne postoji."
            );
        }
        return rez;
    }
}
