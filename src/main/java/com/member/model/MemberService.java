package com.member.model;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("memService")
public class MemberService {

	@Autowired
	MemberRepository memberRepository;

	public void addMem(MemberVO memberVO) {
		if (memberVO.getMemStatus() == null) {
			memberVO.setMemStatus(1);
		}
		memberRepository.save(memberVO);
	}
	
	public boolean isAccountExists(String memAccount) {
		return memberRepository.existsByMemAccount(memAccount);
	}
	
	public boolean validateLogin(String memAccount, String memPassword) {
		return memberRepository.findByMemAccountAndMemPassword (memAccount, memPassword).isPresent();
	}
	
	public String getMemNoByAccount(String memAccount) {
		return memberRepository.getMemNoByAccount(memAccount);
	}
	
	
    // winfred
    public Optional<MemberVO> findById(Integer memNo) { //1130
        return memberRepository.findById(memNo);
    }
    // winfred
	public MemberVO findByMemAccount(String memAccount) { //1130
		// TODO Auto-generated method stub
		return null;
	}

}
