package com.member.model;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.manager.model.ManagerVO;

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

	public List<MemberVO> getAll() {
		List<MemberVO> memberList= memberRepository.findAll();
		return memberList;
	}

	
	public String getMemNoByAccount(String memAccount) {
		return memberRepository.getMemNoByAccount(memAccount);
	}
	
	public  void updateMem(MemberVO memberVO) {
		memberRepository.save(memberVO);
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
  public MemberVO findOne(Integer memNo) {
	  return memberRepository.findOne(memNo);
  }
  
    public void upStatus(Integer memNo,Integer memStatus) { 	
    	   memberRepository.updateStatus(memStatus,memNo);   
    }
    
    public void upAdd(Integer memNo,String memAddress) { 	
 	   memberRepository.updateAdd(memAddress,memNo);   
 }
    

        public boolean updateEmail(Integer memNo, String newEmail) {
            // 使用 findById 獲取資料
            Optional<MemberVO> optionalMember = memberRepository.findById(memNo);
            if (optionalMember.isPresent()) {
                MemberVO member = optionalMember.get();
                member.setMemEmail(newEmail); // 更新 Email
                memberRepository.save(member); // 儲存到資料庫
                return true;
            }
            return false; // 若找不到該用戶，返回失敗
        }

        public boolean updatePhone(Integer memNo, String newPhone) {
            // 使用 findById 獲取資料
            Optional<MemberVO> optionalMember = memberRepository.findById(memNo);
            if (optionalMember.isPresent()) {
                MemberVO member = optionalMember.get();
                member.setMemPhone(newPhone); // 更新 Phone
                memberRepository.save(member); // 儲存到資料庫
                return true;
            }
            return false; // 若找不到該用戶，返回失敗
        }
        
        public void updatePass(Integer memNo,String memPassword) {
        	memberRepository.updatePass(memPassword, memNo);
        }
        public Integer getMemStatusByAccount(String memAccount) {
        	return memberRepository.findByAcc(memAccount);	
        	
        }
    }


