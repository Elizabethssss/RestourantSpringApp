package com.spring.restaurant.service.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ServiceUtil {

    public static int getNumberOfPages(long numberOfItems, int itemsPerPage) {
        if(numberOfItems == 0) {
            return 1;
        }
        return ((int) numberOfItems / itemsPerPage) + (numberOfItems % itemsPerPage == 0 ? 0 : 1);
    }
}
