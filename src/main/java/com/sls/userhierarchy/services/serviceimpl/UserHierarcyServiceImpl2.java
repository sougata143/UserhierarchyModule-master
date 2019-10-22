package com.sls.userhierarchy.services.serviceimpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.sls.userhierarchy.DTO.DepartmentDto;
import com.sls.userhierarchy.DTO.DesignationDto;
import com.sls.userhierarchy.DTO.GroupHeadDTO;
import com.sls.userhierarchy.DTO.HrManagerDTO;
import com.sls.userhierarchy.DTO.OrgHeadDTO;
import com.sls.userhierarchy.DTO.OrganizationDto;
import com.sls.userhierarchy.DTO.PriSupervisorDTO;
import com.sls.userhierarchy.DTO.PrjSupervisorDTO;
import com.sls.userhierarchy.DTO.UserHDTO;
import com.sls.userhierarchy.DTO.UserHierDTO;
import com.sls.userhierarchy.DTO.UserHierarcyDTO;
import com.sls.userhierarchy.dao.LocationMasterDao;
import com.sls.userhierarchy.dao.OrganizationDao;
import com.sls.userhierarchy.dao.UserDao;
import com.sls.userhierarchy.dao.UserHierarcyDao;
import com.sls.userhierarchy.entity.Organization;
import com.sls.userhierarchy.entity.User;
import com.sls.userhierarchy.entity.UserHierarchy;
import com.sls.userhierarchy.services.UserHierarcyService;

/*
 * Service and RestController class for UserHierarchy
 */
@Service
public class UserHierarcyServiceImpl2 {
	
	@Autowired
	UserHierarcyDao hierarcyDao;
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	OrganizationDao orgDao;
	
