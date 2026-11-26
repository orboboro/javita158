package org.generation.italy.javita158.services.tropico;

import org.generation.italy.javita158.model.tropico.Faction;
import org.generation.italy.javita158.model.tropico.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.*;

@Service    //comunica a Spring che la classe seguente è una componente iniettabile
            //cosi quando incontra il costruttore che si aspetta una variabile da iniettare di questo tipo, capisce che è questa la classe che funge da servizio
//Si può usare anche @Component
public class InMemoryTropicoService implements TropicoService {
    private static int idGenerator = 3;
    private static Map<Integer, Resource> resources = new HashMap<>(
            Map.of(
                    1, new Resource(1, "steel", BigDecimal.TWO, true),
                    2, new Resource(2, "coal", BigDecimal.ONE, false)
            )
    );

    @Override
    public Collection<Resource> getResources() {
        return resources.values();
    }

    @Override
    public Optional<Resource> findResourceById(@PathVariable int id) { // L'annotazione dice: il parametro id va preso dalla parte del path identificata da "id"
        return Optional.ofNullable(resources.get(id));
    }

    @Override
    public boolean updateResource(@PathVariable int id, @RequestBody Resource newResource) {

        Resource originResource = resources.get(id);
        if(originResource == null){
            return false;
        }
        originResource.setName(newResource.getName());
        originResource.setBaseMarketPrice(newResource.getBaseMarketPrice());
        originResource.setProcessed(newResource.getProcessed());

        return true;
    }

    @Override
    public boolean deleteResource(@PathVariable int id) {
        Resource old = resources.remove(id);
        return old != null;
    }

    @Override
    public Resource createResource(@RequestBody Resource r) { // Il parametro va preso dal body della richiesta

        int id = idGenerator++;
        r.setId(id);
        resources.put(id, r);
        return r;
    }

    @Override
    public Collection<Faction> getFactions() {
        return List.of();
    }

    @Override
    public Optional<Faction> findFactionById(int id) {
        return Optional.empty();
    }

    @Override
    public Faction createFaction(Faction faction) {
        return null;
    }

    @Override
    public boolean updateFaction(int id, Faction newFaction) {
        return false;
    }

    @Override
    public boolean deleteFaction(int id) {
        return false;
    }

    @Override
    public List<Faction> findFactionsByNameStartingWith(String prefix) {
        return List.of();
    }

    @Override
    public List<Faction> findFactionsByMinimumMembers(int n) {
        return List.of();
    }

    @Override
    public List<Faction> findFactionsByMinimumRebelMembers(int n) {
        return List.of();
    }

    @Override
    public List<Faction> findFactionsWithRebelMembers() {
        return List.of();
    }

}
