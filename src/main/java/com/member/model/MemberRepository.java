package com.member.model;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.goods.model.GoodsVO;

@Repository
public interface MemberRepository extends JpaRepository<MemberVO, Integer> {

	boolean existsByMemAccount(String memAccount);

	Optional<MemberVO> findByMemAccountAndMemPassword(String memAccount, String memPassword);
<<<<<<< HEAD

	@Query(value = "SELECT memNo FROM member WHERE memAccount = ?1 ", nativeQuery = true)
	String getMemNoByAccount(String memAccount);
=======
//	winfred===================================================================================以下
	Optional<MemberVO> findByMemAccount(String memAccount);
//	winfred===================================================================================以上

>>>>>>> master
}
