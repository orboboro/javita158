package org.generation.italy.javita158.services.tropico;

import org.generation.italy.javita158.model.tropico.Resource;
import org.generation.italy.javita158.repositories.tropico.FactionRepository;
import org.generation.italy.javita158.repositories.tropico.ResourceRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service // Comunica a spring che la classe seguente è una componente iniettabile, così che qaundo incontra un costruttore che si aspetta una variabile da iniettare di questo tipo, capisce che deve creare  einiettare un oggetto di questo tipo
public class JPATropicoService implements TropicoService {

    private ResourceRepository resRepo;
    private FactionRepository facRepo;

    public JPATropicoService(ResourceRepository resRepo, FactionRepository facRepo) {
        this.resRepo = resRepo;
        this.facRepo = facRepo;
        IO.println(resRepo.getClass().getName()); // Stampa il nome della classe concreta che implementa questa interfaccia che verrà creata a runtime da Spring
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
        newResource.setId(id); // newResource arriva dal body JSON, e l'id della richiesta è nel path, non nel body. Quindi il suo campo id sarà probabilmente 0 o null, e save lo tratterebbe come un'entità nuova: invece di aggiornare la riga id, ne inserirebbe una nuova
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

    @Override
    public Collection<Resource> getResourcesByBuildingType(List<String> names) {
        return resRepo.findByProducedByBuildingType(names);
    }
}
