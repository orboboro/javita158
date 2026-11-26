package org.generation.italy.javita158.repositories.tropico;

import org.generation.italy.javita158.model.tropico.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface ResourceRepository extends JpaRepository<Resource, Integer> {

    @Query("select distinct r from BuildingType bt join bt.outputResource r where bt.name in :names")
    Collection<Resource> findByProducedByBuildingType(@Param("names") List<String> names);
}
