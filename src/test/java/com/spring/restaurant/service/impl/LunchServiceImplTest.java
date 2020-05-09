package com.spring.restaurant.service.impl;

import com.spring.restaurant.domain.Lunch;
import com.spring.restaurant.domain.LunchType;
import com.spring.restaurant.entity.LunchEntity;
import com.spring.restaurant.entity.LunchTypeEntity;
import com.spring.restaurant.repository.LunchRepository;
import com.spring.restaurant.service.mapper.LunchMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LunchServiceImplTest {
    private static final Lunch LUNCH = initLunch();
    private static final LunchEntity LUNCH_ENTITY = initLunchEntity();

    @Mock
    private LunchRepository lunchRepository;
    @Mock
    private LunchMapper lunchMapper;
    @InjectMocks
    private LunchServiceImpl lunchService;

    @Test
    public void findAllByPageAndLunchType() {
        Pageable pageable = PageRequest.of(3, 9);
        when(lunchRepository.findAllByLunchTypeEntity(LUNCH_ENTITY.getLunchTypeEntity(), pageable))
                .thenReturn(Page.empty());

        Page<Lunch> lunchPage = lunchService.findAllByPageAndLunchType(3, LUNCH.getLunchType());

        assertEquals(lunchPage, Page.empty());
        verify(lunchRepository).findAllByLunchTypeEntity(LUNCH_ENTITY.getLunchTypeEntity(), pageable);
    }

    @Test
    public void pageCount() {
        when(lunchRepository.countAllByLunchTypeEntity(LUNCH_ENTITY.getLunchTypeEntity())).thenReturn(100L);

        int pages = lunchService.pageCount(LUNCH.getLunchType());

        assertTrue(pages > 0);
        verify(lunchRepository).countAllByLunchTypeEntity(LUNCH_ENTITY.getLunchTypeEntity());
    }

    @Test
    public void findById() {
        when(lunchRepository.findById(LUNCH.getId())).thenReturn(Optional.of(LUNCH_ENTITY));
        when(lunchMapper.mapEntityToDomain(LUNCH_ENTITY)).thenReturn(LUNCH);

        Optional<Lunch> lunch = lunchService.findById(LUNCH.getId());

        assertEquals(Optional.of(LUNCH), lunch);
        verify(lunchRepository).findById(LUNCH.getId());
        verify(lunchMapper).mapEntityToDomain(LUNCH_ENTITY);
    }

    private static Lunch initLunch() {
        final Lunch lunch = new Lunch();
        lunch.setId(1L);
        lunch.setName("Lunch");
        lunch.setDescription("Description");
        lunch.setImg("Image");
        lunch.setLunchType(LunchType.LUNCH);
        return lunch;
    }

    private static LunchEntity initLunchEntity() {
        final LunchEntity lunchEntity = new LunchEntity();
        lunchEntity.setId(1L);
        lunchEntity.setName("Lunch");
        lunchEntity.setDescription("Description");
        lunchEntity.setImg("Image");
        lunchEntity.setLunchTypeEntity(LunchTypeEntity.LUNCH);
        return lunchEntity;
    }
}