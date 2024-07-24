package fer.zavrsni.Odbojka.service;

import fer.zavrsni.Odbojka.domain.Mjesto;

import java.util.List;
import java.util.Optional;

public interface MjestoService {
    List<Mjesto> abecedno();

    Optional<Mjesto> findByPbr(Long pbr);
}
