package org.generation.italy.javita158.services.tropico;

import org.generation.italy.javita158.model.tropico.Resource;
import org.generation.italy.javita158.repositories.tropico.ResourceRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class JpaTropicoService implements TropicoService {

    private ResourceRepository resRepo;

    public JpaTropicoService(ResourceRepository resRepo) {
        this.resRepo = resRepo;
        IO.println(resRepo.getClass().getName());
    }

    @Override
    public Collection<Resource> getResources() {
        return resRepo.findAll();
    }

    @Override
    public Optional<Resource> findById(int id) {
        return resRepo.findById(id);
    }

    @Override
    public boolean update(int id, Resource newResource) {
        Optional<Resource> toUpdate = resRepo.findById(id);
        if (toUpdate.isEmpty()) {
            return false;
        }
        resRepo.save(newResource);
        return true;
    }

    @Override
    public boolean delete(int id) {
        Optional<Resource> toDelete = resRepo.findById(id);
        if (toDelete.isEmpty()) {
            return false;
        }
        resRepo.delete(toDelete.get());
        return true;
    }

    @Override
    public Resource create(Resource r) {
        return resRepo.save(r);
    }
}
