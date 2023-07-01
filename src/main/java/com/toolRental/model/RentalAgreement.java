package com.toolRental.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RentalAgreement {
    private String toolCode;
    private String toolLabel;
    private String brand;
    private LocalDate checkoutDate;
    private LocalDate returnDate;
    private int rentalDays;
    private int chargeableDays;
    private double charge;
    private double preDiscount;
    private double discountPercent;
    private double discountAmount;
    private double finalCharge;
}

