package com.sls.userhierarchy.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sls.userhierarchy.entity.Organization;

public interface OrganizationRepository extends JpaRepository<Organization, Long>{

    Optional<Organization> findById(Long i);

    List<Organization> findByOrgName(String organizationName);

}
