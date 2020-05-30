package com.spring.restaurant.controller;

import com.spring.restaurant.domain.CreditCard;
import com.spring.restaurant.domain.Dish;
import com.spring.restaurant.domain.Lunch;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(PurchaseController.class)
public class PurchaseControllerTest {
    public static CreditCard CREDIT_CARD;
    public static Order ORDER;
    public static User USER;
    public static Dish DISH;
    public static Lunch LUNCH;
    public static final String ADMIN_ROLE = "ADMIN";
    public static final String ID = "5";
    public static final String LUNCH_PLUS = "{action: \"plus\", type: \"lunch\", id: 5}";
    public static final String LUNCH_MINUS = "{action: \"minus\", type: \"lunch\", id: 5}";
    public static final String LUNCH_REMOVE = "{action: \"remove\", type: \"lunch\", id: 5}";
    public static final String DISH_PLUS = "{action: \"plus\", type: \"dish\", id: 5}";
    public static final String DISH_MINUS = "{action: \"minus\", type: \"dish\", id: 5}";
    public static final String DISH_REMOVE = "{action: \"remove\", type: \"dish\", id: 5}";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private OrderService orderService;
    @MockBean
    private DishService dishService;
    @MockBean
    private LunchService lunchService;
    @MockBean
    private UserService userService;

    @Before
    public void setUp() throws Exception {
        CREDIT_CARD = new CreditCard();
        USER = new User();
        ORDER = new Order();
        ORDER.setStatus(OrderStatus.SENT);
        ORDER.setLunches(new ArrayList<>());
        ORDER.setDishes(new ArrayList<>());
        DISH = new Dish();
        DISH.setId(Long.valueOf(ID));
        DISH.setPrice(100);
        LUNCH = new Lunch();
        LUNCH.setId(Long.valueOf(ID));
        List<Dish> dishes = new ArrayList<>();
        dishes.add(DISH);
        LUNCH.setDishes(dishes);
    }

    @Test
    @WithMockUser
    public void myOrdersPageTest() throws Exception{
        final List<Order> orders = new ArrayList<>();
        orders.add(ORDER);
        Page<Order> page = new PageImpl<>(orders, PageRequest.of(1, 10),30);

        when(orderService.pageCount(any())).thenReturn(3);
        when(orderService.findAllByUserAndStatusNot(any(), eq(OrderStatus.FORMED), eq(0)))
                .thenReturn(page);

        mvc.perform(get("/myOrders")
                .param("page", "1")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("user/myOrders"))
                .andExpect(model().attribute("orders", page));
    }

