package com.suza.storetrack_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.suza.storetrack_backend.model.DailySale;


@Repository
public interface DailySalesRepository extends JpaRepository<DailySale,Long> {

    
} 