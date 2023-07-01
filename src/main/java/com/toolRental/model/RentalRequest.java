package com.toolRental.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RentalRequest {
    private String toolCode;
    private Integer rentalDays;
    private double discountPercent;
    private LocalDate checkoutDate;
}
