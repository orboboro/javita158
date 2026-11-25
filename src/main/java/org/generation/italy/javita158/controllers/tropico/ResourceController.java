package org.generation.italy.javita158.controllers.tropico;

import org.generation.italy.javita158.dtos.tropico.ResourceDTO;
import org.generation.italy.javita158.model.tropico.Resource;
import org.generation.italy.javita158.services.tropico.InMemoryTropicoService;
import org.generation.italy.javita158.services.tropico.TropicoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Collection;
import java.util.Optional;


@RestController
@RequestMapping("/api/resource") // Questo controller si rende disponibile a ricevere tutte le chiamate che cominciano
                                 // con questa sringa (sarà il dispatcher servlet a smistare le richieste guardando a quali
                                 // sono accettate da quali Controller)
public class ResourceController {
    // BAD: private InMemoryTropicoService service = new InMemoryTropicoService();

    private TropicoService service;

    //Non possiamo dargli il costruttore di default perchè Spring farebbe confusione (Ambiguità)
    public ResourceController(TropicoService service) {
        this.service = service;
    }

    @GetMapping // Questo metodo rispode alle get, ma se la richiesta chima solo l'indirizzo del controller risponde sempre questo
                // metodo (volendo posso specificare l'indirizzo specifico a cui risponde questo metodo come parametro dell'annotazione)
    // Questo metodo ritorna tutte le risorse.
    public ResponseEntity<Collection<ResourceDTO>> getResources() {
        Collection<Resource> resources = service.getResources();
        Collection<ResourceDTO> dtoCol = resources.stream()
                .map(ResourceDTO :: new).toList();
        return ResponseEntity.ok(dtoCol);
    }

    @GetMapping("/{id}") // Metodo che risponde a richieste con indirizzo "/api/resource/" + un id che gli specifichiamo
    // Questo metodo ritorna la risorsa con l'id specificato.
    //Non ritorniamo solo il DTO, ma anche il codice di risposta e una ResponseEntity ingloba entrambe le cose
    public ResponseEntity<ResourceDTO> findById(@PathVariable int id) { // L'annotazione dice: il parametro id va preso dalla parte del path identificata da "id"
         Optional<Resource> or = service.findById(id);
         if(or.isEmpty()) {
             //notFound() ritorna un builder (costruttore) di ResponseEntity e per darmi il ResponseEntity bisogna richiamare build()
             //dietro questa riga si nasconde un DesignPattern, chiamato Builder.
             return ResponseEntity.notFound().build();
         }
         Resource r = or.get();
         ResourceDTO rd = new ResourceDTO(r);
         return ResponseEntity.ok(rd); //.ok() significa che voglio mandare una ResponseEntity con codice 200
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable int id, @RequestBody Resource newResource) {
        if(!service.update(id, newResource)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build(); //se non gli passiamo nulla nell'ok ci vuole il build() dopo
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
