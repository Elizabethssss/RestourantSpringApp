package com.spring.restaurant.controller.util;

import com.spring.restaurant.domain.User;
import com.spring.restaurant.exception.UnauthorizedException;
import com.spring.restaurant.service.UserService;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.context.SecurityContextHolder;

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

    public static User getUserFromSecurityContext(UserService service) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return service.findByEmail(username)
                .orElseThrow(() -> new UnauthorizedException("unauthorized.request"));
    }

}
