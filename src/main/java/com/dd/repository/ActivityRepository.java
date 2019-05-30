package com.dd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dd.entity.Activity;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long>{

}