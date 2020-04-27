package com.spring.restaurant.repository;

import com.spring.restaurant.entity.LunchEntity;
import com.spring.restaurant.entity.LunchTypeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LunchRepository extends JpaRepository<LunchEntity, Long> {
    Page<LunchEntity> findAllByLunchTypeEntity(LunchTypeEntity lunchTypeEntity, Pageable lunchPage);
    long countAllByLunchTypeEntity(LunchTypeEntity lunchTypeEntity);
}
