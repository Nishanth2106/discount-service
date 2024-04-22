package com.backendservice.controller;

import com.backendservice.model.Bill;
import com.backendservice.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BillController {

    private final DiscountService discountService;

    @Autowired
    public BillController(DiscountService discountService) {
        this.discountService = discountService;
    }

    @PostMapping("/calculateNetPayableAmount")
    public double calculateNetPayableAmount(@RequestBody Bill bill) {
        return discountService.calculateNetPayableAmount(bill);
    }

}
