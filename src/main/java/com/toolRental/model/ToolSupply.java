package com.toolRental.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Id;  
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "TOOLS")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ToolSupply {
    @Id
    private String toolCode;
    private String toolLabel;
    private String brand;
    private double charge;
    private boolean wdCharge;
    private boolean weCharge;
    private boolean hdCharge;
    private LocalDate checkoutDate;
    private LocalDate returnDate;
    private int rentalDays;
    private int chargeableDays;
}
