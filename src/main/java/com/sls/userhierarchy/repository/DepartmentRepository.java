package com.sls.userhierarchy.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sls.userhierarchy.entity.Department;
import com.sls.userhierarchy.entity.Organization;

public interface DepartmentRepository extends JpaRepository<Department, Long>{

    Optional<Department> findById(Long i);

    List<Department> findByOrganization(Optional<Organization> organization);
    
    List<Department> findByDeptName(String deptName);

}
