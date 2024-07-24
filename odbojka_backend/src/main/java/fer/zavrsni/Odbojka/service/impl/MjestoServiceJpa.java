package fer.zavrsni.Odbojka.service.impl;

import fer.zavrsni.Odbojka.dao.MjestoRepository;
import fer.zavrsni.Odbojka.domain.Mjesto;
import fer.zavrsni.Odbojka.service.MjestoService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.Collator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MjestoServiceJpa implements MjestoService {
    @Autowired
    private MjestoRepository mjestoRepo;
    @Transactional
    @Override
    public List<Mjesto> abecedno() {
        Collator collator = Collator.getInstance(new Locale("hr", "HR"));
        return mjestoRepo.findAll().stream()
                .sorted((grad1, grad2) -> collator.compare(grad1.getNaziv_mjesta(), grad2.getNaziv_mjesta()))
                .collect(Collectors.toList());
    }
    @Override
    @Transactional
    public Optional<Mjesto> findByPbr(Long pbr) {
        return mjestoRepo.findByPbr(pbr);
    }

}
