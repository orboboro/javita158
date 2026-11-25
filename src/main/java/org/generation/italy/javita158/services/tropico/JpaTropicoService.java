package org.generation.italy.javita158.services.tropico;

import org.generation.italy.javita158.model.tropico.Resource;
import org.generation.italy.javita158.repositories.tropico.ResourceRepository;

import java.util.Collection;
import java.util.List;

public class JpaTropicoService implements TropicoService {

    private ResourceRepository resRepo;

    public JpaTropicoService(ResourceRepository resRepo) {
        this.resRepo = resRepo;
        IO.println(resRepo.getClass().getName());
    }

    @Override
    public Collection<Resource> getResources() {
        return resRepo.;
    }

    @Override
    public Resource findById(int id) {
        return null;
    }

    @Override
    public Resource update(int id, Resource newResource) {
        return null;
    }

    @Override
    public void delete(int id) {

    }

    @Override
    public Resource create(Resource r) {
        return null;
    }
}
