package fer.zavrsni.Odbojka.domain;

import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;
@Entity
public class Profilna extends Media{
    @Id
    @GeneratedValue
    private Long šifra_profilne;
    @Column(nullable=false, unique = true)
    @NotNull
    private String ime_profilne;
    @OneToOne
    @JoinColumn(name = "šifra_osobe", referencedColumnName = "šifra_osobe", nullable=false)
    private Osoba osoba;
    public Long getŠifra_profilne() {
        return šifra_profilne;
    }

    public void setŠifra_profilne(Long šifra_profilne) {
        this.šifra_profilne = šifra_profilne;
    }

    public String getIme_profilne() {
        return ime_profilne;
    }

    public void setIme_profilne(String ime_profilne) {
        this.ime_profilne = ime_profilne;
    }

    public Osoba getOsoba() {
        return osoba;
    }

    public void setOsoba(Osoba osoba) {
        this.osoba = osoba;
    }

    @Override
    public String toString() {
        return "Profilna{" +
                "šifra_profilne=" + šifra_profilne +
                ", ime_profilne='" + ime_profilne + '\'' +
                '}';
    }
}
