package com.sls.userhierarchy.dao;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sls.userhierarchy.DTO.LocationMasterDTO;
import com.sls.userhierarchy.entity.LocationMaster;
import com.sls.userhierarchy.repository.LocationMasterRepository;

/*
 * DAO class for LOCATION_MASTER table
 */

@Component
public class LocationMasterDao {
	
	@Autowired
	LocationMasterRepository locationRepository;
	
	@Transactional
	public LocationMaster getLocationById(long id) {
		return locationRepository.findOne(id);
	}

}
