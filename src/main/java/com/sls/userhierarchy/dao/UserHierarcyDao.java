package com.sls.userhierarchy.dao;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sls.userhierarchy.DTO.UserHierarcyDTO;
import com.sls.userhierarchy.entity.User;
import com.sls.userhierarchy.entity.UserHierarchy;
import com.sls.userhierarchy.repository.UserHierarcyRepository;

/*
 * DAO class for USER_HIERARCY table. This class generates tree structure of organization structure
 */

@Component
public class UserHierarcyDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserDao.class);
    @Autowired
    DataSource dataSource;
    
    @Autowired
    UserHierarcyRepository hierarcyRepository;
    
    @Transactional
    public List<UserHierarchy> getAllHierarcy(){
    	List<UserHierarchy> hierarcyEmp = new ArrayList<>();
    	for(UserHierarchy hierarchy : hierarcyRepository.findAll()) {
    		hierarcyEmp.add(hierarchy);
    	}
    	
    	return hierarcyEmp;
    }
    
    @Transactional
    public UserHierarchy getHierarchyById(long id){
    	return hierarcyRepository.findByHierarcyId(id);
    }
    
    @Transactional
    public List<Long> getHierarchyByOrgHead(long orgHead){
    	return hierarcyRepository.findHierarcyByOrgHead(orgHead);
    }
    
    @Transactional
    public List<Long> getHierarchyHrManagerByGroupHead(long groupHead, long orgHead){
    	return hierarcyRepository.findHierarcyHrManagerByGroupHead(groupHead, orgHead);
    }
    
    @Transactional
    public List<Long> getHierarchyOrgHeadByGroupHead(long groupHead){
    	return hierarcyRepository.findHierarcyByOrgHeadGroupHead(groupHead);
    }
    
    @Transactional
    public List<Long> getHierarchyPrjSupervisorByGroupHead(long groupHead, long orgHead, long hrManager, long priSupervisor){
    	return hierarcyRepository.findHierarchyPrjSupervisorByGroupHead(groupHead, orgHead, hrManager, priSupervisor);
    }
    
    @Transactional
    public List<Long> getHierarchyUserByGroupHead(long groupHead, long orgHead, long hrManager, long priSupervisor,long prjSupervisor){
    	return hierarcyRepository.findHierarchyUserByGroupHead(groupHead, orgHead, hrManager, priSupervisor, prjSupervisor);
    }
    
    @Transactional
    public List<Long> getHierarchyUserByGroupHead(long prjSupervisor){
    	return hierarcyRepository.findHierarchyByPrjSupervisor( prjSupervisor);
    }
    
    @Transactional
    public List<Long> getHierarchyByGroupHead(long groupHead){
    	return hierarcyRepository.findHierarcyByGroupHead(groupHead);
    }
    
    @Transactional
    public List<Long> getHierarchyByHrManager(long hrManager){
    	return hierarcyRepository.findHierarchyByHrManager(hrManager);
    }
    
    @Transactional
    public List<Long> getHierarchyByPriSupervisor(long priSupervisor){
    	return hierarcyRepository.findHierarchyBypriSupervisor(priSupervisor);
    }
    
    @Transactional
    public List<Long> getHierarchyByPrjSupervisor(long prjSupervisor){
    	return hierarcyRepository.findHierarchyByPrjSupervisor(prjSupervisor);
    }
    
    @Transactional
    public List<Long> getHierarchypriSupervisorByOrgHead(long groupHead, long orgHead, long hrManager){
    	return hierarcyRepository.findHierarcyPriSupervisorByOrgHead(groupHead, orgHead, hrManager);
    }
    
    @Transactional
    public List<UserHierarchy> getHierarchyByUser(long id){
    	return hierarcyRepository.findHierarchyByUserId(id);
    }
    
    @Transactional
    public UserHierarchy getHierarchyById1(long id){
    	return hierarcyRepository.findOne(id);
    }
    
    @Transactional
    public void deleteHierarcy(long id) {
    	hierarcyRepository.delete(id);
    }
    
    @Transactional
    public void saveHierarcy(UserHierarchy hierarcy) {
    	hierarcyRepository.save(hierarcy);
    }
    
    @Transactional
    public void updateHierarcy(UserHierarcyDTO hierarcyDTO) {
    	hierarcyRepository.save(hierarcyRepository.findOne(hierarcyDTO.getHierarcyId()));
    }
    
    @Transactional
    public void update(UserHierarchy hierarcyDTO) {
    	hierarcyRepository.save(hierarcyDTO);
    }
    
    @Transactional
    public List<UserHierarchy> getHierarchyByParentOrgId(long id){
    	return hierarcyRepository.findByParentOrgId(id);
    }
    
    @Transactional
    public List<UserHierarchy> getHierarchyByOrgId(long id){
    	return hierarcyRepository.findByOrgId(id);
    }
    
    public List<UserHierarchy> getHierarchyByHrId(long id){
    	return hierarcyRepository.findByHrId(id);
    }
    
    public List<UserHierarchy> getHierarchyByPrSuperId(long id){
    	return hierarcyRepository.findByPrSuperId(id);
    }
    
    public List<UserHierarchy> getHierarchyByPrjSuperId(long id){
    	return hierarcyRepository.findByPrjSuperId(id);
    }
    
    public UserHierarchy getHierarchyByUserId(long id){
    	return hierarcyRepository.findByUserId(id);
    }
    
    public List<UserHierarchy> getHierarchyByGroupHeadId(long id){
    	return hierarcyRepository.findByGroupHead(id);
    }
    
    public List<UserHierarchy> getHierarchyByOrgHeads(long orgHead){
    	return hierarcyRepository.findByOrgHead(orgHead);
    }
    
    public List<UserHierarchy> getHierarchyByOrgHeadsAndHrManager(long orgHead, long hrManager){
    	return hierarcyRepository.findByHrAndOrgHead(orgHead, hrManager);
    }
    
    public List<UserHierarchy> getHierarchyByOrgHeadsAndHrManagerAndPriSuper(long orgHead, long hrManager, long priSupervisor){
    	return hierarcyRepository.findByHrAndOrgHeadAndPriSuper(orgHead, hrManager, priSupervisor);
    }
    
    public List<UserHierarchy> getHierarchyByOrgHeadsAndHrManagerAndPriSuperAndPrjSuper(long orgHead, long hrManager, long priSupervisor, long prjSuper){
    	return hierarcyRepository.findByHrAndOrgHeadAndPriSuperAndPrjSuper(orgHead, hrManager, priSupervisor, prjSuper);
    }
   
    @Autowired
	UserDao userDao;
    public List<User> getUser(long groupHead,long orgHead,long hrManager,long priSupervisor,long prjSupervisor){
    	List<Long> userids = 
    			hierarcyRepository.findHierarchyUserByGroupHead(groupHead,
    					orgHead, hrManager, priSupervisor, prjSupervisor);
    	
    	List<User> users = new ArrayList<>();
    	for(int i = 0 ; i < userids.size() ; i++) {
    		User user = new User();
    		user = userDao.findUserById(userids.get(i));
    		users.add(user);
    	}
    	return users;
    }
	
   /* @Transactional
    public List<UserHierarchyIdsDTO> getAllUserHierarchyByGroupHead(long groupHead){
    	return hierarcyRepository.findAllHierarchyByPrjSupervisor(groupHead);
    }*/
    
}
