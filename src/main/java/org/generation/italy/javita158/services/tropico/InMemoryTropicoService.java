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
import java.util.Optional;

@Service
public class InMemoryTropicoService implements TropicoService {

	private static int idGenerator = 3;

	private static Map<Integer, Resource> resources = new HashMap<>(
			Map.of(
					1, new Resource(1, "steel", BigDecimal.TWO, true),
					2, new Resource(2, "wood", BigDecimal.TWO, true)
			)
	);

	@Override
	public Collection<Resource> getResources() {
		return resources.values();
	}

	@Override
	public Optional<Resource> findById(@PathVariable int id){
		return Optional.ofNullable(resources.get(id));
	}

	@Override
	public boolean update(@PathVariable int id, @RequestBody Resource newResource) {
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
	public boolean delete(@PathVariable int id) {
		Resource old = resources.remove(id);
		return old != null;
	}

	@Override
	public Resource create(@RequestBody Resource r) {
		int id = idGenerator++;
		r.setId(id);
		resources.put(id, r);
		return r;
	}
}
