package com.backendservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    private long itemId;
    private String itemName;
    private ItemType itemType;
    private double itemPrice;
}