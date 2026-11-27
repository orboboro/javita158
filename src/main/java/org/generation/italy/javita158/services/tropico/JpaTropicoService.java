package org.generation.italy.javita158.services.tropico;

import org.generation.italy.javita158.model.tropico.Resource;
import org.generation.italy.javita158.repositories.tropico.ResourceRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

@Service
@Profile("jpa")
public class JpaTropicoService implements TropicoService {

    private final ResourceRepository resRepo;

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
    @Transactional
    public boolean update(int id, Resource newResource) {
        Optional<Resource> toUpdate = resRepo.findById(id);
        if (toUpdate.isEmpty()) {
            return false;
        }
        Resource existing = toUpdate.get();
        existing.setName(newResource.getName());
        existing.setBaseMarketPrice(newResource.getBaseMarketPrice());
        existing.setProcessed(newResource.getProcessed());
        resRepo.save(existing);
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
