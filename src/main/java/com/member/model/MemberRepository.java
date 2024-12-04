package com.member.model;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<MemberVO, Integer>{
	
	boolean existsByMemAccount(String memAccount);
	
	Optional<MemberVO> findByMemAccountAndMemPassword(String memAccount, String memPassword);
//	winfred===================================================================================以下
	Optional<MemberVO> findByMemAccount(String memAccount);
//	winfred===================================================================================以上

	
	@Modifying
	@Transactional
	@Query("UPDATE MemberVO m SET m.memStatus = :memStatus WHERE m.memNo = :memNo")
	void updateStatus(@Param("memStatus") int memStatus, @Param("memNo") int memNo);

}
