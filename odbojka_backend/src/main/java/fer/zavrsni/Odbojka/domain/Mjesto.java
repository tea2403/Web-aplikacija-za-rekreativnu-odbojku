package fer.zavrsni.Odbojka.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.antlr.v4.runtime.misc.NotNull;

@Entity
public class Mjesto {
    @Id
    private Long pbr;
    @Column(nullable=false)
    @NotNull
    private String naziv_mjesta;

    public Long getPbr() {
        return pbr;
    }

    public void setPbr(Long pbr) {
        this.pbr = pbr;
    }

    public String getNaziv_mjesta() {
        return naziv_mjesta;
    }

    public void setNaziv_mjesta(String naziv_mjesta) {
        this.naziv_mjesta = naziv_mjesta;
    }
}
