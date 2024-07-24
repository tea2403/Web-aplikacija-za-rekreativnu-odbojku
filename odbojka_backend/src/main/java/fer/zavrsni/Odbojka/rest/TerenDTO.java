package fer.zavrsni.Odbojka.rest;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class TerenDTO {
    private String napomena;
    private String pbr;
    private String rekviziti;
    private String adresa;
    private String broj_ljudi;
    public TerenDTO(){}

    public TerenDTO(String adresa, String pbr, String maxbroj, String rekviziti, String napomena) {
        this.adresa = adresa;
        this.pbr = pbr;
        this.broj_ljudi = maxbroj;
        this.rekviziti = rekviziti;
        this.napomena = napomena;
    }



    public String getNapomena() {
        return napomena;
    }

    public void setNapomena(String napomena) {
        this.napomena = napomena;
    }

    public String getPbr() {
        return pbr;
    }

    public void setPbr(String pbr) {
        this.pbr = pbr;
    }

    public String getRekviziti() {
        return rekviziti;
    }

    public void setRekviziti(String rekviziti) {
        this.rekviziti = rekviziti;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getBroj_ljudi() {
        return broj_ljudi;
    }

    public void setBroj_ljudi(String broj_ljudi) {
        this.broj_ljudi = broj_ljudi;
    }
}
