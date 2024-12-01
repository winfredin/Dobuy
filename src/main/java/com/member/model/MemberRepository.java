package com.member.model;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<MemberVO, Integer>{
	
	boolean existsByMemAccount(String memAccount);
	
	Optional<MemberVO> findByMemAccountAndMemPassword(String memAccount, String memPassword);
//	winfred===================================================================================以下
	Optional<MemberVO> findByMemAccount(String memAccount);
//	winfred===================================================================================以上

}
