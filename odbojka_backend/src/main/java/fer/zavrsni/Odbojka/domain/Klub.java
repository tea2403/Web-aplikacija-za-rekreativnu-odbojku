package fer.zavrsni.Odbojka.domain;

import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Klub {
    @Id
    @GeneratedValue
    private Long šifra_kluba;

    @Column(nullable=false)
    @NotNull
    private String ime_kluba;

    @ManyToOne
    @JoinColumn(name="pbr_kluba", nullable=false)
    private Mjesto mjesto;
    @OneToMany(mappedBy = "klub", cascade = CascadeType.ALL)
    private Set<Trenirao> treniranja = new HashSet<>();
    public Long getŠifra_kluba() {
        return šifra_kluba;
    }

    public void setŠifra_kluba(Long šifra_kluba) {
        this.šifra_kluba = šifra_kluba;
    }

    public String getIme_kluba() {
        return ime_kluba;
    }

    public void setIme_kluba(String ime_kluba) {
        this.ime_kluba = ime_kluba;
    }

    public Mjesto getMjesto() {
        return mjesto;
    }

    public void setMjesto(Mjesto mjesto) {
        this.mjesto = mjesto;
    }

    public Set<Trenirao> getTreniranja() {
        return treniranja;
    }

    public void setTreniranja(Set<Trenirao> treniranja) {
        this.treniranja = treniranja;
    }
}
