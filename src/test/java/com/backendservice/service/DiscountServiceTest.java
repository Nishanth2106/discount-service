package com.backendservice.service;

import com.backendservice.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DiscountServiceTest {

    @InjectMocks
    private DiscountService discountService;

    @BeforeEach
    public void setUp() {
        discountService = new DiscountService();
    }

    @Test
    void givenEmployeeUser_whenCalculateNetPayableAmount_thenApplyEmployeeDiscount() {
        User employee = new User(1, UserType.EMPLOYEE, LocalDate.now());
        Item item1 = new Item(1, "Item1", ItemType.NON_GROCERY, 100);
        Bill bill = new Bill(1, employee, List.of(item1));

        double netPayableAmount = discountService.calculateNetPayableAmount(bill);
        assertEquals(65, netPayableAmount);
    }


    @Test
    void givenAffiliateUser_whenCalculateNetPayableAmount_thenApplyAffiliateDiscount() {
        User user = new User(1, UserType.AFFILIATE, LocalDate.now());
        Item item1 = new Item(1, "Item1", ItemType.NON_GROCERY, 100);
        Bill bill = new Bill(1, user, List.of(item1));

        double netPayableAmount = discountService.calculateNetPayableAmount(bill);

        assertEquals(85, netPayableAmount);
    }

    @Test
    void givenCustomerWithLongTermMembership_whenCalculateNetPayableAmount_thenApplyCustomerLongTermDiscount() {
        User user = new User(1, UserType.CUSTOMER, LocalDate.now().minusYears(3));
        Item item1 = new Item(1, "Item1", ItemType.GROCERY, 100);
        Bill bill = new Bill(1, user, List.of(item1));

        double netPayableAmount = discountService.calculateNetPayableAmount(bill);

        assertEquals(95, netPayableAmount);
    }

    @Test
    void givenEmployeeWithLongTermMembershipAndGroceryItem_whenCalculateNetPayableAmount_thenApplyCustomerLongTermDiscount() {
        User user = new User(1, UserType.EMPLOYEE, LocalDate.now().minusYears(1));
        Item item1 = new Item(1, "Item1", ItemType.GROCERY, 100);
        Bill bill = new Bill(1, user, List.of(item1));

        double netPayableAmount = discountService.calculateNetPayableAmount(bill);
        assertEquals(95, netPayableAmount);
    }

    @Test
    void givenCustomerWithItemsEligibleForAmountBasedDiscount_whenCalculateNetPayableAmount_thenApplyAmountBasedDiscount() {
        User user = new User(1, UserType.CUSTOMER, LocalDate.now());
        Item item1 = new Item(1, "Item1", ItemType.NON_GROCERY, 490);
        Item item2 = new Item(2, "Item2", ItemType.NON_GROCERY, 490);
        Bill bill = new Bill(1, user, Arrays.asList(item1, item2));

        double netPayableAmount = discountService.calculateNetPayableAmount(bill);

        assertEquals(935, netPayableAmount);
    }

    @Test
    void givenItems_testPercentageDiscountNotAppliedOnGroceries() {
        User user = new User(1, UserType.CUSTOMER, LocalDate.now());
        Item item1 = new Item(1, "Item1", ItemType.GROCERY, 100);
        Item item2 = new Item(2, "Item2", ItemType.NON_GROCERY, 100);
        Bill bill = new Bill(1, user, Arrays.asList(item1, item2));

        double netPayableAmount = discountService.calculateNetPayableAmount(bill);

        assertEquals(190, netPayableAmount);
    }

    @Test
    void testSinglePercentageDiscountApplied() {
        User user = new User(1, UserType.CUSTOMER, LocalDate.now().minusYears(3));
        Item item1 = new Item(1, "Item1", ItemType.NON_GROCERY, 100);
        Item item2 = new Item(2, "Item2", ItemType.NON_GROCERY, 100);
        Bill bill = new Bill(1, user, Arrays.asList(item1, item2));

        double netPayableAmount = discountService.calculateNetPayableAmount(bill);

        assertEquals(180, netPayableAmount);
    }

    @Test
    void givenCustomer_testDiscountExcludesGroceries() {
        User user = new User(1, UserType.CUSTOMER, LocalDate.now());
        Item groceryItem = new Item(1, "Grocery", ItemType.GROCERY, 100);
        Item nonGroceryItem = new Item(2, "Non-Grocery", ItemType.NON_GROCERY, 100);
        Bill bill = new Bill(1, user, Arrays.asList(groceryItem, nonGroceryItem));

        double netPayableAmount = discountService.calculateNetPayableAmount(bill);

        assertEquals(190, netPayableAmount);
    }

    @Test
    void givenEmployee_whenNotMoreThan2Years_NotEligibleForDiscount() {
        User user = new User(1, UserType.CUSTOMER, LocalDate.now().minusYears(1));
        Item item = new Item(1, "Item", ItemType.NON_GROCERY, 100);
        Bill bill = new Bill(1, user, List.of(item));

        double netPayableAmount = discountService.calculateNetPayableAmount(bill);

        assertEquals(95, netPayableAmount);
    }

    @Test
    void givenEmployeeAndAffiliateUsers_whenCalculateNetPayableAmount_thenApplyNoLongTermDiscount() {
        User employee = new User(1, UserType.EMPLOYEE, LocalDate.now().minusYears(3));
        User affiliate = new User(2, UserType.AFFILIATE, LocalDate.now().minusYears(3));
        Item item = new Item(1, "Item", ItemType.NON_GROCERY, 100);

        Bill employeeBill = new Bill(1, employee, List.of(item));
        Bill affiliateBill = new Bill(2, affiliate, List.of(item));

        double employeeNetPayableAmount = discountService.calculateNetPayableAmount(employeeBill);
        double affiliateNetPayableAmount = discountService.calculateNetPayableAmount(affiliateBill);

        assertEquals(65, employeeNetPayableAmount);
        assertEquals(85, affiliateNetPayableAmount);
    }
}
