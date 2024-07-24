package fer.zavrsni.Odbojka.service.impl;

import fer.zavrsni.Odbojka.dao.KlubRepository;
import fer.zavrsni.Odbojka.domain.*;
import fer.zavrsni.Odbojka.rest.KlubDTO;
import fer.zavrsni.Odbojka.service.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class KlubServiceJpa implements KlubService {
    @Autowired
    private KlubRepository klubRepo;
    @Autowired
    private MjestoService mjestoSer;
    @Autowired
    private OsobaService osobaSer;
    @Autowired
    private TreniraoService trSer;
    @Override
    @Transactional
    public void dodajKlubove(List<KlubDTO> zahtjev, String username) {
        Optional<Osoba> igrac = osobaSer.findByEmail(username);
        Set<Trenirao> popis = new HashSet<>(igrac.get().getTreniranja());
        for (Trenirao tr : popis){
            int zast = 0;
            for (KlubDTO klubDTO : zahtjev) {
                if(tr.getKlub().getIme_kluba().equals(klubDTO.getKlub())) {
                    zast = 1;
                    break;
                }
            }
            if (zast == 0) {
                Klub kl = tr.getKlub();
                trSer.izbrisiTrenirao(tr, igrac.get(), kl);
                if (kl.getTreniranja().isEmpty()) izbriKlub(kl);
            }
        }
        for (KlubDTO klubDTO : zahtjev) {
            Optional<Klub> klub = klubRepo.findByNazivKluba(klubDTO.getKlub());
            Klub kl;
            if (klub.isEmpty()){
                Klub kl1 = new Klub();
                kl1.setIme_kluba(klubDTO.getKlub());
                Optional<Mjesto> mj = mjestoSer.findByPbr(Long.valueOf(klubDTO.getPbr()));
                kl1.setMjesto(mj.get());
                klubRepo.save(kl1);
                kl = kl1;
            } else kl = klub.get();
            TreniraoKljuc tk = new TreniraoKljuc();
            tk.setŠifra_kluba(kl.getŠifra_kluba());
            tk.setŠifra_osobe(igrac.get().getŠifra_osobe());
            trSer.createTrenirao(tk, klubDTO.getOd_godina(), klubDTO.getDo_godina(), igrac.get(), kl);
        }
    }

    @Override
    @Transactional
    public void izbriKlub(Klub kl) {
        klubRepo.delete(kl);
    }
}
