package com.flavormetrics.api.repository;

import com.flavormetrics.api.entity.Tag;
import com.flavormetrics.api.model.projection.TagProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    @Query("""
            SELECT t.id AS id,
                   t.name AS name
            FROM Tag t
            WHERE t.name IN (?1)
            """)
    List<TagProjection> getIdsAndNames(List<String> names);

}
