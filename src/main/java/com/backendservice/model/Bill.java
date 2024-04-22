package com.backendservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bill {
    private long billId;
    private User user;
    private List<Item> items;
    
}