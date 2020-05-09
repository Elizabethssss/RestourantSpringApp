package com.spring.restaurant.controller.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

import static com.spring.restaurant.controller.util.ControllerUtil.REDIRECT;
import static com.spring.restaurant.controller.util.ControllerUtil.parsePageNumber;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class ControllerUtilTest {
    public static final String MENU = "menu";
    @Parameter
    public String parameter;
    @Parameter(1)
    public int totalPages;
    @Parameter(2)
    public String url;
    @Parameter(3)
    public String expected;

    @Parameters
    public static Collection<Object[]> testData() {
        return Arrays.asList(new Object[][] {
            {"1", 2, MENU, "0"},
            {"-5", 2, MENU, REDIRECT + MENU},
            {"100", 2, MENU, REDIRECT + MENU},
            {null, 2, MENU, REDIRECT + MENU},
            {"1a", 2, MENU, REDIRECT + MENU}
        });
    }

    @Test
    public void parsePageNumberTest() {
        assertEquals(expected, parsePageNumber(parameter, totalPages, url));
    }
}