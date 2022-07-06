package com.graduate.licenseplate.illegal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.graduate.licenseplate.illegal.entity.OriginLicense;

@Repository
public interface OriginLicenseRepository extends JpaRepository<OriginLicense, String>{

}
