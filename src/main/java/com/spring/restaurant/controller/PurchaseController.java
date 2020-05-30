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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import static com.spring.restaurant.controller.util.ControllerUtil.REDIRECT;
import static com.spring.restaurant.controller.util.ControllerUtil.parsePageNumber;

@Controller
@SessionAttributes({"user", "inBasket"})
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PurchaseController {

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

    @GetMapping("/myOrders")
    public String myOrdersPage(@RequestParam String page, @ModelAttribute("user") User user, Model model, RedirectAttributes attributes) {
        final int totalPages = orderService.pageCount(user);
        final String parsePageStr = parsePageNumber(page, totalPages, "myOrders");
        if (redirectToFirstPage(attributes, parsePageStr)) {
            return parsePageStr;
        }
        int pageNumber = Integer.parseInt(parsePageStr);
        final Page<Order> orders = orderService.findAllByUserAndStatusNot(user, OrderStatus.FORMED, pageNumber);

        model.addAttribute("totalPages", totalPages);
        model.addAttribute("page", pageNumber + 1);
        model.addAttribute("oActive", true);
        model.addAttribute("orders", orders);
        return "user/myOrders";
    }

    private boolean redirectToFirstPage(RedirectAttributes attributes, String parsePageStr) {
        if (parsePageStr.contains(REDIRECT)) {
            attributes.addAttribute("page", 1);
            return true;
        }
        return false;
    }

    @GetMapping("/adminPage")
    public String adminPage(@RequestParam String page, Model model, RedirectAttributes attributes) {
        final int totalPages = orderService.pageCountByStatus(OrderStatus.SENT);
        final String parsePageStr = parsePageNumber(page, totalPages, "adminPage");
        if (redirectToFirstPage(attributes, parsePageStr)) {
            return parsePageStr;
        }
        int pageNumber = Integer.parseInt(parsePageStr);
        final Page<Order> orders = orderService.findAllByStatus(OrderStatus.SENT, pageNumber);

        model.addAttribute("totalPages", totalPages);
        model.addAttribute("page", pageNumber + 1);
        model.addAttribute("aActive", true);
        model.addAttribute("orders", orders);
        return "admin/adminPage";
    }

    @PostMapping("/admin")
    public String acceptOrder(@RequestParam String orderId) {
        final Optional<Order> order = orderService.findById(Long.valueOf(orderId));
        if(order.isPresent()) {
            order.get().setStatus(OrderStatus.CONFIRMED);
            orderService.saveOrder(order.get());
        }
        return "redirect:/adminPage?page=1";
    }

    @GetMapping("/placeOrder")
    public String placeOrder(Model model, @ModelAttribute("user") User user, @ModelAttribute("inBasket") int inBasket) {
        final Optional<Order> formedOrder = orderService.findByUserEntityAndStatus(user, OrderStatus.FORMED);
        if(formedOrder.isPresent()) {
            final Map<Dish, Integer> dishes = formedOrder.get().getDishIntegerMap();
            final Map<Lunch, Integer> lunches = formedOrder.get().getLunchIntegerMap();
            int totalPrice = getTotalPrice(dishes, lunches);

            formedOrder.get().setCost(totalPrice);
            formedOrder.get().setStatus(OrderStatus.SENT);
            formedOrder.get().setCreatedAt(LocalDate.now());
            orderService.saveOrder(formedOrder.get());
        }

        final Order newOrder = new Order();
        newOrder.setStatus(OrderStatus.FORMED);
        newOrder.setCost(0);
        newOrder.setCreatedAt(LocalDate.now());
        newOrder.setUser(user);
        orderService.saveOrder(newOrder);

        model.addAttribute("inBasket", 0);
        return "redirect:/myOrders?page=1";
    }

    @GetMapping("/creditCard")
    public String creditCardPage(@RequestParam String orderId, @ModelAttribute CreditCard creditCard, Model model) {
        final Optional<Order> order = orderService.findById(Long.valueOf(orderId));

        order.ifPresent(value -> model.addAttribute("order", value));
        model.addAttribute("creditCard", creditCard);
        return "user/creditCard";
    }

    @PostMapping("/creditCard")
    public String creditCardPay(@RequestParam String orderId, @ModelAttribute @Valid CreditCard creditCard, Errors errors, Model model) {
        final Optional<Order> order = orderService.findById(Long.valueOf(orderId));
        if(order.isPresent()) {
            if (!creditCard.isDateValid()) {
                errors.rejectValue("ccMonth", "error.creditCard", "Date is not valid!");
                errors.rejectValue("ccYear", "error.creditCard", "Date is not valid!");
            }
            if (errors.hasErrors()) {
                model.addAttribute("order", order.get());
                return "user/creditCard";
            }
            order.get().setStatus(OrderStatus.PAYED);
            orderService.saveOrder(order.get());
        }

        return "redirect:/myOrders?page=1";
    }

    @GetMapping("/basket")
    public String basketPage(Model model, @ModelAttribute("user") User user, @ModelAttribute("inBasket") int inBasket) {
        final Optional<Order> formedOrder = orderService.findByUserEntityAndStatus(user, OrderStatus.FORMED);
        int totalPrice = 0;

        if(formedOrder.isPresent()) {
            for (Dish dish : formedOrder.get().getDishes()) {
                totalPrice += dish.getPriceInt();
            }
            for (Lunch lunch : formedOrder.get().getLunches()) {
                totalPrice += lunch.getPrice();
            }
            model.addAttribute("totalPrice", totalPrice);
            model.addAttribute("dishIntegerMap", formedOrder.get().getDishIntegerMap());
            model.addAttribute("lunchIntegerMap", formedOrder.get().getLunchIntegerMap());
        }

        return "user/basket";
    }

    @PostMapping(value = {"/basket"})
    @ResponseBody
    public String processBasket(@RequestParam(name = "data") String data, @ModelAttribute("user") User user,
                                @ModelAttribute("inBasket") int inBasket, Model model) {
        final JSONObject jsonObject = new JSONObject(data);
        final String action = jsonObject.getString("action");
        final String type = jsonObject.getString("type");
        final Long id = jsonObject.getLong("id");
        final Order formedOrder = orderService.findByUserEntityAndStatus(user, OrderStatus.FORMED).orElse(new Order());

        final Map<Dish, Integer> dishes = formedOrder.getDishIntegerMap();
        final Map<Lunch, Integer> lunches = formedOrder.getLunchIntegerMap();
        BasketDishInfo basketDishInfo = new BasketDishInfo(id, action, inBasket);

        if(type.equals("dish")) {
            actionWithDishes(basketDishInfo, formedOrder, dishes);
        }
        if (type.equals("lunch")) {
            actionWithLunches(basketDishInfo, formedOrder, lunches);
        }

        int totalPrice = getTotalPrice(dishes, lunches);
        basketDishInfo.setTotalPrice(totalPrice);

        model.addAttribute("inBasket", basketDishInfo.itemsInBasket);
        return getJsonObject(basketDishInfo).toString();
    }

    private void actionWithDishes(BasketDishInfo basketDishInfo, Order formedOrder, Map<Dish, Integer> dishes) {
        switch (basketDishInfo.getAction()) {
            case "plus":
                plusDish(basketDishInfo, formedOrder, dishes);
                break;
            case "minus":
                minusDish(basketDishInfo, formedOrder, dishes);
                break;
            case "remove":
                removeDish(basketDishInfo, formedOrder, dishes);
                break;
            default: break;
        }
    }


    private void actionWithLunches(BasketDishInfo basketDishInfo, Order formedOrder, Map<Lunch, Integer> lunches) {
        switch (basketDishInfo.getAction()) {
            case "plus":
                plusLunch(basketDishInfo, formedOrder, lunches);
                break;
            case "minus":
                minusLunch(basketDishInfo, formedOrder, lunches);
                break;
            case "remove":
                removeLunch(basketDishInfo, formedOrder, lunches);
                break;
            default: break;
        }
    }

    private void plusDish(BasketDishInfo basketDishInfo, Order formedOrder, Map<Dish, Integer> dishes) {
        for (Map.Entry<Dish, Integer> entry : dishes.entrySet()) {
            if (entry.getKey().getId().equals(basketDishInfo.getItemId())) {
                dishes.replace(entry.getKey(), entry.getValue() + 1);
                formedOrder.getDishes().add(dishService.findById(basketDishInfo.itemId).orElse(null));
                orderService.saveOrder(formedOrder);
                basketDishInfo.setNumOfItems(entry.getValue());
                basketDishInfo.setPriceOfItems(entry.getKey().getPrice() * entry.getValue());
                basketDishInfo.setItemsInBasket(basketDishInfo.itemsInBasket + 1);
            }
        }
    }

    private void minusDish(BasketDishInfo basketDishInfo, Order formedOrder, Map<Dish, Integer> dishes) {
        Long itemId = basketDishInfo.itemId;
        for (Map.Entry<Dish, Integer> entry : dishes.entrySet()) {
            if (entry.getKey().getId().equals(itemId) && entry.getValue() > 1) {
                dishes.replace(entry.getKey(), entry.getValue() - 1);
                final Dish d = formedOrder.getDishes().stream()
                        .filter(dish -> dish.getId().equals(itemId)).findAny().orElse(null);
                formedOrder.getDishes().remove(d);
                orderService.saveOrder(formedOrder);
                basketDishInfo.setNumOfItems(entry.getValue());
                basketDishInfo.setPriceOfItems(entry.getKey().getPrice() * entry.getValue());
                basketDishInfo.setItemsInBasket(basketDishInfo.itemsInBasket - 1);
            } else if (entry.getKey().getId().equals(itemId) && entry.getValue() == 1) {
                basketDishInfo.setNumOfItems(1);
                basketDishInfo.setPriceOfItems(entry.getKey().getPrice());
            }
        }
    }

    private void removeDish(BasketDishInfo basketDishInfo, Order formedOrder, Map<Dish, Integer> dishes) {
        for (Map.Entry<Dish, Integer> entry : dishes.entrySet()) {
            if (entry.getKey().getId().equals(basketDishInfo.itemId)) {
                basketDishInfo.setItemsInBasket(basketDishInfo.itemsInBasket - entry.getValue());
                formedOrder.getDishes().removeIf(dish -> dish.getId().equals(basketDishInfo.itemId));
                orderService.saveOrder(formedOrder);
                dishes.replace(entry.getKey(), 0);
            }
        }
    }

    private void plusLunch(BasketDishInfo basketDishInfo, Order formedOrder, Map<Lunch, Integer> lunches) {
        for (Map.Entry<Lunch, Integer> entry : lunches.entrySet()) {
            if (entry.getKey().getId().equals(basketDishInfo.itemId)) {
                lunches.replace(entry.getKey(), entry.getValue() + 1);
                formedOrder.getLunches().add(lunchService.findById(basketDishInfo.itemId).orElse(null));
                orderService.saveOrder(formedOrder);
                basketDishInfo.setNumOfItems(entry.getValue());
                basketDishInfo.setPriceOfItems(entry.getKey().getPrice() * entry.getValue());
                basketDishInfo.setItemsInBasket(basketDishInfo.itemsInBasket + 1);
            }
        }
    }

    private void minusLunch(BasketDishInfo basketDishInfo, Order formedOrder, Map<Lunch, Integer> lunches) {
        Long itemId = basketDishInfo.itemId;
        for (Map.Entry<Lunch, Integer> entry : lunches.entrySet()) {
            if (entry.getKey().getId().equals(itemId) && entry.getValue() > 1) {
                lunches.replace(entry.getKey(), entry.getValue() - 1);
                final Lunch l = formedOrder.getLunches().stream()
                        .filter(lunch -> lunch.getId().equals(itemId)).findAny().orElse(null);
                formedOrder.getLunches().remove(l);
                orderService.saveOrder(formedOrder);
                basketDishInfo.setNumOfItems(entry.getValue());
                basketDishInfo.setPriceOfItems(entry.getKey().getPrice() * entry.getValue());
                basketDishInfo.setItemsInBasket(basketDishInfo.itemsInBasket - 1);
            } else if (entry.getKey().getId().equals(itemId) && entry.getValue() == 1) {
                basketDishInfo.setNumOfItems(1);
                basketDishInfo.setPriceOfItems(entry.getKey().getPrice());
            }
        }
    }

    private void removeLunch(BasketDishInfo basketDishInfo, Order formedOrder, Map<Lunch, Integer> lunches) {
        for (Map.Entry<Lunch, Integer> entry : lunches.entrySet()) {
            if (entry.getKey().getId().equals(basketDishInfo.itemId)) {
                basketDishInfo.setItemsInBasket(basketDishInfo.itemsInBasket - entry.getValue());
                formedOrder.getLunches().removeIf(lunch -> lunch.getId().equals(basketDishInfo.itemId));
                orderService.saveOrder(formedOrder);
                lunches.replace(entry.getKey(), 0);
            }
        }
    }

    private JSONObject getJsonObject(BasketDishInfo basketDishInfo) {
        JSONObject json = new JSONObject();
        json.put("numOfItems", basketDishInfo.numOfItems);
        json.put("priceOfItems", basketDishInfo.priceOfItems);
        json.put("totalPrice", basketDishInfo.totalPrice);
        json.put("totalDishes", basketDishInfo.itemsInBasket);
        return json;
    }

    private int getTotalPrice(Map<Dish, Integer> dishes, Map<Lunch, Integer> lunches) {
        int totalPrice = 0;

        for (Map.Entry<Dish, Integer> entry : dishes.entrySet()) {
            totalPrice += entry.getKey().getPrice() * entry.getValue();
        }
        for (Map.Entry<Lunch, Integer> entry : lunches.entrySet()) {
            totalPrice += entry.getKey().getPrice() * entry.getValue();
        }
        return totalPrice;
    }


    @Getter
    @Setter
    @AllArgsConstructor
    class BasketDishInfo {

        private Long itemId;
        private String action;
        private int itemsInBasket;
        private double numOfItems;
        private double priceOfItems;
        private double totalPrice;

        public BasketDishInfo(Long itemId, String action, int itemsInBasket) {
            this.itemId = itemId;
            this.action = action;
            this.itemsInBasket = itemsInBasket;
        }
    }
}
