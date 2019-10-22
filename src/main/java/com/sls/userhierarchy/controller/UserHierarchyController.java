package com.sls.userhierarchy.controller;

import static com.sls.userhierarchy.constant.UserManagementConstant.USER_HOST_SERVER;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sls.userhierarchy.DTO.GroupHeadDTO;
import com.sls.userhierarchy.DTO.UserHierarcyDTO;
import com.sls.userhierarchy.services.serviceimpl.UserHierarcyServiceImpl;
import com.sls.userhierarchy.services.serviceimpl.UserHierarcyServiceImpl2;

/*
 * Service and RestController class for UserHierarchy
 */
@RestController
@RequestMapping("/easybusiness/userhierarcy/")
@CrossOrigin(origins = USER_HOST_SERVER)
public class UserHierarchyController {
	
	@Autowired
	UserHierarcyServiceImpl hierarchyService;
	
	@Autowired
	UserHierarcyServiceImpl2 hierarchyService2;
	
	@GetMapping("getByHierarcyId/{hierarcyId}")
	public ResponseEntity<UserHierarcyDTO> getHierarcyById(@PathVariable("hierarcyId") Long hierarcyId){
		return hierarchyService.getHierarcyById(hierarcyId);
	}
	
	@GetMapping("getAllHierarcy")
	public List<UserHierarcyDTO> populateHierarcyList(){
		return hierarchyService.populateHierarcyList();
	}
	
	@GetMapping("getGroupHead/{id}")
	public GroupHeadDTO groupHead(@PathVariable("id") long id) {
		return hierarchyService.groupHead(id);
	}
	
	@GetMapping("hierarchy/{id}")
	public GroupHeadDTO hierarchy(@PathVariable("id") long id) {
		return hierarchyService.hierarchy(id);
	}
	
	@GetMapping("getAllHierarchy")
	public GroupHeadDTO populateHierarcyList2(){
		return hierarchyService.hierarchyy();
	}

}
