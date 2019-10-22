package com.sls.userhierarchy.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sls.userhierarchy.entity.Designation;

public interface DesignationRepository extends JpaRepository<Designation, Long>{

    Optional<Designation> findById(Long i);

    List<Designation> findByDesig(String desigName);

}
