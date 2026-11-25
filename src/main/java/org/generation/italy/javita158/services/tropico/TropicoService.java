package org.generation.italy.javita158.services.tropico;

import org.generation.italy.javita158.model.tropico.Resource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collection;
import java.util.Optional;

public interface TropicoService {
    Collection<Resource> getResources();

    Optional<Resource> findById(@PathVariable int id);

    boolean update(@PathVariable int id, @RequestBody Resource newResource);

    boolean delete(@PathVariable int id);

    Resource create(@RequestBody Resource r);
}
