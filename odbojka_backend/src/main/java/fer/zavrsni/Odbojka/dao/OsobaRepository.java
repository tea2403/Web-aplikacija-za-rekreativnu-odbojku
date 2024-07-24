package fer.zavrsni.Odbojka.dao;

import fer.zavrsni.Odbojka.domain.Osoba;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OsobaRepository extends JpaRepository<Osoba, Long> {

        Optional<Osoba> findByEmail(String email);
        int countByEmail(String email);

}
