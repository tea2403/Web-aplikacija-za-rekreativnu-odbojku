package fer.zavrsni.Odbojka.domain;

import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Osoba {
    @Id
    @GeneratedValue
    private Long šifra_osobe;
    @Column(nullable=false)
    @NotNull
    private String ime;
    @Column(nullable=false)
    @NotNull
    private String prezime;
    @Column(nullable=false)
    @NotNull
    private String lozinka;
    @Column(unique=true, nullable=false)
    @NotNull
    private String email;
    private String broj_mobitela;

    @OneToOne(mappedBy = "osoba", cascade = CascadeType.ALL)
    private Profilna profilna;
    @ManyToOne
    @JoinColumn(name="pbr_osoba")
    private Mjesto mjesto;
    @OneToMany(mappedBy = "osoba", cascade = CascadeType.ALL)
    private Set<Tema> teme = new HashSet<>();

    @OneToMany(mappedBy = "iznajmljivac", cascade = CascadeType.ALL)
    private Set<Teren> tereni = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "obavijesti",
            joinColumns = @JoinColumn(name = "šifra_osobe"),
            inverseJoinColumns = @JoinColumn(name = "šifra_teme"))
    private Set<Tema> obavijesti;

    private String spol;

    @Temporal(TemporalType.DATE)
    private Date datum_rođenja;

    private Integer početak_igranja;

    @OneToMany(mappedBy = "igrac", cascade = CascadeType.ALL)
    private Set<Trenirao> treniranja = new HashSet<>();

    @OneToMany(mappedBy = "kapetan", cascade = CascadeType.ALL)
    private Set<Tim> kapetanTimova = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "igra_poziciju",
            joinColumns = @JoinColumn(name = "šifra_osobe"),
            inverseJoinColumns = @JoinColumn(name = "šifra_pozicije"))
    private Set<Pozicija> pozicije = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "pripada",
            joinColumns = @JoinColumn(name = "šifra_osobe"),
            inverseJoinColumns = @JoinColumn(name = "šifra_tima"))
    private Set<Tim> timovi = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "rezervirao_igrač",
            joinColumns = @JoinColumn(name = "šifra_osobe"),
            inverseJoinColumns = @JoinColumn(name = "šifra_termina"))
    private Set<Termin> termini = new HashSet<>();
    public Long getŠifra_osobe() {
        return šifra_osobe;
    }

    public void setŠifra_osobe(Long šifra_osobe) {
        this.šifra_osobe = šifra_osobe;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBroj_mobitela() {
        return broj_mobitela;
    }

    public void setBroj_mobitela(String broj_mobitela) {
        this.broj_mobitela = broj_mobitela;
    }

    public Profilna getProfilna() {
        return profilna;
    }

    public void setProfilna(Profilna profilna) {
        this.profilna = profilna;
    }

    public Mjesto getMjesto() {
        return mjesto;
    }

    public void setMjesto(Mjesto mjesto) {
        this.mjesto = mjesto;
    }

    public Set<Tema> getTeme() {
        return teme;
    }

    public void setTeme(Set<Tema> teme) {
        this.teme = teme;
    }

    public Set<Tema> getObavijesti() {
        return obavijesti;
    }

    public void setObavijesti(Set<Tema> obavijesti) {
        this.obavijesti = obavijesti;
    }

    public Set<Teren> getTereni() {
        return tereni;
    }

    public void setTereni(Set<Teren> tereni) {
        this.tereni = tereni;
    }

    public String getSpol() {
        return spol;
    }

    public void setSpol(String spol) {
        this.spol = spol;
    }

    public Date getDatum_rođenja() {
        return datum_rođenja;
    }

    public void setDatum_rođenja(Date datum_rođenja) {
        this.datum_rođenja = datum_rođenja;
    }

    public Integer getPočetak_igranja() {
        return početak_igranja;
    }

    public void setPočetak_igranja(Integer početak_igranja) {
        this.početak_igranja = početak_igranja;
    }

    public Set<Trenirao> getTreniranja() {
        return treniranja;
    }

    public void setTreniranja(Set<Trenirao> treniranja) {
        this.treniranja = treniranja;
    }

    public Set<Tim> getKapetanTimova() {
        return kapetanTimova;
    }

    public void setKapetanTimova(Set<Tim> kapetanTimova) {
        this.kapetanTimova = kapetanTimova;
    }

    public Set<Pozicija> getPozicije() {
        return pozicije;
    }

    public void setPozicije(Set<Pozicija> pozicije) {
        this.pozicije = pozicije;
    }

    public Set<Tim> getTimovi() {
        return timovi;
    }

    public void setTimovi(Set<Tim> timovi) {
        this.timovi = timovi;
    }

    public Set<Termin> getTermini() {
        return termini;
    }

    public void setTermini(Set<Termin> termini) {
        this.termini = termini;
    }

    @Override
    public String toString() {
        return "Osoba{" +
                "šifra_osobe=" + šifra_osobe +
                ", ime='" + ime + '\'' +
                ", prezime='" + prezime + '\'' +
                ", lozinka='" + lozinka + '\'' +
                ", e_mail='" + email + '\'' +
                ", broj_mobitela='" + broj_mobitela + '\'' +
                '}';
    }
}