    @Test
    @WithMockUser
    public void myOrdersPageTestWithRedirect() throws Exception{
        when(orderService.pageCount(any())).thenReturn(3);

        mvc.perform(get("/myOrders")
                .param("page", "5")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/myOrders"));
    }

    @Test
    @WithMockUser(authorities = ADMIN_ROLE)
    public void adminPageTest() throws Exception{
        USER.setEmail("Email");
        ORDER.setUser(USER);
        final List<Order> orders = new ArrayList<>();
        orders.add(ORDER);
        Page<Order> page = new PageImpl<>(orders, PageRequest.of(1, 10),30);

        when(orderService.pageCountByStatus(OrderStatus.SENT)).thenReturn(3);
        when(orderService.findAllByStatus(OrderStatus.SENT, 0)).thenReturn(page);

        mvc.perform(get("/adminPage")
                .param("page", "1")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("admin/adminPage"))
                .andExpect(model().attribute("orders", page));
    }

    @Test
    @WithMockUser(authorities = ADMIN_ROLE)
    public void adminPageTestWithRedirect() throws Exception{
        when(orderService.pageCountByStatus(OrderStatus.SENT)).thenReturn(3);

        mvc.perform(get("/adminPage")
                .param("page", "5")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/adminPage"));
    }

    @Test
    @WithMockUser(authorities = ADMIN_ROLE)
    public void acceptOrdersTest() throws Exception{
        when(orderService.findById(Long.valueOf(ID))).thenReturn(Optional.ofNullable(ORDER));

        mvc.perform(post("/admin")
                .param("orderId", ID)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/adminPage?page=1"));
    }

    @Test
    @WithMockUser
    public void placeOrderTest() throws Exception {
        when(orderService.findByUserEntityAndStatus(any(), eq(OrderStatus.FORMED))).thenReturn(Optional.of(ORDER));

        mvc.perform(get("/placeOrder"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/myOrders?page=1"));

    }

    @Test
    @WithMockUser
    public void creditCardPageTest() throws Exception {
        when(orderService.findById(Long.valueOf(ID))).thenReturn(Optional.of(ORDER));

        mvc.perform(get("/creditCard")
                .param("orderId", ID)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("user/creditCard"));

    }

    @Test
    @WithMockUser
    public void creditCardPayingShouldBeSuccessful() throws Exception {
        when(orderService.findById(Long.valueOf(ID))).thenReturn(Optional.of(ORDER));

        mvc.perform(post("/creditCard")
                .param("orderId", ID)
                .param("creditCardNumber", "4197394215553472")
                .param("ccMonth", "12")
                .param("ccYear", "21")
                .param("cvv", "123")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/myOrders?page=1"));
    }

    @Test
    @WithMockUser
    public void creditCardPayingShouldBeNotSuccessful() throws Exception {
        when(orderService.findById(Long.valueOf(ID))).thenReturn(Optional.of(ORDER));

        mvc.perform(post("/creditCard")
                .param("orderId", ID)
                .param("creditCardNumber", "4197394215553472")
                .param("ccMonth", "12")
                .param("ccYear", "18")
                .param("cvv", "123")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("user/creditCard"));
    }

    @Test
    @WithMockUser
    public void basketPageTest() throws Exception {
        when(orderService.findByUserEntityAndStatus(any(), eq(OrderStatus.FORMED))).thenReturn(Optional.of(ORDER));

        mvc.perform(get("/basket"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/basket"));
    }

    @Test
    @WithMockUser
    public void basketPageTest2() throws Exception {
        ORDER.getDishes().add(DISH);
        ORDER.getLunches().add(LUNCH);
        when(orderService.findByUserEntityAndStatus(any(), eq(OrderStatus.FORMED))).thenReturn(Optional.of(ORDER));

        mvc.perform(get("/basket"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/basket"));
    }

    @Test
    @WithMockUser
    public void processBasketWithLunchPlus() throws Exception {
        ORDER.getDishes().add(DISH);
        ORDER.getLunches().add(LUNCH);
        when(orderService.findByUserEntityAndStatus(any(), eq(OrderStatus.FORMED))).thenReturn(Optional.of(ORDER));

        mvc.perform(post("/basket")
                .param("data", LUNCH_PLUS)
        )
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void processBasketWithLunchMinus() throws Exception {
        ORDER.getDishes().add(DISH);
        ORDER.getLunches().add(LUNCH);
        ORDER.getLunches().add(LUNCH);
        when(orderService.findByUserEntityAndStatus(any(), eq(OrderStatus.FORMED))).thenReturn(Optional.of(ORDER));

        mvc.perform(post("/basket")
                .param("data", LUNCH_MINUS)
        )
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void processBasketWithLunchMinus2() throws Exception {
        ORDER.getDishes().add(DISH);
        ORDER.getLunches().add(LUNCH);
        when(orderService.findByUserEntityAndStatus(any(), eq(OrderStatus.FORMED))).thenReturn(Optional.of(ORDER));

        mvc.perform(post("/basket")
                .param("data", LUNCH_MINUS)
        )
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void processBasketWithLunchRemove() throws Exception {
        ORDER.getDishes().add(DISH);
        ORDER.getLunches().add(LUNCH);
        when(orderService.findByUserEntityAndStatus(any(), eq(OrderStatus.FORMED))).thenReturn(Optional.of(ORDER));

        mvc.perform(post("/basket")
                .param("data", LUNCH_REMOVE)
        )
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void processBasketWithDishPlus() throws Exception {
        ORDER.getDishes().add(DISH);
        ORDER.getLunches().add(LUNCH);
        when(orderService.findByUserEntityAndStatus(any(), eq(OrderStatus.FORMED))).thenReturn(Optional.of(ORDER));

        mvc.perform(post("/basket")
                .param("data", DISH_PLUS)
        )
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void processBasketWithDishMinus() throws Exception {
        ORDER.getDishes().add(DISH);
        ORDER.getDishes().add(DISH);
        ORDER.getLunches().add(LUNCH);
        when(orderService.findByUserEntityAndStatus(any(), eq(OrderStatus.FORMED))).thenReturn(Optional.of(ORDER));

        mvc.perform(post("/basket")
                .param("data", DISH_MINUS)
        )
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void processBasketWithDishMinus2() throws Exception {
        ORDER.getDishes().add(DISH);
        ORDER.getLunches().add(LUNCH);
        when(orderService.findByUserEntityAndStatus(any(), eq(OrderStatus.FORMED))).thenReturn(Optional.of(ORDER));

        mvc.perform(post("/basket")
                .param("data", DISH_MINUS)
        )
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void processBasketWithDishRemove() throws Exception {
        ORDER.getDishes().add(DISH);
        ORDER.getLunches().add(LUNCH);
        when(orderService.findByUserEntityAndStatus(any(), eq(OrderStatus.FORMED))).thenReturn(Optional.of(ORDER));

        mvc.perform(post("/basket")
                .param("data", DISH_REMOVE)
        )
                .andExpect(status().isOk());
    }
}