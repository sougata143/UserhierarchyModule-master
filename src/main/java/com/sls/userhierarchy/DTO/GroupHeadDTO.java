package com.sls.userhierarchy.DTO;

import java.util.List;

import com.sls.userhierarchy.entity.UserHierarchy;

public class GroupHeadDTO {
	
	private UserHierDTO groupHead;
	private List<OrgHeadDTO> orgHeadList;
	
	
	
	public UserHierDTO getGroupHead() {
		return groupHead;
	}
	public void setGroupHead(UserHierDTO userHierDTO) {
		this.groupHead = userHierDTO;
	}
	public List<OrgHeadDTO> getOrgHeadList() {
		return orgHeadList;
	}
	public void setOrgHeadList(List<OrgHeadDTO> orgHeads) {
		this.orgHeadList = orgHeads;
	}
	public GroupHeadDTO(List<UserDTO> hierarcyList, List<OrgHeadDTO> orgHeadList) {
		super();
		this.orgHeadList = orgHeadList;
	}
	public GroupHeadDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "GroupHeadDTO [orgHeadList=" + orgHeadList + "]";
	}
	
	

}
