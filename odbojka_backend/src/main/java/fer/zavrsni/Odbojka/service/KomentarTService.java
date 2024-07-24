package fer.zavrsni.Odbojka.service;

public interface KomentarTService {
    void createKomT(String termin, String kom, String username);

    void izbrisiKomT(String termin, String id, String username, String uloga);
}
