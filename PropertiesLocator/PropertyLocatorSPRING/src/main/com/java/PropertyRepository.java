package com.java;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository

public interface PropertyRepository extends JpaRepository<PropertyEntity, Long>{

	@Query("SELECT DISTINCT p FROM PropertyEntity p WHERE p.sourceName = :source AND p.sourceKey = :key")
	public PropertyEntity findBySourceAndKey(@Param("source") String source, @Param("key") String key);
	
    @Transactional
    @Modifying
    @Query("DELETE FROM PropertyEntity p WHERE p.sourceName = :source AND p.sourceKey = :key")
    public void deleteBySourceAndKey(@Param("source") String source, @Param("key") String key);
	
}
