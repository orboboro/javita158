package org.generation.italy.javita158.controllers.tropico;

import org.generation.italy.javita158.dtos.tropico.ResourceDTO;
import org.generation.italy.javita158.model.tropico.Resource;
import org.generation.italy.javita158.services.tropico.InMemoryTropicoService;
import org.generation.italy.javita158.services.tropico.TropicoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/resource") // Questo controller si rende disponibile a ricevere tutte le chiamate che cominciano
                                 // con questa sringa (sarà il dispatcher servlet a smistare le richieste guardando a quali
                                 // sono accettate da quali Controller)
public class ResourceController {

    // BAD! private InMemoryTropicoService service = new InMemoryTropicoService();
    private TropicoService service;

    public ResourceController(TropicoService service){
        this.service = service;
    }

    @GetMapping // Questo metodo rispode alle get, ma se la richiesta chima solo l'indirizzo del controller risponde sempre questo
                // metodo (volendo posso specificare l'indirizzo specifico a cui risponde questo metodo come parametro dell'annotazione)
    // Questo metodo ritornerà tutte le risorse
    public ResponseEntity<Collection<ResourceDTO>> getResources() {
        Collection<Resource> resCol = service.getResources();
        Collection<ResourceDTO> dtoCol = resCol.stream().map(ResourceDTO::new).toList();
        return ResponseEntity.ok(dtoCol);
    }


    // Questo metodo ritornerà una risorda con un certo id
    @GetMapping("/{id}")
    public ResponseEntity<ResourceDTO> findById (@PathVariable int id){
        Optional<Resource> or =  service.findById(id);
        if(or.isEmpty()){
            return ResponseEntity.notFound().build(); // design pattern "BUILDER"
        }
        Resource r = or.get();
        ResourceDTO rd = new ResourceDTO(r);
        return ResponseEntity.ok(rd);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update (@PathVariable int id, @RequestBody Resource newResource) {
        if(!service.update(id, newResource)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public void delete (@PathVariable int id) {
        service.delete(id);
    }

    @PostMapping
    public Resource create (@RequestBody Resource r) {
        return service.create(r);
    }


}
