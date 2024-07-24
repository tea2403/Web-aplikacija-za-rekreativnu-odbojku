package fer.zavrsni.Odbojka.service.impl;

import fer.zavrsni.Odbojka.dao.SlikaRepository;
import fer.zavrsni.Odbojka.domain.Slika;
import fer.zavrsni.Odbojka.domain.Teren;
import fer.zavrsni.Odbojka.service.SlikaService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SlikaServiceJpa implements SlikaService {
    @Autowired
    private SlikaRepository slikaRepo;

    @Override
    @Transactional
    public void createSlika(List<MultipartFile> slike, Teren t) {
        int i = 0;
        if (slike == null) return;
        for (i = 0; slike.size() > i; i = i + 1) {
            Slika sl = new Slika();
            sl.setTeren(t);
            sl.setIme_slike(sl.upload(slike.get(i), t.getŠifra_terena() + "_" + UUID.randomUUID().toString(), "tereni"));
            t.getSlike().add(sl);
            slikaRepo.save(sl);
        }
    }

    @Override
    @Transactional
    public Optional<Slika> findByŠifraSlike(Long slika) {
        return slikaRepo.findByŠifraSlike(slika);
    }

    @Override
    @Transactional
    public void delete(Slika slik) {
        slik.izbrisi(slik.getIme_slike(), "tereni");
        slik.getTeren().getSlike().remove(slik);
        slikaRepo.delete(slik);
    }

}
