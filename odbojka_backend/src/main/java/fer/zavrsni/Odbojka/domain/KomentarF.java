package fer.zavrsni.Odbojka.domain;

import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.sql.Timestamp;

@Entity
public class KomentarF {
    @Id
    @GeneratedValue
    private Long šifra_komentaraF;

    @Column(nullable=false)
    @NotNull
    private String tekstF;

    @Column(nullable=false)
    @NotNull
    private Timestamp vrijeme_komentaraF;

    @ManyToOne
    @JoinColumn(name="šifra_autora", nullable=false)
    private Osoba osoba;

    @ManyToOne
    @JoinColumn(name="šifra_teme", nullable=false)
    private Tema tema;

    public Long getŠifra_komentaraF() {
        return šifra_komentaraF;
    }

    public void setŠifra_komentaraF(Long šifra_komentaraF) {
        this.šifra_komentaraF = šifra_komentaraF;
    }

    public String getTekstF() {
        return tekstF;
    }

    public void setTekstF(String tekstF) {
        this.tekstF = tekstF;
    }

    public Timestamp getVrijeme_komentaraF() {
        return vrijeme_komentaraF;
    }

    public void setVrijeme_komentaraF(Timestamp vrijeme_komentaraF) {
        this.vrijeme_komentaraF = vrijeme_komentaraF;
    }

    public Osoba getOsoba() {
        return osoba;
    }

    public void setOsoba(Osoba osoba) {
        this.osoba = osoba;
    }

    public Tema getTema() {
        return tema;
    }

    public void setTema(Tema tema) {
        this.tema = tema;
    }
}
