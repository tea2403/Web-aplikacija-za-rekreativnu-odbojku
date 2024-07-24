package fer.zavrsni.Odbojka.domain;

import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Teren {
    @Id
    @GeneratedValue
    private Long šifra_terena;

    @Column(nullable=false)
    @NotNull
    private String adresa;
    private String napomena;

    @Column(nullable=false)
    @NotNull
    private Boolean rekviziti;

    @Column(nullable=false)
    @NotNull
    private Integer broj_ljudi;

    @ManyToOne
    @JoinColumn(name="šifra_iznajmljivača", nullable=false)
    private Osoba iznajmljivac;
    @OneToMany(mappedBy = "teren", cascade = CascadeType.ALL)
    private Set<Termin> termini = new HashSet<>();
    @ManyToOne
    @JoinColumn(name="pbr_terena", nullable=false)
    private Mjesto mjesto;
    @OneToMany(mappedBy = "teren", cascade = CascadeType.ALL)
    private Set<Slika> slike = new HashSet<>();
    public Long getŠifra_terena() {
        return šifra_terena;
    }

    public void setŠifra_terena(Long šifra_terena) {
        this.šifra_terena = šifra_terena;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getNapomena() {
        return napomena;
    }

    public void setNapomena(String napomena) {
        this.napomena = napomena;
    }

    public Boolean getRekviziti() {
        return rekviziti;
    }

    public void setRekviziti(Boolean rekviziti) {
        this.rekviziti = rekviziti;
    }

    public Integer getBroj_ljudi() {
        return broj_ljudi;
    }

    public void setBroj_ljudi(Integer broj_ljudi) {
        this.broj_ljudi = broj_ljudi;
    }

    public Osoba getIznajmljivac() {
        return iznajmljivac;
    }

    public void setIznajmljivac(Osoba iznajmljivac) {
        this.iznajmljivac = iznajmljivac;
    }

    public Mjesto getMjesto() {
        return mjesto;
    }

    public void setMjesto(Mjesto mjesto) {
        this.mjesto = mjesto;
    }

    public Set<Slika> getSlike() {
        return slike;
    }

    public void setSlike(Set<Slika> slike) {
        this.slike = slike;
    }

    public Set<Termin> getTermini() {
        return termini;
    }

    public void setTermini(Set<Termin> termini) {
        this.termini = termini;
    }
}
