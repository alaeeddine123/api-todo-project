package com.CHRESTAPI.todolist.controllers.testroles;


import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shop")
public class ShopController {

    @GetMapping("/inventory")
    public String getInventory() {
        return "Shop inventory - accessible to shopowners and admins";
    }

    @GetMapping("/sales")
    public String getSales() {
        return "Shop sales - accessible to shopowners and admins";
    }
}