package org.generation.italy.javita158.services.tropico;

import org.generation.italy.javita158.model.tropico.Resource;

import java.util.Collection;
import java.util.Optional;

public interface TropicoService {
    Collection<Resource> getResources();

    Optional<Resource> findById(int id);

    boolean update(int id, Resource newResource);

    boolean delete(int id);

    Resource create(Resource r);
}
