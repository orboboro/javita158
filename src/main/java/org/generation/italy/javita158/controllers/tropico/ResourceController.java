package org.generation.italy.javita158.controllers.tropico;

import org.generation.italy.javita158.model.tropico.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/resource") // Questo controller si rende disponibile a ricevere tutte le chiamate che cominciano
                                 // con questa sringa (sarà il dispatcher servlet a smistare le richieste guardando a quali
                                 // sono accettate da quali Controller)
public class ResourceController {

    @GetMapping // Questo metodo rispode alle get, ma se la richiesta chima solo l'indirizzo del controller risponde sempre questo
                // metodo (volendo posso specificare l'indirizzo specifico a cui risponde questo metodo come parametro dell'annotazione)
    public Resource getResource() {
        return new Resource(1, "steel", BigDecimal.TWO, true);
    }


}
