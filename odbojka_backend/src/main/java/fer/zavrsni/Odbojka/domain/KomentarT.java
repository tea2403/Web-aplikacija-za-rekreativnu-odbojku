package fer.zavrsni.Odbojka.domain;

import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.sql.Timestamp;

@Entity
public class KomentarT {
    @Id
    @GeneratedValue
    private Long šifra_komentaraT;

    @Column(nullable=false)
    @NotNull
    private String tekstT;

    @Column(nullable=false)
    @NotNull
    private Timestamp vrijeme_komentaraT;

    @ManyToOne
    @JoinColumn(name="šifra_autora", nullable=false)
    private Osoba osoba;

    @ManyToOne
    @JoinColumn(name="šifra_termina", nullable=false)
    private Termin termin;

    public Long getŠifra_komentaraT() {
        return šifra_komentaraT;
    }

    public void setŠifra_komentaraT(Long šifra_komentaraT) {
        this.šifra_komentaraT = šifra_komentaraT;
    }

    public String getTekstT() {
        return tekstT;
    }

    public void setTekstT(String tekstT) {
        this.tekstT = tekstT;
    }

    public Timestamp getVrijeme_komentaraT() {
        return vrijeme_komentaraT;
    }

    public void setVrijeme_komentaraT(Timestamp vrijeme_komentaraT) {
        this.vrijeme_komentaraT = vrijeme_komentaraT;
    }

    public Osoba getOsoba() {
        return osoba;
    }

    public void setOsoba(Osoba osoba) {
        this.osoba = osoba;
    }

    public Termin getTermin() {
        return termin;
    }

    public void setTermin(Termin termin) {
        this.termin = termin;
    }
}
