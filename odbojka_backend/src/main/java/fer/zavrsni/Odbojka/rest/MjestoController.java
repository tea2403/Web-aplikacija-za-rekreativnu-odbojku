package fer.zavrsni.Odbojka.rest;

import fer.zavrsni.Odbojka.domain.Mjesto;
import fer.zavrsni.Odbojka.service.MjestoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

@RestController
@RequestMapping("/mjesta")
public class MjestoController {
    @Autowired
    private MjestoService mjestoService;

    @GetMapping("/")
    public List<Mjesto> popis() {
        return mjestoService.abecedno();
    }
}
