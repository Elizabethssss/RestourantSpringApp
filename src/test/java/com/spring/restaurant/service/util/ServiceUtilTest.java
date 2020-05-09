package com.spring.restaurant.service.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static com.spring.restaurant.controller.util.ControllerUtil.REDIRECT;
import static com.spring.restaurant.controller.util.ControllerUtil.parsePageNumber;
import static com.spring.restaurant.service.util.ServiceUtil.getNumberOfPages;
import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class ServiceUtilTest {
    @Parameterized.Parameter
    public long numberOfItems;
    @Parameterized.Parameter(1)
    public int itemsPerPage;
    @Parameterized.Parameter(2)
    public int expected;

    @Parameterized.Parameters
    public static Collection<Object[]> testData() {
        return Arrays.asList(new Object[][] {
                {0, 0, 1},
                {1, 7, 1},
                {7, 7, 1},
                {14, 7, 2}
        });
    }

    @Test
    public void getNumberOfPagesTest() {
        assertEquals(expected, getNumberOfPages(numberOfItems, itemsPerPage));
    }
}