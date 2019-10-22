package com.sls.userhierarchy.services.serviceimpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
public class UserHierarcyServiceImpl implements UserHierarcyService {
	
	@Autowired
	UserHierarcyDao hierarcyDao;
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	OrganizationDao orgDao;
	
	@Autowired
	LocationMasterDao locationDao;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserHierarcyServiceImpl.class);

	
	/*
	 * (non-Javadoc)
	 * @see com.sls.userhierarchy.services.userhierarcy.UserHierarcyService#getHierarcyById(java.lang.Long)
	 * fetching hierarchy by hierarchy ID
	 * GET method for user_hierarcy table with param hierarcyId
	 */
	@Override
	public ResponseEntity<UserHierarcyDTO> getHierarcyById(Long hierarcyId) {
		UserHierarchy hierarcy = hierarcyDao.getHierarchyById(hierarcyId);
		return new ResponseEntity<UserHierarcyDTO>(prepareUserHierarchyDTO(hierarcy), HttpStatus.OK);
	}

	
	/*
	 * preparing hierarchy DTO for fetching data
	 */
	private UserHierarcyDTO prepareUserHierarchyDTO(UserHierarchy hierarcy) {
		UserHierarcyDTO hierarcyDTO = new UserHierarcyDTO();
		
		hierarcyDTO.setFromDate(hierarcy.getFromDate());
		hierarcyDTO.setModBy(hierarcy.getModBy());
		hierarcyDTO.setModOn(hierarcy.getModOn());
		hierarcyDTO.setPracticeMaster(hierarcy.getPracticeMaster());
		hierarcyDTO.setPrjAllocDate(hierarcy.getPrjAllocDate());
		hierarcyDTO.setPrjReleaseDate(hierarcy.getPrjReleaseDate());
		hierarcyDTO.setProjectMaster(hierarcy.getProjectMaster());
		hierarcyDTO.setStatus(hierarcy.getStatus());
		hierarcyDTO.setToDate(hierarcy.getToDate());
		
		User user = userDao.findUserById(hierarcy.getUser().getId());
		
		
		hierarcyDTO.setUser(prepareUserDTO(user));
		
		//setting priSupervisor
		User priSupervisor = userDao.findUserById(hierarcy.getPriSupervisor().getId());
		hierarcyDTO.setPriSupervisor(prepareUserDTO(priSupervisor));
		
		User hrManager = userDao.findUserById(hierarcy.getHrManager().getId());
		hierarcyDTO.setHrManager(prepareUserDTO(hrManager));
		
		hierarcyDTO.setHierarcyId(hierarcy.getHierarcyId());
		
		Organization org = orgDao.findOrganizationById(hierarcy.getUser().getOrganization().getId()).get();
		hierarcyDTO.setOrgId(org);
		
		Organization parentOrg = orgDao.findOrganizationById(hierarcy.getUser().getOrganization().getId()).get();
		hierarcyDTO.setParentOrgId(parentOrg);
		
		if(hierarcy.getGroupHead() != null) {
			User groupHeadEntity = userDao.findUserById(hierarcy.getGroupHead().getId());
			long groupHeadDesigId = groupHeadEntity.getDesignation().getId();
			if(groupHeadDesigId == 6) {		//checking if the user is chairman or not
//				hierarcyDTO.setGroupHead(groupHeadEntity.getFirstName()+" "+groupHeadEntity.getLastName());
				hierarcyDTO.setGroupHead(prepareUserDTO(groupHeadEntity));
			}
		}
		
		
		
		if(hierarcy.getOrgHead() != null) {
			User orgHeadEntity = userDao.findUserById(hierarcy.getOrgHead().getId());
			long orgHeadDesigID = orgHeadEntity.getDesignation().getId();
			if(orgHeadDesigID == 1) {	//checking if the user is director or not
				hierarcyDTO.setOrgHead(prepareUserDTO(orgHeadEntity));
			}
		}
		
		return hierarcyDTO;
	}

	
	/*
	 * (non-Javadoc)
	 * @see com.sls.userhierarchy.services.userhierarcy.UserHierarcyService#populateHierarcyList()
	 * fetching all hierarchies
	 * GET method for user_hierarcy table
	 */
	@Override
	public List<UserHierarcyDTO> populateHierarcyList() {
		List<UserHierarcyDTO> hierarcyEmpList = new ArrayList<>();
		try {
			List<UserHierarchy> hierarcyList = hierarcyDao.getAllHierarcy();
			hierarcyList.forEach(hierarcy->{
				hierarcyEmpList.add(prepareUserHierarchyDTO(hierarcy));
			});
		}catch(Exception e) {
			e.printStackTrace();
		}
		return hierarcyEmpList;
	}

	
	/*
	 * (non-Javadoc)
	 * @see com.sls.userhierarchy.services.userhierarcy.UserHierarcyService#groupHead()
	 * fetching hierarchy in nested manner
	 */
	@Override
	@Transactional
	public GroupHeadDTO groupHead(long id) {
		long start = Calendar.getInstance().getTimeInMillis();
		int count = 0;
		List<UserHierarchy> groupHeadHierarcyList = hierarcyDao.getHierarchyByUser(id);	//fetching all hierarchies
		
		count++;
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
		
		if(!groupHeadHierarcyList.isEmpty()) {
			for(int x = 0 ; x < groupHeadHierarcyList.size() ; x++) {
				
//				OrgHeadDTO orgHeads = new OrgHeadDTO();
				
				if(groupHeadHierarcyList.get(x).getOrgHead()!=null) {
					orgHierarchyList = hierarcyDao.getHierarchyOrgHeadByGroupHead(groupHeadHierarcyList.get(x).getGroupHead().getId())
							.stream()
							.distinct()
							.collect(Collectors.toList());
					count++;
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
							count++;
							
							hrNum = hrManagerHierarchyList.size();
							
						}

						orgHeads.setOrgHead(prepareUserDTO(userDao.findUserById(orgHierarchyList.get(m))));
						count++;
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
									count++;
									priSuNum = priSupervisorHierarchyList.size();
								}
								
								hrManagers.setHrManager(prepareUserDTO(userDao.findUserById(hrManagerHierarchyList.get(l))));
								count++;
								
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
											count++;
											prjNum = prjSupervisorHierarchyList.size();
										}
										
										priSupervisors.setPriSupervisor(prepareUserDTO(userDao.findUserById(priSupervisorHierarchyList.get(k))));
										count++;
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
													count++;
													uNum = userHierarchyList.size();
												}
												
												prjSupervisors.setPrjSupervisor(prepareUserDTO(userDao.findUserById(prjSupervisorHierarchyList.get(j))));
												count++;
												//setting users list
												if(!userHierarchyList.isEmpty()) {
//													List<UserHDTO> userList = new ArrayList<>();
													for(int i = 0 ; i < userHierarchyList.size() ; i++) {
														UserHDTO users = new UserHDTO();
														users.setUser(prepareUserDTO(userDao.findUserById(userHierarchyList.stream().distinct().collect(Collectors.toList()).get(i))));
														count++;
														userList.add(users);
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
		System.out.println("count "+count);
		
//		System.out.println("orgNum "+orgNum);
//		System.out.println("orgHierarchyList "+orgHierarchyList.stream().distinct().collect(Collectors.toList()));
//		System.out.println("orgHeadList "+orgHeadList.size());
//		System.out.println("hrNum "+hrNum);
//		System.out.println("hrManagerHierarchyList "+hrManagerHierarchyList.stream().distinct().collect(Collectors.toList()));
//		System.out.println("priSuNum "+priSuNum);
//		System.out.println("priSupervisorHierarchyList "+priSupervisorHierarchyList.stream().distinct().collect(Collectors.toList()));
//		System.out.println("prjNum "+prjNum);
//		System.out.println("prjSupervisorHierarchyList "+prjSupervisorHierarchyList.stream().distinct().collect(Collectors.toList()));
//		System.out.println("uNum "+uNum);
//		System.out.println("userHierarchyList "+userHierarchyList.stream().distinct().collect(Collectors.toList()));
		
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
			System.out.println(usrImgLength);
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


	@Override
	public GroupHeadDTO hierarchy(long id) {
		GroupHeadDTO grouphead = new GroupHeadDTO();
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
		
		List<UserHierarchy> orgHeads = new ArrayList<>();
		
		if(!groupHeadHierarcyList.isEmpty()) {
			for(int x = 0 ; x < groupHeadHierarcyList.size() ; x++) {
				groupHeadList.setGroupHead(prepareUserDTO(groupHeadHierarcyList.get(x).getGroupHead()));
				
				if(groupHeadHierarcyList.get(x).getOrgHead()!=null) {
					orgHierarchyList = hierarcyDao.getHierarchyOrgHeadByGroupHead(groupHeadHierarcyList.get(x).getGroupHead().getId())
							.stream()
							.distinct()
							.collect(Collectors.toList());
					for(int a = 0 ; a < orgHierarchyList.size() ; a++) {
						
						orgHeads = hierarcyDao.getHierarchyByOrgHeads(orgHierarchyList.get(a));
						for(int aa = 0 ; aa < orgHeads.size() ; aa++) {
							OrgHeadDTO orghead = new OrgHeadDTO();
							orghead.setOrgHead(prepareUserDTO(orgHeads.get(aa).getOrgHead()));
							orgHeadList.add(orghead);
						}
					}
					orgNum = orgHierarchyList.size();
					
				}
				
				/*if(groupHeadHierarcyList.get(x).getHrManager()!=null && 
						groupHeadHierarcyList.get(x).getOrgHead()!=null) {
					hrManagerHierarchyList = 
							hierarcyDao.getHierarchyHrManagerByGroupHead(groupHeadHierarcyList.get(x).getGroupHead().getId())
							.stream()
							.distinct()
							.collect(Collectors.toList());
					
					hrNum = hrManagerHierarchyList.size();
					
				}*/
				
				/*if(groupHeadHierarcyList.get(x).getPriSupervisor()!=null && 
						groupHeadHierarcyList.get(x).getHrManager()!=null && 
						groupHeadHierarcyList.get(x).getOrgHead()!=null) {
					priSupervisorHierarchyList = 
							hierarcyDao.getHierarchypriSupervisorByOrgHead(groupHeadHierarcyList.get(x).getGroupHead().getId())
							.stream()
							.distinct()
							.collect(Collectors.toList());
					priSuNum = priSupervisorHierarchyList.size();
				}
				
				if(groupHeadHierarcyList.get(x).getPrjSupervisor()!=null && 
						groupHeadHierarcyList.get(x).getPriSupervisor()!=null && 
						groupHeadHierarcyList.get(x).getHrManager()!=null && 
						groupHeadHierarcyList.get(x).getOrgHead()!=null) {
					prjSupervisorHierarchyList =
							hierarcyDao.getHierarchyPrjSupervisorByGroupHead(groupHeadHierarcyList.get(x).getGroupHead().getId())
							.stream()
							.distinct()
							.collect(Collectors.toList());
					prjNum = prjSupervisorHierarchyList.size();
				}
				
				if(groupHeadHierarcyList.get(x).getUser()!=null &&
						groupHeadHierarcyList.get(x).getPrjSupervisor()!=null && 
						groupHeadHierarcyList.get(x).getPriSupervisor()!=null && 
						groupHeadHierarcyList.get(x).getHrManager()!=null &&
						groupHeadHierarcyList.get(x).getOrgHead()!=null) {
					userHierarchyList =
							hierarcyDao.getHierarchyUserByGroupHead(groupHeadHierarcyList.get(x).getGroupHead().getId())
							.stream()
							.distinct()
							.collect(Collectors.toList());
					uNum = userHierarchyList.size();
				}*/
				
				grouphead.setGroupHead(prepareUserDTO(groupHeadHierarcyList.get(x).getGroupHead()));
				grouphead.setOrgHeadList(orgHeadList);
			}
		}
		
		return grouphead;
	}


	@Override
	public GroupHeadDTO hierarchyy() {
		GroupHeadDTO grphd = new GroupHeadDTO();
		List<UserHierarchy> hierarchies = hierarcyDao.getAllHierarcy();
		List<User> grphds = hierarchies.stream().map(UserHierarchy::getGroupHead).distinct().collect(Collectors.toList());
		List<User> orghds = hierarchies.stream().map(UserHierarchy::getOrgHead).distinct().collect(Collectors.toList());
		List<User> hrmanagers = hierarchies.stream().map(UserHierarchy::getHrManager).distinct().collect(Collectors.toList());
		List<User> prisuper = hierarchies.stream().map(UserHierarchy::getPriSupervisor).distinct().collect(Collectors.toList());
		List<User> prjsuper = hierarchies.stream().map(UserHierarchy::getPrjSupervisor).distinct().collect(Collectors.toList());
		List<User> user = hierarchies.stream().map(UserHierarchy::getUser).distinct().collect(Collectors.toList());
		for(int i = 0 ; i < grphds.size() ; i++) {
			GroupHeadDTO grphd2 = new GroupHeadDTO();
			grphd2.setGroupHead(prepareUserDTO(grphds.get(i)));
			grphd2.setOrgHeadList(prepareOrgHeadDTO(orghds,hrmanagers,prisuper,prjsuper,user));
			grphd = grphd2;
		}
		System.out.println(grphds.size());
		System.out.println(orghds.size());
		System.out.println(hrmanagers.size());
		System.out.println(prisuper.size());
		System.out.println(prjsuper.size());
		System.out.println(user.size());
		return grphd;
	}


	private List<OrgHeadDTO> prepareOrgHeadDTO(List<User> orghds, List<User> hrmanagers, 
												List<User> prisuper, List<User> prjsuper, List<User> user) {
		List<OrgHeadDTO> orgheads = new ArrayList<>();
		for(int i = 0 ; i < orghds.size() ; i++) {
			OrgHeadDTO orghead = new OrgHeadDTO();
			orghead.setOrgHead(prepareUserDTO(orghds.get(i)));
			List<UserHierarchy> orgheadhier = hierarcyDao.getHierarchyByOrgHeads(orghds.get(i).getId());
			/*List<Long> org = orgheadhier.stream().map(UserHierarchy::getHrManager).collect(Collectors.toList())
								.stream().map(User::getId).collect(Collectors.toList());
			List<Long> hrs = hrmanagers.stream().map(User::getId).collect(Collectors.toList());*/
			List<User> hrmngrs = orgheadhier
											.stream()
											.map(UserHierarchy::getHrManager)
											.distinct()
											.collect(Collectors.toList());
			orghead.setHrManager(prepareHrManagerDTO(hrmngrs,prisuper,prjsuper,user,orghds.get(i)));
			orgheads.add(orghead);
		}
		return orgheads;
	}


	private List<HrManagerDTO> prepareHrManagerDTO(List<User> hrmanagers, 
			List<User> prisuper, List<User> prjsuper, List<User> user, User orghead) {
		List<HrManagerDTO> hrmanagerDTOs = new ArrayList<>();
		for(int i = 0 ; i < hrmanagers.size() ; i++) {
			HrManagerDTO hr = new HrManagerDTO();
			hr.setHrManager(prepareUserDTO(hrmanagers.get(i)));
//			List<UserHierarchy> hrhier = hierarcyDao.getHierarchyByHrId(hrmanagers.get(i).getId());
			List<UserHierarchy> hrhier =
					hierarcyDao.getHierarchyByOrgHeadsAndHrManager(orghead.getId(),hrmanagers.get(i).getId());
			List<User> prisupers = hrhier
										.stream()
										.map(UserHierarchy::getPriSupervisor)
										.distinct()
										.collect(Collectors.toList());
			hr.setPriSuperviser(preparePriSuperDTO(prisupers,prjsuper,user,hrmanagers.get(i),orghead));
			hrmanagerDTOs.add(hr);
		}
		return hrmanagerDTOs;
	}


	private List<PriSupervisorDTO> preparePriSuperDTO(List<User> prisuper,
			List<User> prjsuper, List<User> user, User hr, User orghead) {
		List<PriSupervisorDTO> prsupers = new ArrayList<>();
		for(int i = 0 ; i < prisuper.size() ; i++) {
			PriSupervisorDTO psuper = new PriSupervisorDTO();
			psuper.setPriSupervisor(prepareUserDTO(prisuper.get(i)));
//			List<UserHierarchy> prihier = hierarcyDao.getHierarchyByPrSuperId(prisuper.get(i).getId());
			List<UserHierarchy> prihier = 
					hierarcyDao.getHierarchyByOrgHeadsAndHrManagerAndPriSuper(orghead.getId(),hr.getId(),prisuper.get(i).getId());
			List<User> prjsupers = prihier
											.stream()
											.map(UserHierarchy::getPrjSupervisor)
											.distinct()
											.collect(Collectors.toList());
			psuper.setPrjSupervisor(preparePrjSuperDTO(prjsupers,user,orghead,hr,prisuper.get(i)));
			prsupers.add(psuper);
		}
		return prsupers;
	}


	private List<PrjSupervisorDTO> preparePrjSuperDTO(List<User> prjsuper,
			List<User> user, User orghead, User hr, User prisuper) {
		List<PrjSupervisorDTO> prjs = new ArrayList<>();
		for(int i = 0 ; i < prjsuper.size() ; i++) {
			PrjSupervisorDTO pr = new PrjSupervisorDTO();
			pr.setPrjSupervisor(prepareUserDTO(prjsuper.get(i)));
//			List<UserHierarchy> prjhier = hierarcyDao.getHierarchyByPrjSuperId(prjsuper.get(i).getId());
			List<UserHierarchy> prjhier = 
					hierarcyDao.getHierarchyByOrgHeadsAndHrManagerAndPriSuperAndPrjSuper(
							orghead.getId(),hr.getId(),prisuper.getId(),prjsuper.get(i).getId());
			List<User> users = prjhier
										.stream()
										.map(UserHierarchy::getUser)
										.distinct()
										.collect(Collectors.toList());
			pr.setUser(prepareUsersDTO(users));
			prjs.add(pr);
		}
		return prjs;
	}


	private List<UserHDTO> prepareUsersDTO(List<User> user) {
		List<UserHDTO> users = new ArrayList<>();
		for(int i = 0 ; i < user.size() ; i++) {
			UserHDTO userd = new UserHDTO();
			userd.setUser(prepareUserDTO(user.get(i)));
			users.add(userd);
		}
		return users;
	}
}
