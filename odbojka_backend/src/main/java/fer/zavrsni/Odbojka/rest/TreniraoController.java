package fer.zavrsni.Odbojka.rest;

import fer.zavrsni.Odbojka.domain.*;
import fer.zavrsni.Odbojka.service.OsobaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/trenirao")
public class TreniraoController {
    @Autowired
    private OsobaService osobaService;
    @GetMapping("/{igrac2}")
    public List<Map<String, String>> infoKlubovi(@PathVariable String igrac2){
        Optional<Osoba> igrac = osobaService.findByEmail(igrac2);
        List<Map<String, String>> rez = new ArrayList<>();
        if(igrac.isPresent()) {
            for (Trenirao tr : igrac.get().getTreniranja()) {
                Map<String, String> mapa = new HashMap<>();
                mapa.put("klub", tr.getKlub().getIme_kluba());
                mapa.put("pbr", String.valueOf(tr.getKlub().getMjesto().getPbr()));
                mapa.put("od", tr.getOd_godina() != null ? String.valueOf(tr.getOd_godina()) : null);
                mapa.put("do", tr.getDo_godina() != null ? String.valueOf(tr.getDo_godina()) : null);
                rez.add(mapa);
            }
        }
        return rez;
    }
}
