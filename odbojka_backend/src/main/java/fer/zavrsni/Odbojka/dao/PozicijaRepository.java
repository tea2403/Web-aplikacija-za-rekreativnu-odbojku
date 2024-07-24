package fer.zavrsni.Odbojka.dao;


import fer.zavrsni.Odbojka.domain.Pozicija;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PozicijaRepository extends JpaRepository<Pozicija, Long> {
    @Query("SELECT p FROM Pozicija p WHERE p.naziv_pozicije = :poz")
    Optional<Pozicija> findByNazivPozicije(@Param("poz") String poz);
}
