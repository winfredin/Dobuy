package com.countercarousel.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CountercarouselRepository extends JpaRepository<CountercarouselVO, Integer>{
	@Query(value = "SELECT * FROM countercarousel ORDER BY carouselTime desc LIMIT 3", nativeQuery = true)
	List<CountercarouselVO> findNewest3();

}
