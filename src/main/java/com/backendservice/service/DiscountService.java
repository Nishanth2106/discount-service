package com.backendservice.service;

import com.backendservice.model.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DiscountService {

    public double calculateNetPayableAmount(Bill bill) {
        double totalAmount = calculateTotalAmount(bill);
        double discountAmount = calculateDiscountAmount(bill);
        return totalAmount - discountAmount;
    }

    private double calculateTotalAmount(Bill bill) {
        double totalAmount = 0;
        for (Item item : bill.getItems()) {
            totalAmount += item.getItemPrice();
        }
        return totalAmount;
    }

    private double calculateDiscountAmount(Bill bill) {
        double discountAmount = 0;
        discountAmount += calculatePercentageBasedDiscount(bill);
        discountAmount += calculateAmountBasedDiscount(bill);
        return discountAmount;
    }

    private double calculatePercentageBasedDiscount(Bill bill) {
        double discountPercentage = 0;
        User user = bill.getUser();
        boolean isPercentageDiscountApplied = false;

        if (user.getUserType() == UserType.EMPLOYEE) {
            discountPercentage = DiscountConstants.EMPLOYEE_DISCOUNT_PERCENTAGE;
            isPercentageDiscountApplied = true;
        } else if (user.getUserType() == UserType.AFFILIATE) {
            discountPercentage = DiscountConstants.AFFILIATE_DISCOUNT_PERCENTAGE;
            isPercentageDiscountApplied = true;
        } else if (isLongTermCustomer(user.getRegistrationDate())) {
            discountPercentage = DiscountConstants.LONG_TERM_CUSTOMER_DISCOUNT_PERCENTAGE;
            isPercentageDiscountApplied = true;
        }

        if (isPercentageDiscountApplied) {
            discountPercentage = Math.min(discountPercentage, 100);
            return calculateDiscountAmountForPercentage(bill, discountPercentage);
        } else {
            return 0;
        }
    }

    private double calculateDiscountAmountForPercentage(Bill bill, double discountPercentage) {
        double totalNonGroceryAmount = calculateTotalNonGroceryAmount(bill);
        return totalNonGroceryAmount * discountPercentage / 100;
    }

    private double calculateTotalNonGroceryAmount(Bill bill) {
        double totalNonGroceryAmount = 0;
        for (Item item : bill.getItems()) {
            if (item.getItemType() != ItemType.GROCERY) {
                totalNonGroceryAmount += item.getItemPrice();
            }
        }
        return totalNonGroceryAmount;
    }

    private boolean isLongTermCustomer(LocalDate registrationDate) {
        return registrationDate.plusYears(2).isBefore(LocalDate.now());
    }

    private double calculateAmountBasedDiscount(Bill bill) {
        double totalAmount = calculateTotalAmount(bill);
        return Math.floor(totalAmount / 100) * 5;
    }
}
