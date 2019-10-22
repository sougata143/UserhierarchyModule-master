package com.sls.userhierarchy.DTO;

import java.sql.Date;
import java.util.Arrays;

public class UserHierDTO {

	private Long id;
    private String userName;
    private String firstName;
    private String lastName;
    private String gender;
    private Date dateOfBirth;
    private String email;
    private String alternateEmail;
    private Long mobile;
    private OrganizationDto organization;
    private DepartmentDto department;
    private DesignationDto designation;
    private String typeOfEmployment;
    private byte[] userImg;
    private String permAddr;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAlternateEmail() {
		return alternateEmail;
	}
	public void setAlternateEmail(String alternateEmail) {
		this.alternateEmail = alternateEmail;
	}
	public Long getMobile() {
		return mobile;
	}
	public void setMobile(Long mobile) {
		this.mobile = mobile;
	}
	public OrganizationDto getOrganization() {
		return organization;
	}
	public void setOrganization(OrganizationDto organization) {
		this.organization = organization;
	}
	public DepartmentDto getDepartment() {
		return department;
	}
	public void setDepartment(DepartmentDto department) {
		this.department = department;
	}
	public DesignationDto getDesignation() {
		return designation;
	}
	public void setDesignation(DesignationDto designation) {
		this.designation = designation;
	}
	public String getTypeOfEmployment() {
		return typeOfEmployment;
	}
	public void setTypeOfEmployment(String typeOfEmployment) {
		this.typeOfEmployment = typeOfEmployment;
	}
	public byte[] getUserImg() {
		return userImg;
	}
	public void setUserImg(byte[] userImg) {
		this.userImg = userImg;
	}
	public String getPermAddr() {
		return permAddr;
	}
	public void setPermAddr(String permAddr) {
		this.permAddr = permAddr;
	}

    
	
}
