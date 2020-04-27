package com.spring.restaurant.controller.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ControllerUtil {
    public static final String REDIRECT = "redirect:/";
    public static String parsePageNumber(String page, int totalPages, String url) {
        if(page == null) {
            return REDIRECT + url;
        }
        try {
            int pageValue = Integer.parseInt(page) - 1;
            if(pageValue < 0 || pageValue > totalPages) {
                return REDIRECT + url;
            }
            return String.valueOf(pageValue);
        }
        catch (NumberFormatException ex) {
            return REDIRECT + url;
        }
    }
}
