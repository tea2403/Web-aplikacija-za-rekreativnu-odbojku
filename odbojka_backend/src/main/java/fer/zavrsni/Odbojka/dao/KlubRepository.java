package fer.zavrsni.Odbojka.dao;

import fer.zavrsni.Odbojka.domain.Klub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface KlubRepository extends JpaRepository<Klub, Long> {
    @Query("SELECT k FROM Klub k WHERE k.ime_kluba = :naziv")
    Optional<Klub> findByNazivKluba(@Param("naziv") String naziv);
}
