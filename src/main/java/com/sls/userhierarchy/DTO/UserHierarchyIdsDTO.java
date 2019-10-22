package com.sls.userhierarchy.DTO;

public class UserHierarchyIdsDTO {

	private Long orgHead;
	private Long hrManager;
	private Long priSupervisor;
	private Long prjSupervisor;
	private Long user;
	
	
	public Long getOrgHead() {
		return orgHead;
	}
	public void setOrgHead(Long orgHead) {
		this.orgHead = orgHead;
	}
	public Long getHrManager() {
		return hrManager;
	}
	public void setHrManager(Long hrManager) {
		this.hrManager = hrManager;
	}
	public Long getPriSupervisor() {
		return priSupervisor;
	}
	public void setPriSupervisor(Long priSupervisor) {
		this.priSupervisor = priSupervisor;
	}
	public Long getPrjSupervisor() {
		return prjSupervisor;
	}
	public void setPrjSupervisor(Long prjSupervisor) {
		this.prjSupervisor = prjSupervisor;
	}
	public Long getUser() {
		return user;
	}
	public void setUser(Long user) {
		this.user = user;
	}
	
	
	
}