	@Autowired
	LocationMasterDao locationDao;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserHierarcyServiceImpl2.class);

	
	/*
	 * (non-Javadoc)
	 * @see com.sls.userhierarchy.services.userhierarcy.UserHierarcyService#groupHead()
	 * fetching hierarchy in nested manner
	 */
	@Transactional
	public GroupHeadDTO groupHead(long id) {
		long start = Calendar.getInstance().getTimeInMillis();
		List<UserHierarchy> groupHeadHierarcyList = hierarcyDao.getHierarchyByUser(id);	//fetching all hierarchies
		
		GroupHeadDTO groupHeadList = new GroupHeadDTO();
//		List<GroupHeadDTO> groupHeadDTOList = new ArrayList<>();
		
		List<OrgHeadDTO> orgHeadList = new ArrayList<>();
		List<UserHDTO> userList = new ArrayList<>();
		List<Long> orgHierarchyList = new ArrayList<>();
		List<HrManagerDTO> hrManagerList = new ArrayList<>();
		List<PriSupervisorDTO> priSupervisorList = new ArrayList<>();
		List<Long> priSupervisorHierarchyList = new ArrayList<>();
		List<PrjSupervisorDTO> prjSupervisorList = new ArrayList<>();
		List<Long> prjSupervisorHierarchyList = new ArrayList<>();
		List<Long> userHierarchyList = new ArrayList<>();
		List<Long> hrManagerHierarchyList = new ArrayList<>();
		
		long orgNum = 0;
		long hrNum = 0;
		long priSuNum = 0;
		long prjNum = 0;
		long uNum = 0;
		
		List<Long> user= new ArrayList<>();
		
		if(!groupHeadHierarcyList.isEmpty()) {
			for(int x = 0 ; x < groupHeadHierarcyList.size() ; x++) {
				
//				OrgHeadDTO orgHeads = new OrgHeadDTO();
				
				if(groupHeadHierarcyList.get(x).getOrgHead()!=null) {
					orgHierarchyList = hierarcyDao.getHierarchyOrgHeadByGroupHead(groupHeadHierarcyList.get(x).getGroupHead().getId())
							.stream()
							.distinct()
							.collect(Collectors.toList());
					orgNum = orgHierarchyList.size();
					
				}
				
				groupHeadList.setGroupHead(prepareUserDTO(groupHeadHierarcyList.get(x).getGroupHead()));
				
				if(!orgHierarchyList.isEmpty()) {
					//getting organization head
//					List<HrManagerDTO> hrManagerList = new ArrayList<>();
					for(int m = 0 ; m < orgHierarchyList.size() ; m++) {
						OrgHeadDTO orgHeads = new OrgHeadDTO();
						
						if(groupHeadHierarcyList.get(x).getHrManager()!=null && 
								groupHeadHierarcyList.get(x).getOrgHead()!=null) {
							hrManagerHierarchyList = 
									hierarcyDao.getHierarchyHrManagerByGroupHead(
											groupHeadHierarcyList.get(x).getGroupHead().getId(),
											orgHierarchyList.get(m))
									.stream()
									.distinct()
									.collect(Collectors.toList());
							
							hrNum = hrManagerHierarchyList.size();
							
						}

						orgHeads.setOrgHead(prepareUserDTO(userDao.findUserById(orgHierarchyList.get(m))));
						
						if(!hrManagerHierarchyList.isEmpty()) {
							//getting HR Manager (hrmanager)
//							List<PriSupervisorDTO> priSupervisorList = new ArrayList<>();
							for(int l = 0 ; l < hrManagerHierarchyList.size() ; l++) {
								HrManagerDTO hrManagers = new HrManagerDTO();
								
								if(groupHeadHierarcyList.get(x).getPriSupervisor()!=null && 
										groupHeadHierarcyList.get(x).getHrManager()!=null && 
										groupHeadHierarcyList.get(x).getOrgHead()!=null) {
									priSupervisorHierarchyList = 
											hierarcyDao.getHierarchypriSupervisorByOrgHead(
													groupHeadHierarcyList.get(x).getGroupHead().getId(),
													orgHierarchyList.get(m),
													hrManagerHierarchyList.get(l))
											.stream()
											.distinct()
											.collect(Collectors.toList());
									priSuNum = priSupervisorHierarchyList.size();
								}
								
								hrManagers.setHrManager(prepareUserDTO(userDao.findUserById(hrManagerHierarchyList.get(l))));
								
								
								if(!priSupervisorHierarchyList.isEmpty()) {
									//geting primary supervisor (priSupervisor)
//									List<PrjSupervisorDTO> prjSupervisorList = new ArrayList<>();
									for(int k = 0 ; k < priSupervisorHierarchyList.size() ; k++) {
										PriSupervisorDTO priSupervisors = new PriSupervisorDTO();
//										
										if(groupHeadHierarcyList.get(x).getPrjSupervisor()!=null && 
												groupHeadHierarcyList.get(x).getPriSupervisor()!=null && 
												groupHeadHierarcyList.get(x).getHrManager()!=null && 
												groupHeadHierarcyList.get(x).getOrgHead()!=null) {
											prjSupervisorHierarchyList =
													hierarcyDao.getHierarchyPrjSupervisorByGroupHead(
															groupHeadHierarcyList.get(x).getGroupHead().getId(),
															orgHierarchyList.get(m),
															hrManagerHierarchyList.get(l),
															priSupervisorHierarchyList.get(k))
													.stream()
													.distinct()
													.collect(Collectors.toList());
											prjNum = prjSupervisorHierarchyList.size();
										}
										
										priSupervisors.setPriSupervisor(prepareUserDTO(userDao.findUserById(priSupervisorHierarchyList.get(k))));
										
										if(!prjSupervisorHierarchyList.isEmpty()) {
											//setting project supervisor (prjsupervisor)
											
											for(int j = 0 ; j < prjSupervisorHierarchyList.size() ; j++) {

												PrjSupervisorDTO prjSupervisors = new PrjSupervisorDTO();
												
												if(groupHeadHierarcyList.get(x).getUser()!=null &&
														groupHeadHierarcyList.get(x).getPrjSupervisor()!=null && 
														groupHeadHierarcyList.get(x).getPriSupervisor()!=null && 
														groupHeadHierarcyList.get(x).getHrManager()!=null &&
														groupHeadHierarcyList.get(x).getOrgHead()!=null) {
													
													userHierarchyList =
															hierarcyDao.getHierarchyUserByGroupHead(
																	groupHeadHierarcyList.get(x).getGroupHead().getId(),
																	orgHierarchyList.get(m),
																	hrManagerHierarchyList.get(l),
																	priSupervisorHierarchyList.get(k),
																	prjSupervisorHierarchyList.get(j))
															.stream()
															.distinct()
															.collect(Collectors.toList());
													uNum = userHierarchyList.size();
												}
												
												prjSupervisors.setPrjSupervisor(prepareUserDTO(userDao.findUserById(prjSupervisorHierarchyList.get(j))));
												
												//setting users list
												if(!userHierarchyList.isEmpty()) {
//													List<UserHDTO> userList = new ArrayList<>();
													for(int i = 0 ; i < userHierarchyList.size() ; i++) {
														UserHDTO users = new UserHDTO();
														User userEntity = userDao.findUserById(userHierarchyList
																.stream()
																.distinct()
																.collect(Collectors.toList()).get(i));
														users.setUser(prepareUserDTO(userEntity));
														if(!user.contains(users.getUser().getId())) {
															userList.add(users);
															user.add(userEntity.getId());
														}
													}
												}
												prjSupervisors.setUser(userList.stream().distinct().collect(Collectors.toList()));
												prjSupervisorList.add(prjSupervisors);
											}
										}
										priSupervisors.setPrjSupervisor(prjSupervisorList.stream().distinct().collect(Collectors.toList()));
										priSupervisorList.add(priSupervisors);
									}
								}
								hrManagers.setPriSuperviser(priSupervisorList.stream().distinct().collect(Collectors.toList()));
								hrManagerList.add(hrManagers);
							}
						}
						orgHeads.setHrManager(hrManagerList.stream().distinct().collect(Collectors.toList()));
						orgHeadList.add(orgHeads);
					}
				}
				groupHeadList.setOrgHeadList(orgHeadList.stream().distinct().collect(Collectors.toList()));
			}
		}
		
		long end = Calendar.getInstance().getTimeInMillis();
		System.out.println("out "+(end-start));
		
		/*System.out.println("orgNum "+orgNum);
		System.out.println("orgHierarchyList "+orgHierarchyList.stream().distinct().collect(Collectors.toList()));
		System.out.println("orgHeadList "+orgHeadList.size());
		System.out.println("hrNum "+hrNum);
		System.out.println("hrManagerHierarchyList "+hrManagerHierarchyList.stream().distinct().collect(Collectors.toList()));
		System.out.println("priSuNum "+priSuNum);
		System.out.println("priSupervisorHierarchyList "+priSupervisorHierarchyList.stream().distinct().collect(Collectors.toList()));
		System.out.println("prjNum "+prjNum);
		System.out.println("prjSupervisorHierarchyList "+prjSupervisorHierarchyList.stream().distinct().collect(Collectors.toList()));
		System.out.println("uNum "+uNum);
		System.out.println("userHierarchyList "+userHierarchyList.stream().distinct().collect(Collectors.toList()));
		System.out.println(user);*/
		
		return groupHeadList;
	}
	
	public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor){
	    Map<Object, Boolean> map = new ConcurrentHashMap<>();
	    return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}
	
	
	//preparing user DTO
	private UserHierDTO prepareUserDTO(User userEntity) {
		UserHierDTO userDTO = new UserHierDTO();
		
		userDTO.setAlternateEmail(userEntity.getAlternateEmail());
		userDTO.setDateOfBirth(userEntity.getDateOfBirth());
		DepartmentDto deptDO = new DepartmentDto();
		try {
		    deptDO.setDeptName(userEntity.getDepartment().getDeptName());
		    deptDO.setId(userEntity.getDepartment().getId());

		    OrganizationDto orgDTO = new OrganizationDto();

		    orgDTO.setId(userEntity.getDepartment().getOrganization().getId());
		    orgDTO.setHierarchyId(userEntity.getDepartment().getOrganization().getHierarchyId());
		    orgDTO.setOrgName(userEntity.getDepartment().getOrganization().getOrgName());
		    
		    deptDO.setOrganization(orgDTO);
		    userDTO.setDepartment(deptDO);
		    userDTO.setOrganization(orgDTO);
		} catch (Exception e) {
		    LOGGER.error("error in getting organization/department of user {} {}", userEntity.getUserName(),
			    e.getMessage());
		}
		try {
		    DesignationDto desigDTO = new DesignationDto();

		    desigDTO.setDesig(userEntity.getDesignation().getDesig());
		    desigDTO.setId(userEntity.getDesignation().getId());
		    desigDTO.setModBy(userEntity.getDesignation().getModBy());
		    desigDTO.setModOn(userEntity.getDesignation().getModOn());

		    userDTO.setDesignation(desigDTO);
		} catch (Exception e) {
		    LOGGER.error("error in getting designation of user {} {}", userEntity.getUserName(), e.getMessage());
		}
		
		userDTO.setEmail(userEntity.getEmail());
		userDTO.setFirstName(userEntity.getFirstName());
		userDTO.setGender(userEntity.getGender());
		userDTO.setId(userEntity.getId());
		userDTO.setLastName(userEntity.getLastName());
		userDTO.setMobile(userEntity.getMobile());
		userDTO.setTypeOfEmployment(userEntity.getTypeOfEmployment());
		
		//getting user image
		int usrImgLength;
		try {
			if(userEntity.getUserImg() != null) {
			usrImgLength = (int) userEntity.getUserImg().length();
//			System.out.println(usrImgLength);
			if(0 != usrImgLength) {
//				userDTO.setUserImg(userEntity.getUserImg().getBytes(1, usrImgLength));
			}else {
				userDTO.setUserImg(null);
			}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		userDTO.setUserName(userEntity.getUserName());
		userDTO.setPermAddr(userEntity.getPermAddr());
		
		return userDTO;
	    }


}
