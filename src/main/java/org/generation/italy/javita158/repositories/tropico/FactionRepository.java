package org.generation.italy.javita158.repositories.tropico;

import org.generation.italy.javita158.model.tropico.Faction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface FactionRepository extends JpaRepository<Faction, Integer> {

    @Query("from Faction f where f.name like :pattern escape '!'")
    Collection<Faction> findByNameStartingWith(@Param("pattern") String pattern);

}
