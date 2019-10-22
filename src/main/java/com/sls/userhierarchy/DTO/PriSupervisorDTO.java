package com.sls.userhierarchy.DTO;

import java.util.List;

public class PriSupervisorDTO {
	
	private UserHierDTO priSupervisor;
	private List<PrjSupervisorDTO> prjSupervisor;
	public UserHierDTO getPriSupervisor() {
		return priSupervisor;
	}
	public void setPriSupervisor(UserHierDTO priSupervisor) {
		this.priSupervisor = priSupervisor;
	}
	public List<PrjSupervisorDTO> getPrjSupervisor() {
		return prjSupervisor;
	}
	public void setPrjSupervisor(List<PrjSupervisorDTO> prjSupervisor) {
		this.prjSupervisor = prjSupervisor;
	}
	@Override
	public String toString() {
		return "PriSupervisorDTO [priSupervisor=" + priSupervisor + ", prjSupervisor=" + prjSupervisor + "]";
	}
	
	
	

}
