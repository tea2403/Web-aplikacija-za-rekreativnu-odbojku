package fer.zavrsni.Odbojka.dao;


import fer.zavrsni.Odbojka.domain.Tema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TemaRepository extends JpaRepository<Tema, Long> {
    Optional<Tema> findByNaslov(String naslov);

    int countByNaslov(String naslov);
    @Query("SELECT t FROM Tema t LEFT JOIN KomentarF k ON t = k.tema " +
            "WHERE k.šifra_komentaraF = (SELECT k2.šifra_komentaraF FROM KomentarF k2 " +
            "WHERE k2.tema = t ORDER BY k2.vrijeme_komentaraF DESC LIMIT 1) " +
            "ORDER BY k.vrijeme_komentaraF DESC")
    List<Tema> findAllSortedByVrijemeStvaranjaDesc();
}
