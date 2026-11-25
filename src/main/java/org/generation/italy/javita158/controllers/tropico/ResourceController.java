package org.generation.italy.javita158.controllers.tropico;

import org.generation.italy.javita158.model.tropico.Resource;
import org.generation.italy.javita158.services.tropico.InMemoryTropicoService;
import org.generation.italy.javita158.services.tropico.TropicoService;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/resource") // Questo controller si rende disponibile a ricevere tutte le chiamate che cominciano
                                 // con questa sringa (sarà il dispatcher servlet a smistare le richieste guardando a quali
                                 // sono accettate da quali Controller)
public class ResourceController {

    //private InMemoryTropicoService service = new InMemoryTropicoService();  BAD
    private TropicoService service;

    public ResourceController(TropicoService service) {
        this.service = service;
    }

    @GetMapping // Questo metodo rispode alle get, ma se la richiesta chima solo l'indirizzo del controller risponde sempre questo
                // metodo (volendo posso specificare l'indirizzo specifico a cui risponde questo metodo come parametro dell'annotazione)
    // Questo metodo ritorna tutte le risorse.
    public Collection<Resource> getResources(TropicoService service) {
        return service.getResources();
    }

    @GetMapping("/{id}") // Metodo che risponde a richieste con indirizzo "/api/resource/" + un id che gli specifichiamo
    // Questo metodo ritorna la risorsa con l'id specificato.
    public Resource findById(@PathVariable int id) { // L'annotazione dice: il parametro id va preso dalla parte del path identificata da "id"
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public Resource update(@PathVariable int id, @RequestBody Resource newResource) {
        return service.update(id, newResource);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        service.delete(id);
    }

    @PostMapping
    public Resource create(@RequestBody Resource r) { // Il parametro va preso dal body della richiesta
        return service.create(r);
    }




}
