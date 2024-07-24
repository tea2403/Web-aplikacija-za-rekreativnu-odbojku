package fer.zavrsni.Odbojka.dao;

import fer.zavrsni.Odbojka.domain.Slika;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SlikaRepository extends JpaRepository<Slika, Long> {
    @Query("SELECT t FROM Slika t WHERE t.šifra_slike = :slika")
    Optional<Slika> findByŠifraSlike(@Param("slika") Long slika);
}
