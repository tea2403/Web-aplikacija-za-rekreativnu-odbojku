package fer.zavrsni.Odbojka.dao;


import fer.zavrsni.Odbojka.domain.Tim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TimRepository extends JpaRepository<Tim, Long> {
    @Query("SELECT COUNT(t) FROM Tim t WHERE t.naziv_tima = :naziv")
    int countByNaziv_timaIgnoreCase(@Param("naziv")String naziv);

    @Query("SELECT t FROM Tim t WHERE t.naziv_tima = :naziv")
    Optional<Tim> findByNazivTima(@Param("naziv")String naziv);

    @Query("SELECT t FROM Tim t WHERE t.šifra_tima = :tim")
    Optional<Tim> findByŠifraTima(@Param("tim") Long tim);
}
