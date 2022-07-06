package com.graduate.licenseplate.illegal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.graduate.licenseplate.illegal.entity.IllegalLicense;

@Repository
public interface IllegalLicenseRepository extends JpaRepository<IllegalLicense, Long>{

	Optional<IllegalLicense> findByLicenseplate(String plate);


}
