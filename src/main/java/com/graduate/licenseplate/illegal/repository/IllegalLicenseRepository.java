package com.graduate.licenseplate.illegal.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.graduate.licenseplate.illegal.entity.IllegalLicense;

@Repository
public interface IllegalLicenseRepository extends JpaRepository<IllegalLicense, Long>{

	Optional<IllegalLicense> findByLicenseplate(String plate);

	@Query(value = "SELECT * FROM illegal_license license "
			+ "ORDER BY license.date DESC, license.time DESC "
			+ "LIMIT :startIndex, :showCount", nativeQuery=true)
	List<IllegalLicense> findLimitShowCountByDate(@Param("startIndex") Integer startIndex,@Param("showCount") Integer showCount);

	@Query(value = "SELECT * FROM illegal_license license "
			+ "WHERE license.date >= :startTime AND license.date <= :endTime "
			+ "ORDER BY license.date DESC, license.time DESC "
			+ "LIMIT :startIndex, :showCount", nativeQuery=true)
	List<IllegalLicense> findIllegalByStartAndEnd(
			@Param("startTime") Integer startTime,
			@Param("endTime") Integer endTime, 
			@Param("startIndex") Integer startIndex,
			@Param("showCount") Integer showCount);

	@Query("SELECT COUNT(*) FROM IllegalLicense")
	Integer findCountById();
	
	@Query(value = "SELECT COUNT(*) FROM illegal_license license "
			+ "WHERE license.date >= :startTime AND license.date <= :endTime ", nativeQuery=true)
	Integer findCountByCondition(
			@Param("startTime") Integer startTime,
			@Param("endTime") Integer endTime);

	List<IllegalLicense> findByIllegalMember_RegistrationNumber(@Param("registrationNumber") String registNum);
	
	
}
