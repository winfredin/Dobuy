package com.member.model;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import org.springframework.stereotype.Repository;

import com.goods.model.GoodsVO;

@Repository
public interface MemberRepository extends JpaRepository<MemberVO, Integer> {

	boolean existsByMemAccount(String memAccount);

	Optional<MemberVO> findByMemAccountAndMemPassword(String memAccount, String memPassword);

	@Query(value = "SELECT memNo FROM member WHERE memAccount = ?1 ", nativeQuery = true)
	String getMemNoByAccount(String memAccount);

	@Query(value = "SELECT * FROM member WHERE memNo = ?1 ", nativeQuery = true)
	MemberVO findOne(Integer memNo);
//	winfred===================================================================================以下
	Optional<MemberVO> findByMemAccount(String memAccount);
//	winfred===================================================================================以上

	
	@Modifying
	@Transactional
	@Query("UPDATE MemberVO m SET m.memStatus = :memStatus WHERE m.memNo = :memNo")
	void updateStatus(@Param("memStatus") int memStatus, @Param("memNo") int memNo);

	@Modifying
	@Transactional
	@Query("UPDATE MemberVO m SET m.memPassword = :memPassword WHERE m.memNo = :memNo")
	void updatePass(String memPassword,Integer memNo);
	
	@Modifying
	@Transactional
	@Query("UPDATE MemberVO m SET m.memAddress = :memAddress WHERE m.memNo = :memNo")
	void updateAdd(String memAddress,Integer memNo);
}
