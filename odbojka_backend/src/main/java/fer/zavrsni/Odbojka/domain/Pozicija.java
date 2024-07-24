package fer.zavrsni.Odbojka.domain;

import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Pozicija {
    @Id
    @GeneratedValue
    private Long šifra_pozicije;

    @Column(nullable=false, unique = true)
    @NotNull
    private String naziv_pozicije;

    @ManyToMany(mappedBy = "pozicije")
    private Set<Osoba> igraci = new HashSet<>();

    public Long getŠifra_pozicije() {
        return šifra_pozicije;
    }

    public void setŠifra_pozicije(Long šifra_pozicije) {
        this.šifra_pozicije = šifra_pozicije;
    }

    public String getNaziv_pozicije() {
        return naziv_pozicije;
    }

    public void setNaziv_pozicije(String naziv_pozicije) {
        this.naziv_pozicije = naziv_pozicije;
    }

    public Set<Osoba> getIgraci() {
        return igraci;
    }

    public void setIgraci(Set<Osoba> igraci) {
        this.igraci = igraci;
    }
}
