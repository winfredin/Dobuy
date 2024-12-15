package com.counterHome.counterOrderDetailTest.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewCounterOrderDetailRepository extends JpaRepository<NewCounterOrderDetailVO, Integer>{


}
