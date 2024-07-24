package fer.zavrsni.Odbojka.rest;

import fer.zavrsni.Odbojka.domain.Mjesto;
import fer.zavrsni.Odbojka.domain.Osoba;
import fer.zavrsni.Odbojka.domain.Profilna;
import fer.zavrsni.Odbojka.service.OsobaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
@RequestMapping("/osoba")
public class OsobaController {
    @Autowired
    private OsobaService osobaService;

    @GetMapping("/{igrac2}")
    public Map<String, String> infoOsoba(@PathVariable String igrac2) throws IOException {
        Optional<Osoba> osoba = osobaService.findByEmail(igrac2);
        Map<String, String> rezultat = new HashMap<>();
        if (osoba.isPresent()) {
            Osoba osoba1 = osoba.get();
            rezultat.put("ime", osoba1.getIme());
            rezultat.put("prezime", osoba1.getPrezime());
            rezultat.put("broj", osoba1.getBroj_mobitela());
            Profilna p = osoba1.getProfilna();
            if (p != null) {
                rezultat.put("profilna", String.format("https://firebasestorage.googleapis.com/v0/b/odbojka-c4851.appspot.com/o/%s?alt=media", URLEncoder.encode("profilne" + "/" + p.getIme_profilne(), StandardCharsets.UTF_8)));
            }else rezultat.put("profilna","");
            Mjesto m = osoba1.getMjesto();
            if (m != null) {
                rezultat.put("pbr", String.valueOf(m.getPbr()));
                rezultat.put("grad", m.getNaziv_mjesta());
            }else {
                rezultat.put("pbr","");
                rezultat.put("grad","");
            }
        }
        return rezultat;
    }

    @PostMapping("/registracija")
    public void createOsoba(@Valid @RequestBody Osoba osoba) {
        osobaService.createOsoba(osoba);
    }

    @PostMapping("/uredi")
    public Map<String, String> urediOsoba(@RequestParam("ime") String ime,
                                          @RequestParam("prezime") String prezime,
                                          @RequestParam("broj") String broj,
                                          @RequestParam(value = "profilna", required = false) MultipartFile profilna,
                                          @RequestParam("brisi") String brisi,
                                          @RequestParam("pbr") String pbr,
                                          @AuthenticationPrincipal User user) {
        return osobaService.urediOsoba(ime, prezime, broj, profilna, brisi, pbr, user.getUsername());
    }
}
