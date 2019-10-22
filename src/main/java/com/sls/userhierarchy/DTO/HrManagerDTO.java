package com.sls.userhierarchy.DTO;

import java.util.List;

public class HrManagerDTO {
	
	private UserHierDTO hrManager;
	private List<PriSupervisorDTO> priSuperviser;
	public UserHierDTO getHrManager() {
		return hrManager;
	}
	public void setHrManager(UserHierDTO hrManager) {
		this.hrManager = hrManager;
	}
	public List<PriSupervisorDTO> getPriSuperviser() {
		return priSuperviser;
	}
	public void setPriSuperviser(List<PriSupervisorDTO> priSuperviser) {
		this.priSuperviser = priSuperviser;
	}
	@Override
	public String toString() {
		return "HrManagerDTO [hrManager=" + hrManager + ", priSuperviser=" + priSuperviser + ", getHrManager()="
				+ getHrManager() + ", getPriSuperviser()=" + getPriSuperviser() + ", getClass()=" + getClass()
				+ ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}
	
	
	
	

}
