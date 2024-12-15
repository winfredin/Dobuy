package com.counterHome.counterOrderTest.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewCounterOrderRepository extends  JpaRepository<NewCounterOrderVO, Integer> {

}
