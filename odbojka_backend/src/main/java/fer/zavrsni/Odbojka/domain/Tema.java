package fer.zavrsni.Odbojka.domain;

import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Tema {
    @Id
    @GeneratedValue
    private Long šifra_teme;

    @Column(nullable=false, unique = true)
    @NotNull
    private String naslov;
    @Column(nullable=false)
    @NotNull
    private Timestamp vrijeme_stvaranja;
    @ManyToOne
    @JoinColumn(name="šifra_osobe", nullable=false)
    private Osoba osoba;
    @OneToMany(mappedBy = "tema", cascade = CascadeType.ALL)
    private Set<KomentarF> komentari = new HashSet<>();
    @ManyToMany(mappedBy = "obavijesti")
    private Set<Osoba> obavijesti = new HashSet<>();
    public Long getŠifra_teme() {
        return šifra_teme;
    }
    public void setŠifra_teme(Long šifra_teme) {
        this.šifra_teme = šifra_teme;
    }

    public String getNaslov() {
        return naslov;
    }

    public void setNaslov(String naslov) {
        this.naslov = naslov;
    }

    public Timestamp getVrijeme_stvaranja() {
        return vrijeme_stvaranja;
    }

    public void setVrijeme_stvaranja(Timestamp vrijeme_stvaranja) {
        this.vrijeme_stvaranja = vrijeme_stvaranja;
    }

    public Osoba getOsoba() {
        return osoba;
    }

    public void setOsoba(Osoba osoba) {
        this.osoba = osoba;
    }

    public Set<KomentarF> getKomentari() {
        return komentari;
    }

    public void setKomentari(Set<KomentarF> komentari) {
        this.komentari = komentari;
    }

    public Set<Osoba> getObavijesti() {
        return obavijesti;
    }

    public void setObavijesti(Set<Osoba> obavijesti) {
        this.obavijesti = obavijesti;
    }
}
