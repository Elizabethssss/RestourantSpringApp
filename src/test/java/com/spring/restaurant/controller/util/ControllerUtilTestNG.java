package com.spring.restaurant.controller.util;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.spring.restaurant.controller.util.ControllerUtil.REDIRECT;
import static com.spring.restaurant.controller.util.ControllerUtil.parsePageNumber;
import static org.junit.Assert.assertEquals;

public class ControllerUtilTestNG {
    public static final String MENU = "menu";

    @DataProvider
    public Object[][] testData() {
        return new Object[][] {
            {"1", 2, MENU, "0"},
            {"-5", 2, MENU, REDIRECT + MENU},
            {"100", 2, MENU, REDIRECT + MENU},
            {null, 2, MENU, REDIRECT + MENU},
            {"1a", 2, MENU, REDIRECT + MENU}
        };
    }

    @Test(dataProvider = "testData")
    public void parsePageNumberTest(String parameter, int totalPages, String url, String expected) {
        assertEquals(expected, parsePageNumber(parameter, totalPages, url));
    }
}