package org.generation.italy.javita158.controllers.tropico;

import org.generation.italy.javita158.model.tropico.Resource;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/resource") // Questo controller si rende disponibile a ricevere tutte le chiamate che cominciano
                                 // con questa sringa (sarà il dispatcher servlet a smistare le richieste guardando a quali
                                 // sono accettate da quali Controller)
public class ResourceController {

    private static int idGenerator = 3;

    private static Map<Integer, Resource> resources = new HashMap<>(
            Map.of(
                    1, new Resource(1, "steel", BigDecimal.TWO, true),
                    2, new Resource(2, "wood", BigDecimal.TWO, true)
            ));

    @GetMapping // Questo metodo rispode alle get, ma se la richiesta chima solo l'indirizzo del controller risponde sempre questo
                // metodo (volendo posso specificare l'indirizzo specifico a cui risponde questo metodo come parametro dell'annotazione)
    // Questo metodo ritornerà tutte le risorse
    public Collection<Resource> getResources() {
        return resources.values();
    }


    // Questo metodo ritornerà una risorda con un certo id
    @GetMapping("/{id}")
    public Resource findById (@PathVariable int id){
        return resources.get(id);
    }

    @PutMapping("/{id}")
    public Resource update (@PathVariable int id, @RequestBody Resource newResource) {
        Resource originResource = resources.get(id);

        originResource.setName(newResource.getName());
        originResource.setBaseMarketPrice(newResource.getBaseMarketPrice());
        originResource.setProcessed(newResource.getProcessed());

        return originResource;
    }

    @DeleteMapping("/{id}")
    public void delete (@PathVariable int id) {
        resources.remove(id);
    }

    @PostMapping
    public Resource create (@RequestBody Resource r) {
        int id = idGenerator++;
        r.setId(id);
        resources.put(id, r);
        return r;
    }


}
