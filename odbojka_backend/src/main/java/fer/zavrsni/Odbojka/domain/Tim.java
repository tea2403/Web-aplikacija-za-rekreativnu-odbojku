package fer.zavrsni.Odbojka.domain;

import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Tim {
    @Id
    @GeneratedValue
    private Long šifra_tima;

    @Column(nullable=false, unique = true)
    @NotNull
    private String naziv_tima;

    @ManyToOne
    @JoinColumn(name="šifra_kapetana", nullable=false)
    private Osoba kapetan;
    @ManyToMany(mappedBy = "timovi")
    private Set<Osoba> igraci = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "rezervirao_tim",
            joinColumns = @JoinColumn(name = "šifra_tima"),
            inverseJoinColumns = @JoinColumn(name = "šifra_termina"))
    private Set<Termin> termini = new HashSet<>();

    public Long getŠifra_tima() {
        return šifra_tima;
    }

    public void setŠifra_tima(Long šifra_tima) {
        this.šifra_tima = šifra_tima;
    }

    public String getNaziv_tima() {
        return naziv_tima;
    }

    public void setNaziv_tima(String naziv_tima) {
        this.naziv_tima = naziv_tima;
    }

    public Osoba getKapetan() {
        return kapetan;
    }

    public void setKapetan(Osoba kapetan) {
        this.kapetan = kapetan;
    }

    public Set<Osoba> getIgraci() {
        return igraci;
    }

    public void setIgraci(Set<Osoba> igraci) {
        this.igraci = igraci;
    }

    public Set<Termin> getTermini() {
        return termini;
    }

    public void setTermini(Set<Termin> termini) {
        this.termini = termini;
    }
}
