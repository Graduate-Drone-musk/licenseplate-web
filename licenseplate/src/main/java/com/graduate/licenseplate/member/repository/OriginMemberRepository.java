package com.graduate.licenseplate.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.graduate.licenseplate.member.entity.OriginMember;

@Repository
public interface OriginMemberRepository extends JpaRepository<OriginMember, String>{

}
