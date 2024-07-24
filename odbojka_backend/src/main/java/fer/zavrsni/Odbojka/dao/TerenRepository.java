package fer.zavrsni.Odbojka.dao;

import fer.zavrsni.Odbojka.domain.Teren;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TerenRepository extends JpaRepository<Teren, Long> {
    @Query("SELECT COUNT(t) FROM Teren t WHERE t.šifra_terena = :teren")
    int countByŠifraTerena(@Param("teren") Long teren);

    @Query("SELECT t FROM Teren t WHERE t.šifra_terena = :teren")
    Optional<Teren> findByŠifraTerena(@Param("teren")Long teren);
}
