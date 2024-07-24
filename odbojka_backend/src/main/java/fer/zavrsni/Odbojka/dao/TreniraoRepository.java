package fer.zavrsni.Odbojka.dao;

import fer.zavrsni.Odbojka.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TreniraoRepository extends JpaRepository<Trenirao, TreniraoKljuc> {
    @Override
    Optional<Trenirao> findById(TreniraoKljuc treniraoKljuc);
}
