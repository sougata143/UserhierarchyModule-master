package com.sls.userhierarchy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sls.userhierarchy.DTO.LocationMasterDTO;
import com.sls.userhierarchy.entity.LocationMaster;

public interface LocationMasterRepository extends JpaRepository<LocationMaster, Long> {
	
	@Query("select l from LocationMaster l where l.id = :id")
	LocationMasterDTO findById(@Param("id") Long id);

}
