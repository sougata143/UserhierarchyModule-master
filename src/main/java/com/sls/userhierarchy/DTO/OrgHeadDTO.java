package com.sls.userhierarchy.DTO;

import java.util.List;

public class OrgHeadDTO {

	private UserHierDTO orgHead;
	private List<HrManagerDTO> hrManager;
	
	
	public UserHierDTO getOrgHead() {
		return orgHead;
	}
	public void setOrgHead(UserHierDTO orgHead) {
		this.orgHead = orgHead;
	}
	public List<HrManagerDTO> getHrManager() {
		return hrManager;
	}
	public void setHrManager(List<HrManagerDTO> hrManager) {
		this.hrManager = hrManager;
	}
	@Override
	public String toString() {
		return "OrgHeadDTO [orgHead=" + orgHead + ", hrManager=" + hrManager + ", getOrgHead()=" + getOrgHead()
				+ ", getHrManager()=" + getHrManager() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}
	
	
	
	
	
}
