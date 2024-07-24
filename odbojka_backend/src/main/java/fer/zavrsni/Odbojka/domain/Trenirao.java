package fer.zavrsni.Odbojka.domain;


import jakarta.persistence.*;

@Entity
public class Trenirao {
    @EmbeddedId
    private TreniraoKljuc id;

    @ManyToOne
    @MapsId("šifra_osobe")
    @JoinColumn(name = "šifra_osobe", nullable = false)
    private Osoba igrac;

    @ManyToOne()
    @MapsId("šifra_kluba")
    @JoinColumn(name = "šifra_kluba", nullable = false)
    private Klub klub;

    private Integer od_godina;
    private Integer do_godina;

    public TreniraoKljuc getId() {
        return id;
    }

    public void setId(TreniraoKljuc id) {
        this.id = id;
    }

    public Osoba getIgrac() {
        return igrac;
    }

    public void setIgrac(Osoba igrac) {
        this.igrac = igrac;
    }

    public Klub getKlub() {
        return klub;
    }

    public void setKlub(Klub klub) {
        this.klub = klub;
    }

    public Integer getOd_godina() {
        return od_godina;
    }

    public void setOd_godina(Integer od_godina) {
        this.od_godina = od_godina;
    }

    public Integer getDo_godina() {
        return do_godina;
    }

    public void setDo_godina(Integer do_godina) {
        this.do_godina = do_godina;
    }
}
