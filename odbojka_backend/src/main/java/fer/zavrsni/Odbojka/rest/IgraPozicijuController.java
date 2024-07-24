package fer.zavrsni.Odbojka.rest;

import fer.zavrsni.Odbojka.domain.Osoba;
import fer.zavrsni.Odbojka.domain.Pozicija;
import fer.zavrsni.Odbojka.service.IgraPozicijuService;
import fer.zavrsni.Odbojka.service.OsobaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/pozicije")
public class IgraPozicijuController {
    @Autowired
    private IgraPozicijuService igraService;
    @Autowired
    private OsobaService osobaSer;

    @PostMapping("/")
    public void urediPozicije(@RequestBody List<String> pozicije, @AuthenticationPrincipal User user) {
        igraService.igraPoziciju(pozicije, user.getUsername());
    }


    @GetMapping("/{igrac2}")
    public List<String> infoPozicije(@PathVariable String igrac2) {
        Optional<Osoba> igrac = osobaSer.findByEmail(igrac2);
        List<String> rez = new ArrayList<>();
        if (igrac.isPresent()){
            for (Pozicija poz : igrac.get().getPozicije()) {
                rez.add(poz.getNaziv_pozicije());
            }
        }
        return rez;
    }
}
