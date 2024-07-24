package fer.zavrsni.Odbojka.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class TreniraoKljuc implements Serializable {
    @Column(name = "šifra_osobe")
    Long šifra_osobe;

    @Column(name = "šifra_kluba")
    Long šifra_kluba;

    public Long getŠifra_osobe() {
        return šifra_osobe;
    }

    public void setŠifra_osobe(Long šifra_osobe) {
        this.šifra_osobe = šifra_osobe;
    }

    public Long getŠifra_kluba() {
        return šifra_kluba;
    }

    public void setŠifra_kluba(Long šifra_kluba) {
        this.šifra_kluba = šifra_kluba;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TreniraoKljuc that = (TreniraoKljuc) o;
        return Objects.equals(šifra_osobe, that.šifra_osobe) && Objects.equals(šifra_kluba, that.šifra_kluba);
    }

    @Override
    public int hashCode() {
        return Objects.hash(šifra_osobe, šifra_kluba);
    }
}
