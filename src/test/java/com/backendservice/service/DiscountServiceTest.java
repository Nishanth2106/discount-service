package com.backendservice.service;

import com.backendservice.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class DiscountServiceTest {

    @InjectMocks
    private DiscountService discountService;

    @BeforeEach
    public void setUp() {
        discountService = new DiscountService();
    }

    @Test
     void testEmployeeDiscount() {
        User user = new User(1, UserType.EMPLOYEE, LocalDate.now());
        Item item1 = new Item(1, "Item1", ItemType.NON_GROCERY, 100);
        Bill bill = new Bill(1, user, List.of(item1));

        double netPayableAmount = discountService.calculateNetPayableAmount(bill);

        Assertions.assertEquals(65, netPayableAmount);
    }

    @Test
    void testAffiliateDiscount() {
        User user = new User(1, UserType.AFFILIATE, LocalDate.now());
        Item item1 = new Item(1, "Item1", ItemType.NON_GROCERY, 100);
        Bill bill = new Bill(1, user, List.of(item1));

        double netPayableAmount = discountService.calculateNetPayableAmount(bill);

        Assertions.assertEquals(85, netPayableAmount);
    }

    @Test
    void testCustomerLongTermDiscount() {
        User user = new User(1, UserType.CUSTOMER, LocalDate.now().minusYears(3));
        Item item1 = new Item(1, "Item1", ItemType.GROCERY, 100);
        Bill bill = new Bill(1, user, List.of(item1));

        double netPayableAmount = discountService.calculateNetPayableAmount(bill);

        Assertions.assertEquals(95, netPayableAmount);
    }

    @Test
    void testCustomerLongTermDiscountAndUserType() {
        User user = new User(1, UserType.EMPLOYEE, LocalDate.now().minusYears(1));
        Item item1 = new Item(1, "Item1", ItemType.GROCERY, 100);
        Bill bill = new Bill(1, user, Arrays.asList(item1));

        double netPayableAmount = discountService.calculateNetPayableAmount(bill);
        Assertions.assertEquals(95, netPayableAmount);
    }

    @Test
    void testAmountBasedDiscount() {
        User user = new User(1, UserType.CUSTOMER, LocalDate.now());
        Item item1 = new Item(1, "Item1", ItemType.NON_GROCERY, 490);
        Item item2 = new Item(2, "Item2", ItemType.NON_GROCERY, 490);
        Bill bill = new Bill(1, user, Arrays.asList(item1, item2));

        double netPayableAmount = discountService.calculateNetPayableAmount(bill);

        Assertions.assertEquals(935, netPayableAmount);
    }

    @Test
    void testPercentageDiscountNotAppliedOnGroceries() {
        User user = new User(1, UserType.CUSTOMER, LocalDate.now());
        Item item1 = new Item(1, "Item1", ItemType.GROCERY, 100);
        Item item2 = new Item(2, "Item2", ItemType.NON_GROCERY, 100);
        Bill bill = new Bill(1, user, Arrays.asList(item1, item2));

        double netPayableAmount = discountService.calculateNetPayableAmount(bill);

        Assertions.assertEquals(190, netPayableAmount);
    }

    @Test
    void testSinglePercentageDiscountApplied() {
        User user = new User(1, UserType.CUSTOMER, LocalDate.now().minusYears(3));
        Item item1 = new Item(1, "Item1", ItemType.NON_GROCERY, 100);
        Item item2 = new Item(2, "Item2", ItemType.NON_GROCERY, 100);
        Bill bill = new Bill(1, user, Arrays.asList(item1, item2));

        double netPayableAmount = discountService.calculateNetPayableAmount(bill);

        Assertions.assertEquals(180, netPayableAmount);
    }

    @Test
    void testDiscountExcludesGroceries() {
        User user = new User(1, UserType.CUSTOMER, LocalDate.now());
        Item groceryItem = new Item(1, "Grocery", ItemType.GROCERY, 100);
        Item nonGroceryItem = new Item(2, "Non-Grocery", ItemType.NON_GROCERY, 100);
        Bill bill = new Bill(1, user, Arrays.asList(groceryItem, nonGroceryItem));

        double netPayableAmount = discountService.calculateNetPayableAmount(bill);

        Assertions.assertEquals(190, netPayableAmount);
    }

    @Test
    void testNoLongTermDiscount() {
        User user = new User(1, UserType.CUSTOMER, LocalDate.now().minusYears(1));
        Item item = new Item(1, "Item", ItemType.NON_GROCERY, 100);
        Bill bill = new Bill(1, user, List.of(item));

        double netPayableAmount = discountService.calculateNetPayableAmount(bill);

        Assertions.assertEquals(95, netPayableAmount);
    }

    @Test
    void testOtherUserTypesNoLongTermDiscount() {
        User employee = new User(1, UserType.EMPLOYEE, LocalDate.now().minusYears(3));
        User affiliate = new User(2, UserType.AFFILIATE, LocalDate.now().minusYears(3));
        Item item = new Item(1, "Item", ItemType.NON_GROCERY, 100);

        Bill employeeBill = new Bill(1, employee, List.of(item));
        Bill affiliateBill = new Bill(2, affiliate, List.of(item));

        double employeeNetPayableAmount = discountService.calculateNetPayableAmount(employeeBill);
        double affiliateNetPayableAmount = discountService.calculateNetPayableAmount(affiliateBill);

        Assertions.assertEquals(65, employeeNetPayableAmount);
        Assertions.assertEquals(85, affiliateNetPayableAmount);
    }
}
