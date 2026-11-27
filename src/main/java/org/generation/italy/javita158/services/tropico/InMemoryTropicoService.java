package org.generation.italy.javita158.services.tropico;

import org.generation.italy.javita158.model.tropico.Resource;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service // Comunica a spring che la classe seguente è una componente iniettabile, così che qaundo incontra un costruttore che si aspetta una variabile da iniettare di questo tipo, capisce che deve creare  einiettare un oggetto di questo tipo
@Profile("in-memory")
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
    public Optional<Resource> findById(int id) {
        return Optional.ofNullable(resources.get(id));
    }

    @Override
    public boolean update(int id, Resource newResource) {

        Resource originResource = resources.get(id);
        if (originResource == null) {
            return false;
        }
        originResource.setName(newResource.getName());
        originResource.setBaseMarketPrice(newResource.getBaseMarketPrice());
        originResource.setProcessed(newResource.getProcessed());

        return true;
    }

    @Override
    public boolean delete(int id) {
        Resource old = resources.remove(id);
        return old != null;
    }

    @Override
    public Resource create(Resource r) {

        int id = idGenerator++;
        r.setId(id);
        resources.put(id, r);
        return r;
    }
}
