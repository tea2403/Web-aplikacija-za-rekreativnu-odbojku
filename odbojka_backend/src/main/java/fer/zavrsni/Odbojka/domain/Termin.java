package fer.zavrsni.Odbojka.domain;

import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Termin {
    @Id
    @GeneratedValue
    private Long šifra_termina;

    @Column(nullable=false)
    @NotNull
    private Timestamp početak;

    @Column(nullable=false)
    @NotNull
    private Timestamp kraj;
    @Column(nullable=false)
    @NotNull
    private Float cijena;
    @ManyToOne
    @JoinColumn(name="šifra_terena", nullable=false)
    private Teren teren;

    @OneToMany(mappedBy = "termin", cascade = CascadeType.ALL)
    private Set<KomentarT> komentari = new HashSet<>();
    @ManyToMany(mappedBy = "termini")
    private Set<Osoba> igraci = new HashSet<>();
    @ManyToMany(mappedBy = "termini")
    private Set<Tim> timovi = new HashSet<>();
    public Long getŠifra_termina() {
        return šifra_termina;
    }

    public void setŠifra_termina(Long šifra_termina) {
        this.šifra_termina = šifra_termina;
    }

    public Timestamp getPocetak() {
        return početak;
    }

    public void setPocetak(Timestamp pocetak) {
        this.početak = pocetak;
    }

    public Timestamp getKraj() {
        return kraj;
    }

    public void setKraj(Timestamp kraj) {
        this.kraj = kraj;
    }

    public Float getCijena() {
        return cijena;
    }

    public void setCijena(Float cijena) {
        this.cijena = cijena;
    }

    public Teren getTeren() {
        return teren;
    }

    public void setTeren(Teren teren) {
        this.teren = teren;
    }

    public Set<KomentarT> getKomentari() {
        return komentari;
    }

    public void setKomentari(Set<KomentarT> komentari) {
        this.komentari = komentari;
    }


    public Set<Osoba> getIgraci() {
        return igraci;
    }

    public void setIgraci(Set<Osoba> igraci) {
        this.igraci = igraci;
    }

    public Set<Tim> getTimovi() {
        return timovi;
    }

    public void setTimovi(Set<Tim> timovi) {
        this.timovi = timovi;
    }

}
