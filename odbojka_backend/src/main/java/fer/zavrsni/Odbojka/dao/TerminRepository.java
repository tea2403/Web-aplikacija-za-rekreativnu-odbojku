package fer.zavrsni.Odbojka.dao;

import fer.zavrsni.Odbojka.domain.Termin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TerminRepository extends JpaRepository<Termin, Long> {
    @Query("SELECT t FROM Termin t WHERE t.šifra_termina = :termin")
    Optional<Termin> findByŠifraTermina(@Param("termin") Long termin);

    @Query("SELECT t FROM Termin t LEFT JOIN Teren ter ON t.teren = ter LEFT JOIN Mjesto mj ON ter.mjesto = mj WHERE mj.pbr = :pbr AND t.početak >= CURRENT_TIMESTAMP ORDER BY t.početak ASC")
    List<Termin> findAllSortedByPocetakGradAsc(@Param("pbr") Long pbr);
}
