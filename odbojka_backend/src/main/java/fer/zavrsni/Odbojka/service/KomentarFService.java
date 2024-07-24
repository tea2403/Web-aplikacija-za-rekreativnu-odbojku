package fer.zavrsni.Odbojka.service;

public interface KomentarFService {
    void createKomF(String naslov, String kom, String username);

    void izbrisiKom(String naslov, String kom, String username, String uloga);
}
