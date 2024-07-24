package fer.zavrsni.Odbojka.service;

import fer.zavrsni.Odbojka.domain.*;

public interface TreniraoService {

    void createTrenirao(TreniraoKljuc tr, String od_godina, String do_godina, Osoba osoba, Klub klub);

    void izbrisiTrenirao(Trenirao tr, Osoba igrac, Klub kl);
}
