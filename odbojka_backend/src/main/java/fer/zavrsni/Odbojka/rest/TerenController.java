package fer.zavrsni.Odbojka.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fer.zavrsni.Odbojka.domain.*;
import fer.zavrsni.Odbojka.service.OsobaService;
import fer.zavrsni.Odbojka.service.TerenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/teren")
public class TerenController {
    @Autowired
    private TerenService terenService;
    @Autowired
    private OsobaService osobaSer;

    @PostMapping("/{teren}")
    public Long createTeren(@RequestParam(value = "slike", required = false) List<MultipartFile> slike,
                            @RequestParam("adresa") String adresa,
                            @RequestParam("pbr") String pbr,
                            @RequestParam("broj_ljudi") String maxbroj,
                            @RequestParam("rekviziti") String rekviziti,
                            @RequestParam("napomena") String napomena,
                            @AuthenticationPrincipal User user,
                            @RequestParam String termini,
                            @PathVariable String teren) throws JsonProcessingException {
        TerenDTO zahtjev = new TerenDTO(adresa, pbr, maxbroj, rekviziti, napomena);
        List<TerminDTO> x = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        Matcher matcher = Pattern.compile("\\{([^{}]+)}").matcher(termini);
        List<String> rez = new ArrayList<>();
        while (matcher.find()) {
            rez.add("{" + matcher.group(1) + "}");
        }
        for (String s : rez){
            x.add(objectMapper.readValue(s, TerminDTO.class));
        }
        return terenService.createTeren(zahtjev, slike, user.getUsername(), user.getAuthorities().stream().findFirst().get().getAuthority(), Long.valueOf(teren), x);
    }

    @GetMapping("/")
    public List<Map<String, String>> prikaziTerene(@AuthenticationPrincipal User user){
        List<Map<String, String>> rez = new ArrayList<>();
        Osoba o = osobaSer.findByEmail(user.getUsername()).get();
        Set<Teren> trazi1;
        if(user.getAuthorities().stream().findFirst().get().getAuthority().equals("admin")) trazi1 = terenService.findAll();
        else trazi1 = o.getTereni();
        List<Teren> trazi = trazi1.stream()
                .sorted(Comparator.comparing(Teren::getŠifra_terena))
                .toList();
        for (Teren t : trazi) {
            Map<String, String> mapa = new HashMap<>();
            mapa.put("id", String.valueOf(t.getŠifra_terena()));
            mapa.put("adresa", t.getAdresa());
            mapa.put("maxbroj", String.valueOf(t.getBroj_ljudi()));
            mapa.put("napomena", t.getNapomena());
            mapa.put("rekviziti", String.valueOf(t.getRekviziti()));
            mapa.put("pbr", String.valueOf(t.getMjesto().getPbr()));
            mapa.put("iznajmljivac", t.getIznajmljivac().getEmail());
            String mob = t.getIznajmljivac().getBroj_mobitela();
            mapa.put("mob", Objects.requireNonNullElse(mob, ""));
            StringBuilder termini = new StringBuilder();
            List<Termin> sortirani = new ArrayList<>(t.getTermini());
            sortirani.sort(Comparator.comparing(Termin::getPocetak));
            for (Termin term : sortirani) {
                if(term.getPocetak().before(new Date())) continue;
                Set<Osoba> svi = new HashSet<>(term.getIgraci());
                for (Tim tim : term.getTimovi()) {
                    svi.addAll(tim.getIgraci());
                }
                String poruka = "{\"pocetak\": \"" + term.getPocetak() + "\", \"zauzeto\": \"" + svi.size() + "\", \"kraj\": \"" + term.getKraj() + "\", \"id\": \"" + term.getŠifra_termina() + "\", \"cijena\": \"" + term.getCijena() + "\"}";
                termini.append(poruka).append(";;;");
            }
            mapa.put("termini", termini.toString().trim());
            rez.add(mapa);
        }
        return rez;
    }

    @PostMapping("/izbrisi/slika/{slika}")
    public void izbrisiSliku(@PathVariable String slika, @AuthenticationPrincipal User user) {
        terenService.izbrisiSliku(Long.valueOf(slika), user.getUsername(), user.getAuthorities().stream().findFirst().get().getAuthority());
    }

    @GetMapping("/slike/{teren}")
    public List<String> prikaziSlike(@PathVariable String teren) throws IOException {
        List<String> rez = new ArrayList<>();
        Optional<Teren> term = terenService.findByŠifraTerena(Long.valueOf(teren));
        if (term.isPresent()) {
            for (Slika s : term.get().getSlike()) {
                rez.add(String.format("https://firebasestorage.googleapis.com/v0/b/odbojka-c4851.appspot.com/o/%s?alt=media", URLEncoder.encode("tereni" + "/" + s.getIme_slike(), StandardCharsets.UTF_8)));
            }
        }
        return rez;
    }
    @GetMapping("/slike/brisi/{teren}")
    public List<Map<String, String>> prikaziSlikeZaBrisanje(@PathVariable String teren) throws IOException {
        List<Map<String, String>> rez = new ArrayList<>();
        Optional<Teren> term = terenService.findByŠifraTerena(Long.valueOf(teren));
        if (term.isPresent()) {
            for (Slika s : term.get().getSlike()) {
                Map<String, String> mapa = new HashMap<>();
                mapa.put("slika", String.format("https://firebasestorage.googleapis.com/v0/b/odbojka-c4851.appspot.com/o/%s?alt=media", URLEncoder.encode("tereni" + "/" + s.getIme_slike(), StandardCharsets.UTF_8)));
                mapa.put("id", String.valueOf(s.getŠifra_slike()));
                rez.add(mapa);
            }
        }
        return rez;
    }
}
