package fer.zavrsni.Odbojka.service;

import fer.zavrsni.Odbojka.domain.Teren;
import fer.zavrsni.Odbojka.rest.TerenDTO;
import fer.zavrsni.Odbojka.rest.TerminDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TerenService {
    Long createTeren(TerenDTO zahtjev, List<MultipartFile> slike, String username, String uloga, Long teren, List<TerminDTO> x);

    int countByŠifraTerena(Long teren);

    void izbrisiTeren(Teren t);

    Optional<Teren> findByŠifraTerena(Long teren);

    void izbrisiSliku(Long aLong, String username, String uloga);

    Set<Teren> findAll();
}
