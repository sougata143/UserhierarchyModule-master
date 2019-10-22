package com.sls.userhierarchy.services;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.sls.userhierarchy.DTO.GroupHeadDTO;
import com.sls.userhierarchy.DTO.UserHierarcyDTO;

public interface UserHierarcyService {

	public ResponseEntity<UserHierarcyDTO> getHierarcyById(Long id);
	public List<UserHierarcyDTO> populateHierarcyList();
	public GroupHeadDTO groupHead(long id) ;
	public GroupHeadDTO hierarchy(long id) ;
	public GroupHeadDTO hierarchyy();
	
}
