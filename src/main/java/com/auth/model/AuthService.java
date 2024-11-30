package com.auth.model;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;




@Service("authService")
public class AuthService {
	@Autowired
	AuthRepository repository;
	


	public void addAuth(AuthVO authVO) {

		repository.save(authVO);
	}

	public void updateAuth(AuthVO authVO) {
		repository.save(authVO);
	}

	public void deleteAuth(Integer authNo) {
		if (repository.existsById(authNo))
			repository.deleteByAuthno(authNo);
	}

	public AuthVO getOneAuth(Integer authNo) {
		Optional<AuthVO> optional = repository.findById(authNo);
		return optional.orElse(null);
	}

	public List<AuthVO> getAll() {
		return repository.findAll();
	}
}
