package com.spring.restaurant.service;

import com.spring.restaurant.domain.Lunch;
import com.spring.restaurant.domain.LunchType;
import com.spring.restaurant.entity.LunchTypeEntity;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface LunchService {
    Page<Lunch> findAllByPageAndLunchType(int pageNumber, LunchType lunchType);

    int pageCount(LunchType lunchType);

    Optional<Lunch> findById(Long lunchId);
}
