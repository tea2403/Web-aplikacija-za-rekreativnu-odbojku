package fer.zavrsni.Odbojka.service;

import fer.zavrsni.Odbojka.domain.Slika;
import fer.zavrsni.Odbojka.domain.Teren;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface SlikaService {
    void createSlika(List<MultipartFile> slike, Teren t);

    Optional<Slika> findByŠifraSlike(Long slika);

    void delete(Slika slik);
}
