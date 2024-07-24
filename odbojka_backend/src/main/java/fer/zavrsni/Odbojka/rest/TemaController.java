package fer.zavrsni.Odbojka.rest;

import fer.zavrsni.Odbojka.domain.KomentarF;
import fer.zavrsni.Odbojka.domain.Osoba;
import fer.zavrsni.Odbojka.domain.Tema;
import fer.zavrsni.Odbojka.service.OsobaService;
import fer.zavrsni.Odbojka.service.TemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/teme")
public class TemaController {
    @Autowired
    private TemaService temaService;
    @Autowired
    private OsobaService osobaSer;

    @PostMapping("/")
    public void createTema(@RequestParam("naslov") String naslov, @AuthenticationPrincipal User user) {
        temaService.createTema(naslov, user.getUsername());
    }

    @DeleteMapping("/")
    public void izbrisiTemu(@RequestParam("naslov") String naslov, @AuthenticationPrincipal User user) {
        temaService.izbrisiTemu(naslov, user.getUsername(), user.getAuthorities().stream().findFirst().get().getAuthority());
    }

    @GetMapping("/")
    public List<Map<String, String>> prikaziTeme() {
        List<Map<String, String>> rez = new ArrayList<>();
        for (Tema t : temaService.findAll()) {
            Map<String, String> mapa = new HashMap<>();
            mapa.put("naslov", t.getNaslov());
            KomentarF k = null;
            if (t.getKomentari().isEmpty()) mapa.put("komentar", "");
            else {
                for (KomentarF komentar : t.getKomentari()) {
                    if (k == null || komentar.getVrijeme_komentaraF().compareTo(k.getVrijeme_komentaraF()) < 0) {
                        k = komentar;
                    }
                }
                mapa.put("komentar", k.getTekstF());
                mapa.put("id", String.valueOf(k.getŠifra_komentaraF()));
            }
            rez.add(mapa);
        }
        return rez;
    }

    @GetMapping("/obavijesti/ukljuci/{naslov}")
    public void obavijesti1(@PathVariable String naslov, @AuthenticationPrincipal User user) {
        temaService.obavijestiU(naslov, user.getUsername());
    }

    @GetMapping("/obavijesti/iskljuci/{naslov}")
    public void obavijesti2(@PathVariable String naslov, @AuthenticationPrincipal User user) {
        temaService.obavijestiI(naslov, user.getUsername());
    }

    @GetMapping("/obavijesti/info/{naslov}")
    public Boolean info(@PathVariable String naslov, @AuthenticationPrincipal User user) {
        Osoba o = osobaSer.findByEmail(user.getUsername()).get();
        Set<Tema> teme = o.getObavijesti();
        for (Tema t : teme){
            if (t.getNaslov().equals(naslov)) return true;
        }
        return false;
    }
}
