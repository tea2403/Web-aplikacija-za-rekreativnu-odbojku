package fer.zavrsni.Odbojka.domain;

import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;

@Entity
public class Slika extends Media{
    @Id
    @GeneratedValue
    private Long šifra_slike;

    @Column(nullable=false, unique = true)
    @NotNull
    private String ime_slike;

    @ManyToOne
    @JoinColumn(name="šifra_terena", nullable=false)
    private Teren teren;

    public Long getŠifra_slike() {
        return šifra_slike;
    }

    public void setŠifra_slike(Long šifra_slike) {
        this.šifra_slike = šifra_slike;
    }

    public String getIme_slike() {
        return ime_slike;
    }

    public void setIme_slike(String ime_slike) {
        this.ime_slike = ime_slike;
    }

    public Teren getTeren() {
        return teren;
    }

    public void setTeren(Teren teren) {
        this.teren = teren;
    }
}
