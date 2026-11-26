package org.generation.italy.javita158.controllers.tropico;

import org.generation.italy.javita158.dtos.tropico.FactionDTO;
import org.generation.italy.javita158.model.tropico.Faction;
import org.generation.italy.javita158.services.tropico.TropicoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/api/faction")
public class FactionController {
    private TropicoService service;

    public FactionController(TropicoService service){
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Collection<FactionDTO>> getFactions(){
        Collection<Faction> facCol = service.getFactions();
        Collection<FactionDTO> dtoCol = facCol.stream()
                                              .map(FactionDTO::new)
                                              .toList();
        return ResponseEntity.ok(dtoCol);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FactionDTO> findById(@PathVariable int id) {
        Optional<Faction> of = service.findFactionById(id);
        if(of.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        Faction f = of.get();
        FactionDTO fd = new FactionDTO(f);
        return ResponseEntity.ok(fd);
    }

    @PostMapping
    public ResponseEntity<FactionDTO> create(@RequestBody Faction faction){
        Faction f = service.createFaction(faction);
        FactionDTO fd = new FactionDTO(f);
        return ResponseEntity.ok(fd);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable int id, @RequestBody Faction newFaction){
        if(!service.updateFaction(id, newFaction)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        if(!service.deleteFaction(id)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Collection<FactionDTO>> findByNameStartingWith(
            @RequestParam(required = false, defaultValue = "") String prefix) {
        Collection<Faction> facCol = service.findFactionsByNameStartingWith(prefix);
        Collection<FactionDTO> dtoCol = facCol.stream()
                                              .map(FactionDTO :: new)
                                              .toList();
        return ResponseEntity.ok(dtoCol);
    }
}
