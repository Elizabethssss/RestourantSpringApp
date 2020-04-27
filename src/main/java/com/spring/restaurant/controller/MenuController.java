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
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static com.spring.restaurant.controller.util.ControllerUtil.REDIRECT;
import static com.spring.restaurant.controller.util.ControllerUtil.parsePageNumber;

@Controller
@SessionAttributes({"user", "inBasket"})
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class MenuController {

    public static final String IN_BASKET = "inBasket";
    private final UserService userService;
    private final OrderService orderService;
    private final DishService dishService;
    private final LunchService lunchService;

    @ModelAttribute(name = "user")
    public User user() {
        return new User();
    }

    @ModelAttribute(name = "inBasket")
    public int inBasket() {
        return 0;
    }

    @GetMapping("/index")
    public String homePage(Model model, @ModelAttribute("user") User user, @ModelAttribute("inBasket") int inBasket) {
        final Optional<Order> formedOrder = orderService.findByUserEntityAndStatus(user, OrderStatus.FORMED);
        if (!formedOrder.isPresent()) {
            final Order order = new Order();
            order.setStatus(OrderStatus.FORMED);
            order.setCost(0);
            order.setCreatedAt(LocalDate.now());
            order.setUser(user);
            orderService.saveOrder(order);
        } else {
            inBasket = formedOrder.get().getDishes().size() + formedOrder.get().getLunches().size();
        }

        model.addAttribute(IN_BASKET, inBasket);
        model.addAttribute("user", user);
        model.addAttribute("hActive", true);
        return "user/index";
    }

    @PostMapping("/index")
    public String performLogin(@ModelAttribute User user, RedirectAttributes attributes) {
        user = userService.findByEmail(user.getEmail()).orElse(null);
        attributes.addFlashAttribute("user", user);
        return "redirect:/index";
    }

    @GetMapping("/menu")
    public String menuPage(@RequestParam String type, @RequestParam String page, Model model,
                           RedirectAttributes attributes) {
        int totalPages;
        int pageNumber;
        if (isLunch(type)) {
            totalPages = lunchService.pageCount(LunchType.valueOf(type));
            final String parsePageStr = parsePageNumber(page, totalPages, "menu");
            if (redirectToFirstPage(type, attributes, parsePageStr)) {
                return parsePageStr;
            }
            pageNumber = Integer.parseInt(parsePageStr);
            final Page<Lunch> lunches = lunchService.findAllByPageAndLunchType(pageNumber, LunchType.valueOf(type));
            model.addAttribute("lunches", lunches);
        } else {
            totalPages = dishService.pageCount(DishType.valueOf(type));
            final String parsePageStr = parsePageNumber(page, totalPages, "menu");
            if (redirectToFirstPage(type, attributes, parsePageStr)) {
                return parsePageStr;
            }
            pageNumber = Integer.parseInt(parsePageStr);
            model.addAttribute("dishes", dishService.findAllByPageAndDishType(pageNumber, DishType.valueOf(type)));
        }
        addAttributesToMenuModel(type, model, totalPages, pageNumber);
        return "user/menu";
    }

    private boolean isLunch(@RequestParam String type) {
        return type.equals(LunchType.BREAKFAST.name()) || type.equals(LunchType.LUNCH.name())
                || type.equals(LunchType.HOLIDAY.name());
    }

    private boolean redirectToFirstPage(@RequestParam String type, RedirectAttributes attributes, String parsePageStr) {
        if (parsePageStr.contains(REDIRECT)) {
            attributes.addAttribute("type", type);
            attributes.addAttribute("page", 1);
            return true;
        }
        return false;
    }

    private void addAttributesToMenuModel(@RequestParam String type, Model model, int totalPages, int pageNumber) {
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("page", pageNumber + 1);
        model.addAttribute("type", type);
        model.addAttribute("mActive", true);
    }

    @GetMapping("/dish")
    public String dishPage(@RequestParam String dishId, Model model) {
        final Long dishIdValue = Long.valueOf(dishId);
        model.addAttribute("dish", dishService.findById(dishIdValue).orElse(null));
        return "user/dish";
    }

    @PostMapping("/dish")
    public String buyDish(@RequestParam(name = "dishId") String dishId, @ModelAttribute("user") User user,
                          @ModelAttribute("inBasket") int inBasket, RedirectAttributes attributes) {
        final Long dishIdValue = Long.valueOf(dishId);
        final Optional<Order> formedOrder = orderService.findByUserEntityAndStatus(user, OrderStatus.FORMED);
        final Optional<Dish> dish = dishService.findById(dishIdValue);
        if(formedOrder.isPresent() && dish.isPresent()) {
            formedOrder.get().getDishes().add(dish.get());
            orderService.saveOrder(formedOrder.get());
            inBasket++;
        }
        attributes.addFlashAttribute("user", user);
        attributes.addFlashAttribute(IN_BASKET, inBasket);
        attributes.addAttribute("dishId", dishId);
        return "redirect:/dish";
    }

    @GetMapping("/lunch")
    public String lunchPage(@RequestParam String lunchId, Model model) {
        final Long idValue = Long.valueOf(lunchId);
        final Optional<Lunch> lunch = lunchService.findById(idValue);
        if(lunch.isPresent()) {
            if (lunch.get().getTimeFrom().compareTo(LocalTime.now()) > 0 ||
                    lunch.get().getTimeTo().compareTo(LocalTime.now()) < 0) {
                model.addAttribute("disabled", true);
            }
            model.addAttribute("lunch", lunch.get());
        }
        return "user/lunch";
    }

    @PostMapping("/lunch")
    public String buyLunch(@RequestParam(name = "lunchId") String lunchId, @ModelAttribute("user") User user,
                           @ModelAttribute("inBasket") int inBasket, RedirectAttributes attributes) {
        final Long idValue = Long.valueOf(lunchId);
        final Optional<Order> formedOrder = orderService.findByUserEntityAndStatus(user, OrderStatus.FORMED);
        final Optional<Lunch> lunch = lunchService.findById(idValue);
        if(formedOrder.isPresent() && lunch.isPresent()) {
            formedOrder.get().getLunches().add(lunch.get());
            orderService.saveOrder(formedOrder.get());
            inBasket++;
        }
        attributes.addFlashAttribute("user", user);
        attributes.addFlashAttribute(IN_BASKET, inBasket);
        attributes.addAttribute("lunchId", idValue);
        return "redirect:/lunch";
    }

}