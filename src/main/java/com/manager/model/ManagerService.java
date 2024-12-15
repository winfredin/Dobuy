package com.manager.model;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;




@Service("managerService")
public class ManagerService {

	@Autowired
	ManagerRepository repository;


	

	public void addManager(ManagerVO managerVO) {
		repository.save(managerVO);
	}

	public void updateManager(ManagerVO managerVO) {
		repository.save(managerVO);
	}

	public void deleteManager(Integer managerNo) {
		if (repository.existsById(managerNo))
			repository.deleteByManagerNo(managerNo);
	}

	public ManagerVO getOneManager(Integer managerNo) {
		Optional<ManagerVO> optional = repository.findById(managerNo);
		return optional.orElse(null);
	}
	public ManagerVO getAP(String managerAccount,String managerPassword) {
		return repository.findAP(managerAccount,managerPassword);
	}
	public List<ManagerVO> getAll() {
		List<ManagerVO> managerList= repository.findAll();
		return managerList;
	}
	
	
	public boolean isAccountExist(String managerAccount) {
	    return repository.findAC(managerAccount) != null;
	}
}




