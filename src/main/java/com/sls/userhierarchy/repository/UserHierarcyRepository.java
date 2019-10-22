package com.sls.userhierarchy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sls.userhierarchy.DTO.UserHierarchyIdsDTO;
import com.sls.userhierarchy.entity.UserHierarchy;

public interface UserHierarcyRepository extends JpaRepository<UserHierarchy, Long> {

	UserHierarchy findByHierarcyId(long id);
	
	//for hr manager
	@Query("select distinct h.hrManager.id from UserHierarchy h where h.orgHead.id = :orgHead")
	List<Long> findHierarcyByOrgHead(@Param("orgHead") long orgHead);
	
	@Query("select distinct h.hrManager.id from UserHierarchy h where h.groupHead.id = :groupHead and h.orgHead.id = :orgHead")
	List<Long> findHierarcyHrManagerByGroupHead(@Param("groupHead") long groupHead, @Param("orgHead") long orgHead);
	
	
	//for org head
	@Query("select distinct  h.orgHead.id from UserHierarchy h where h.groupHead.id = :groupHead")
	List<Long> findHierarcyByGroupHead(@Param("groupHead") long groupHead);
	
	@Query("select distinct  h.orgHead.id from UserHierarchy h where h.groupHead.id = :groupHead")
	List<Long> findHierarcyByOrgHeadGroupHead(@Param("groupHead") long groupHead);
	
	
	
	@Query("select distinct h from UserHierarchy h where h.user.id = :id")
	List<UserHierarchy> findHierarchyByUserId(@Param("id") long id);
	
	
	// for pri supervisor
	@Query("select distinct  h.priSupervisor.id from UserHierarchy h where h.hrManager.id = :hrManager")
	List<Long> findHierarchyByHrManager(@Param("hrManager") long hrManager);
	
	@Query("select distinct h.priSupervisor.id from UserHierarchy h where h.groupHead.id = :groupHead and h.orgHead.id = :orgHead and h.hrManager.id = :hrManager")
	List<Long> findHierarcyPriSupervisorByOrgHead(@Param("groupHead") long groupHead, @Param("orgHead") long orgHead, @Param("hrManager") long hrManager);
	
	
	//for prj supervisor
	@Query("select distinct  h.prjSupervisor.id from UserHierarchy h where h.priSupervisor.id = :priSupervisor")
	List<Long> findHierarchyBypriSupervisor(@Param("priSupervisor") long priSupervisor);
	
	@Query("select distinct  h.prjSupervisor.id from UserHierarchy h where h.groupHead.id = :groupHead and h.orgHead.id = :orgHead and h.hrManager.id = :hrManager and h.priSupervisor.id = :priSupervisor")
	List<Long> findHierarchyPrjSupervisorByGroupHead(@Param("groupHead") long groupHead, @Param("orgHead") long orgHead, @Param("hrManager") long hrManager,@Param("priSupervisor") long priSupervisor);
	
	
	//for user
	@Query("select distinct  h.user.id from UserHierarchy h where h.prjSupervisor.id = :prjSupervisor")
	List<Long> findHierarchyByPrjSupervisor(@Param("prjSupervisor") long prjSupervisor);
	
	@Query("select distinct  h.user.id from UserHierarchy h where h.groupHead.id = :groupHead and h.orgHead.id = :orgHead and h.hrManager.id = :hrManager and h.priSupervisor.id = :priSupervisor and h.prjSupervisor.id = :prjSupervisor")
	List<Long> findHierarchyUserByGroupHead(@Param("groupHead") long groupHead, @Param("orgHead") long orgHead, @Param("hrManager") long hrManager,@Param("priSupervisor") long priSupervisor, @Param("prjSupervisor") long prjSupervisor);
	
	
	
	@Query("select distinct  h from UserHierarchy h where h.parentOrgId.id = :parentOrgId")
	List<UserHierarchy> findByParentOrgId(@Param("parentOrgId") long parentOrgId);
	
	@Query("select distinct  h from UserHierarchy h where h.orgId.id = :orgId")
	List<UserHierarchy> findByOrgId(@Param("orgId") long orgId);
	
	@Query("select distinct  h from UserHierarchy h where h.hrManager.id = :hrManager")
	List<UserHierarchy> findByHrId(@Param("hrManager") long hrManager);
	
	@Query("select distinct  h from UserHierarchy h where h.priSupervisor.id = :priSupervisor")
	List<UserHierarchy> findByPrSuperId(@Param("priSupervisor") long priSupervisor);
	
	@Query("select distinct  h from UserHierarchy h where h.prjSupervisor.id = :prjSupervisor")
	List<UserHierarchy> findByPrjSuperId(@Param("prjSupervisor") long prjSupervisor);
	
	@Query("select distinct  h from UserHierarchy h where h.user.id = :user")
	UserHierarchy findByUserId(@Param("user") long user);
	
	@Query("select distinct  h from UserHierarchy h where h.groupHead.id = :groupHead")
	List<UserHierarchy> findByGroupHead(@Param("groupHead") long groupHead);
	
	@Query("select distinct  h from UserHierarchy h where h.orgHead.id = :orgHead")
	List<UserHierarchy> findByOrgHead(@Param("orgHead") long orgHead);
	
	@Query("select distinct  h from UserHierarchy h where h.orgHead.id = :orgHead and h.hrManager.id = :hrManager")
	List<UserHierarchy> findByHrAndOrgHead(@Param("orgHead") long orgHead, @Param("hrManager") long hrManager);
	
	@Query("select distinct  h from UserHierarchy h where h.orgHead.id = :orgHead and h.hrManager.id = :hrManager and h.priSupervisor.id = :priSupervisor")
	List<UserHierarchy> findByHrAndOrgHeadAndPriSuper(@Param("orgHead") long orgHead, @Param("hrManager") long hrManager, @Param("priSupervisor") long priSupervisor);
	
	@Query("select distinct  h from UserHierarchy h where h.orgHead.id = :orgHead and h.hrManager.id = :hrManager and h.priSupervisor.id = :priSupervisor and h.prjSupervisor.id = :prjSupervisor")
	List<UserHierarchy> findByHrAndOrgHeadAndPriSuperAndPrjSuper(@Param("orgHead") long orgHead, @Param("hrManager") long hrManager, @Param("priSupervisor") long priSupervisor, @Param("prjSupervisor") long prjSupervisor);
	
	//for all 
//	@Query("select distinct  h.orgHead.id, h.hrManager.id, h.priSupervisor.id, h.prjSupervisor.id, h.user.id from UserHierarchy h where h.groupHead.id = :groupHead")
//	List<UserHierarchyIdsDTO> findAllHierarchyByPrjSupervisor(@Param("groupHead") long groupHead);
	
	
		
}
