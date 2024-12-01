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
	
	
    // winfred
    public Optional<MemberVO> findById(Integer memNo) { //1130
        return memberRepository.findById(memNo);
    }
    // winfred
    public MemberVO findByMemAccount(String memAccount) {
        Optional<MemberVO> optionalMember = memberRepository.findByMemAccount(memAccount);
        return optionalMember.orElse(null);
    }

}
