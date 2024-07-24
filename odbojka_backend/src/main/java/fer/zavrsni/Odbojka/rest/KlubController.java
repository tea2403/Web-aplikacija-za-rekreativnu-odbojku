package fer.zavrsni.Odbojka.rest;


import fer.zavrsni.Odbojka.service.KlubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/klubovi")
public class KlubController {
    @Autowired
    private KlubService klubService;

    @PostMapping("/uredi")
    public void dodajKlubove(@RequestBody List<KlubDTO> zahtjev, @AuthenticationPrincipal User user) {
        klubService.dodajKlubove(zahtjev, user.getUsername());
    }


}
