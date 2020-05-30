package com.spring.restaurant.controller;

import com.spring.restaurant.domain.Dish;
import com.spring.restaurant.domain.DishType;
import com.spring.restaurant.domain.Lunch;
import com.spring.restaurant.domain.LunchType;
import com.spring.restaurant.domain.Order;
import com.spring.restaurant.domain.OrderStatus;
import com.spring.restaurant.domain.User;
import com.spring.restaurant.service.DishService;
import com.spring.restaurant.service.LunchService;
import com.spring.restaurant.service.OrderService;
import com.spring.restaurant.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(MenuController.class)
public class MenuControllerTest {
    private static final User USER = new User();
    private static final Dish DISH = new Dish();
    private static Order ORDER;
    private static Lunch LUNCH;
    public static final String ID = "5";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;
    @MockBean
    private OrderService orderService;
    @MockBean
    private DishService dishService;
    @MockBean
    private LunchService lunchService;

    @Before
    public void setUp() {
        ORDER = new Order();
        LUNCH = new Lunch();
    }

    @Test
    @WithMockUser
    public void homePageTestWithNoFormedOrder() throws Exception {
        when(orderService.findByUserEntityAndStatus(any(), eq(OrderStatus.FORMED))).thenReturn(Optional.empty());

        mvc.perform(get("/index"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/index"))
                .andExpect(model().attribute("hActive", true));
    }

    @Test
    @WithMockUser
    public void homePageTestWithFormedOrder() throws Exception {
        ORDER.setDishes(new ArrayList<>());
        ORDER.setLunches(new ArrayList<>());

        when(orderService.findByUserEntityAndStatus(any(), eq(OrderStatus.FORMED))).thenReturn(Optional.of(ORDER));

        mvc.perform(get("/index"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/index"))
                .andExpect(model().attribute("hActive", true));
    }

    @Test
    @WithMockUser
    public void performLoginTest() throws Exception {
        when(userService.findByEmail(anyString())).thenReturn(Optional.of(USER));

        mvc.perform(post("/index"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/index"));
    }

    @Test
    @WithMockUser
    public void lunchMenuPageTest() throws Exception {
        when(lunchService.pageCount(any())).thenReturn(3);
        when(lunchService.findAllByPageAndLunchType(1, LunchType.BREAKFAST)).thenReturn(Page.empty());

        mvc.perform(get("/menu")
                .param("type", "BREAKFAST")
                .param("page", "1")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("user/menu"));
    }

    @Test
    @WithMockUser
    public void lunchMenuPageTest2() throws Exception {
        when(lunchService.pageCount(any())).thenReturn(3);
        when(lunchService.findAllByPageAndLunchType(1, LunchType.HOLIDAY)).thenReturn(Page.empty());

        mvc.perform(get("/menu")
                .param("type", "HOLIDAY")
                .param("page", "1")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("user/menu"));
    }

    @Test
    @WithMockUser
    public void lunchMenuPageTest3() throws Exception {
        when(lunchService.pageCount(any())).thenReturn(3);
        when(lunchService.findAllByPageAndLunchType(1, LunchType.LUNCH)).thenReturn(Page.empty());

        mvc.perform(get("/menu")
                .param("type", "LUNCH")
                .param("page", "1")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("user/menu"));
    }

    @Test
    @WithMockUser
    public void lunchMenuPageTestWithRedirect() throws Exception {
        when(lunchService.pageCount(any())).thenReturn(3);
        when(lunchService.findAllByPageAndLunchType(5, LunchType.BREAKFAST)).thenReturn(Page.empty());

        mvc.perform(get("/menu")
                .param("type", "BREAKFAST")
                .param("page", "5")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/menu"));
    }

    @Test
    @WithMockUser
    public void dishMenuPageTest() throws Exception {
        when(dishService.pageCount(any())).thenReturn(3);
        when(dishService.findAllByPageAndDishType(1, DishType.SALAD)).thenReturn(Page.empty());

        mvc.perform(get("/menu")
                .param("type", "SALAD")
                .param("page", "1")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("user/menu"));
    }

    @Test
    @WithMockUser
    public void dishMenuPageTestWithRedirect() throws Exception {
        when(dishService.pageCount(any())).thenReturn(3);
        when(dishService.findAllByPageAndDishType(5, DishType.SALAD)).thenReturn(Page.empty());

        mvc.perform(get("/menu")
                .param("type", "SALAD")
                .param("page", "5")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/menu"));
    }

    @Test
    @WithMockUser
    public void dishPageTest() throws Exception {
        when(dishService.findById(any())).thenReturn(Optional.of(DISH));

        mvc.perform(get("/dish")
                .param("dishId", ID)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("user/dish"))
                .andExpect(model().attribute("dish", DISH));
    }

    @Test
    @WithMockUser
    public void buyDishTest() throws Exception {
        ORDER.setDishes(new ArrayList<>());

        when(orderService.findByUserEntityAndStatus(any(), eq(OrderStatus.FORMED))).thenReturn(Optional.of(ORDER));
        when(dishService.findById(Long.valueOf(ID))).thenReturn(Optional.of(DISH));

        mvc.perform(post("/dish")
                .param("dishId", ID)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/dish"));
    }

    @Test
    @WithMockUser
    public void lunchPageWithOneTime() throws Exception {
        LUNCH.setLunchType(LunchType.HOLIDAY);
        LUNCH.setDishes(new ArrayList<>());

        when(lunchService.findById(any())).thenReturn(Optional.of(LUNCH));

        mvc.perform(get("/lunch")
                .param("lunchId", ID)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("user/lunch"))
                .andExpect(model().attribute("lunch", LUNCH));
    }

    @Test
    @WithMockUser
    public void lunchPageWithAnotherTime() throws Exception {
        LUNCH.setLunchType(LunchType.BREAKFAST);
        LUNCH.setDishes(new ArrayList<>());

        when(lunchService.findById(any())).thenReturn(Optional.of(LUNCH));

        mvc.perform(get("/lunch")
                .param("lunchId", ID)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("user/lunch"))
                .andExpect(model().attribute("disabled", true))
                .andExpect(model().attribute("lunch", LUNCH));
    }

    @Test
    @WithMockUser
    public void buyLunchTest() throws Exception {
        ORDER.setLunches(new ArrayList<>());

        when(orderService.findByUserEntityAndStatus(any(), eq(OrderStatus.FORMED))).thenReturn(Optional.of(ORDER));
        when(lunchService.findById(Long.valueOf(ID))).thenReturn(Optional.of(LUNCH));

        mvc.perform(post("/lunch")
                .param("lunchId", ID)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/lunch"));
    }
}