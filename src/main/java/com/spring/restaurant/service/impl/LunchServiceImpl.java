package com.spring.restaurant.service.impl;

import com.spring.restaurant.domain.Lunch;
import com.spring.restaurant.domain.LunchType;
import com.spring.restaurant.entity.LunchEntity;
import com.spring.restaurant.entity.LunchTypeEntity;
import com.spring.restaurant.repository.LunchRepository;
import com.spring.restaurant.service.LunchService;
import com.spring.restaurant.service.mapper.Mapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.spring.restaurant.service.util.ServiceUtil.getNumberOfPages;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class LunchServiceImpl implements LunchService {
    private static final int ITEMS_PER_PAGE = 9;
    private final LunchRepository lunchRepository;
    private final Mapper<Lunch, LunchEntity> lunchMapper;

    @Override
    public Page<Lunch> findAllByPageAndLunchType(int pageNumber, LunchType lunchType) {
        Pageable lunchPage = PageRequest.of(pageNumber, ITEMS_PER_PAGE);
        return lunchRepository.findAllByLunchTypeEntity(LunchTypeEntity.valueOf(lunchType.name()), lunchPage)
                .map(lunchMapper::mapEntityToDomain);
    }

    @Override
    public int pageCount(LunchType lunchType) {
        return getNumberOfPages(lunchRepository.countAllByLunchTypeEntity(LunchTypeEntity.valueOf(lunchType.name())), ITEMS_PER_PAGE);
    }

    @Override
    public Optional<Lunch> findById(Long lunchId) {
        return lunchRepository.findById(lunchId).map(lunchMapper::mapEntityToDomain);
    }
}
