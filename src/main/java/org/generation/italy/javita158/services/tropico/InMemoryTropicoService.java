package org.generation.italy.javita158.services.tropico;

import org.generation.italy.javita158.model.tropico.Resource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service // Comunica a spring che la classe seguente è una componente iniettabile, così che qaundo incontra un costruttore che si aspetta una variabile da iniettare di questo tipo, capisce che deve creare  einiettare un oggetto di questo tipo
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
    public Resource findById(@PathVariable int id) { // L'annotazione dice: il parametro id va preso dalla parte del path identificata da "id"
        return resources.get(id);
    }

    @Override
    public Resource update(@PathVariable int id, @RequestBody Resource newResource) {

        Resource originResource = resources.get(id);

        originResource.setName(newResource.getName());
        originResource.setBaseMarketPrice(newResource.getBaseMarketPrice());
        originResource.setProcessed(newResource.getProcessed());

        return originResource;
    }

    @Override
    public void delete(@PathVariable int id) {
        resources.remove(id);
    }

    @Override
    public Resource create(@RequestBody Resource r) { // Il parametro va preso dal body della richiesta

        int id = idGenerator++;
        r.setId(id);
        resources.put(id, r);
        return r;
    }
}
