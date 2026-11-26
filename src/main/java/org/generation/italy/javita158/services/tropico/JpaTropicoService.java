package org.generation.italy.javita158.services.tropico;

import org.generation.italy.javita158.model.tropico.Faction;
import org.generation.italy.javita158.model.tropico.Resource;
import org.generation.italy.javita158.repositories.tropico.FactionRepository;
import org.generation.italy.javita158.repositories.tropico.ResourceRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class JpaTropicoService implements TropicoService{
    private ResourceRepository resRepo;
    private FactionRepository facRepo;

    public JpaTropicoService(ResourceRepository resRepo, FactionRepository facRepo) {
        this.resRepo = resRepo;
        IO.println(resRepo.getClass().getName());
        this.facRepo = facRepo;
    }


    @Override
    public Collection<Resource> getResources() {
        return resRepo.findAll();
    }

    @Override
    public Optional<Resource> findResourceById(int id) {
        return resRepo.findById(id);
    }

    @Override
    public boolean updateResource(int id, Resource newResource) {
        Optional<Resource> toUpdate = resRepo.findById(id);
        if(toUpdate.isEmpty()) {
            return false;
        }
        resRepo.save(newResource);
        return true;
    }

    @Override
    public boolean deleteResource(int id) {
        Optional<Resource> toDelete = resRepo.findById(id);
        if(toDelete.isEmpty()) {
            return false;
        }
        resRepo.delete(toDelete.get());
        return true;
    }

    @Override
    public Resource createResource(Resource r) {
        return resRepo.save(r);
    }

    @Override
    public Collection<Faction> getFactions() {
        return facRepo.findAll();
    }

    @Override
    public Optional<Faction> findFactionById(int id) {
        return facRepo.findById(id);
    }

    @Override
    public Faction createFaction(Faction faction) {
        return facRepo.save(faction);
    }

    @Override
    public boolean updateFaction(int id, Faction newFaction) {
        Optional<Faction> toUpdate = facRepo.findById(id);
        if(toUpdate.isEmpty()){
            return false;
        }
        facRepo.save(newFaction);
        return true;
    }

    @Override
    public boolean deleteFaction(int id) {
        Optional<Faction> toDelete = facRepo.findById(id);
        if(toDelete.isEmpty()){
            return false;
        }
        facRepo.delete(toDelete.get());
        return true;
    }

    @Override
    public Collection<Faction> findFactionsByNameStartingWith(String prefix) {
        if (prefix == null) {
            return facRepo.findAll();
        }
        String escaped = prefix.replace("!", "!!")
                .replace("%", "!%")
                .replace("_", "!_");
        return facRepo.findByNameStartingWith(escaped + "%");
    }

    @Override
    public List<Faction> findFactionsByMinimumMembers(int n) {
        return List.of();
    }

    @Override
    public List<Faction> findFactionsByMinimumRebelMembers(int n) {
        return List.of();
    }

    @Override
    public List<Faction> findFactionsWithRebelMembers() {
        return List.of();
    }
}
