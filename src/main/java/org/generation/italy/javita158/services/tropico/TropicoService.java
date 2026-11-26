package org.generation.italy.javita158.services.tropico;

import org.generation.italy.javita158.model.tropico.Faction;
import org.generation.italy.javita158.model.tropico.Resource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface TropicoService {
    Collection<Resource> getResources();

    Optional<Resource> findResourceById(@PathVariable int id);

    boolean updateResource(@PathVariable int id, @RequestBody Resource newResource);

    boolean deleteResource(@PathVariable int id);

    Resource createResource(@RequestBody Resource r);

    Collection<Faction> getFactions();

    Optional<Faction> findFactionById(@PathVariable int id);

    Faction createFaction(@RequestBody Faction faction);

    boolean updateFaction(@PathVariable int id, @RequestBody Faction newFaction);

    boolean deleteFaction(@PathVariable int id);

    Collection<Faction> findFactionsByNameStartingWith(@RequestParam String prefix);

    List<Faction> findFactionsByMinimumMembers(@PathVariable int n);

    List<Faction> findFactionsByMinimumRebelMembers(@PathVariable int n);

    List<Faction> findFactionsWithRebelMembers();

    //List<Resource> findResourcesByBuildingTypes(String... names);

    //int mergeFactions(@PathVariable int sourceFactionId, @PathVariable int targetFactionId);
}
