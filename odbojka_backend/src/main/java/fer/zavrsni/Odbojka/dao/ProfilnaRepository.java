package fer.zavrsni.Odbojka.dao;

import fer.zavrsni.Odbojka.domain.Profilna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProfilnaRepository extends JpaRepository<Profilna, Long> {
    @Query("SELECT p FROM Profilna p WHERE p.ime_profilne = :naziv")
    Optional<Profilna> findByImeProfilne(@Param("naziv")String naziv);

    @Modifying
    @Query("DELETE FROM Profilna p WHERE p.ime_profilne = :imeProfilne")
    void deleteByImeProfilne(@Param("imeProfilne") String imeProfilne);
}
