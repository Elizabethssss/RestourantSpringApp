package com.spring.restaurant.service.util;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.spring.restaurant.service.util.ServiceUtil.getNumberOfPages;
import static org.testng.AssertJUnit.assertEquals;

public class ServiceUtilTestNG {

    @DataProvider
    public Object[][] testData() {
        return new Object[][] {
                {0, 0, 1},
                {1, 7, 1},
                {7, 7, 1},
                {14, 7, 2}
        };
    }

    @Test(dataProvider = "testData")
    public void getNumberOfPagesTest(long numberOfItems, int itemsPerPage, int expected) {
        assertEquals(expected, getNumberOfPages(numberOfItems, itemsPerPage));
    }
}