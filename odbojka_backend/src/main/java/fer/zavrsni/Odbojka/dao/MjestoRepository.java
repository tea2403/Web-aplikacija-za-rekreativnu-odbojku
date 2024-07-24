package fer.zavrsni.Odbojka.dao;

import fer.zavrsni.Odbojka.domain.Mjesto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MjestoRepository extends JpaRepository<Mjesto, Long> {
    Optional<Mjesto> findByPbr(Long pbr);
}
