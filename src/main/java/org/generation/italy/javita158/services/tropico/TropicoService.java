package org.generation.italy.javita158.services.tropico;

import org.generation.italy.javita158.model.tropico.Resource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collection;

public interface TropicoService {
    Collection<Resource> getResources();

    Resource findById(@PathVariable int id);

    Resource update(@PathVariable int id, @RequestBody Resource newResource);

    void delete(@PathVariable int id);

    Resource create(@RequestBody Resource r);
}
