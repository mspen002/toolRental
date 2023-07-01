package com.toolRental.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toolRental.model.RentalAgreement;
import com.toolRental.model.RentalRequest;
import com.toolRental.model.ToolSupply;
import com.toolRental.repository.ToolSupplyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ToolRentalService {

    @Autowired
    private final ToolSupplyRepository toolSupplyRepository;
    
    private static Calendar cacheCalendar;

    public ToolSupply findOneTool(String toolCode){
        return this.toolSupplyRepository.findById(toolCode).orElseThrow(EntityNotFoundException::new);
    }

    public RentalAgreement performRentalRequest(RentalRequest rentalRequest) throws Exception {
        RentalAgreement rentalAgreement = new RentalAgreement();
        if(rentalRequest.getDiscountPercent() > 100 || rentalRequest.getDiscountPercent() < 0) {
            throw new Exception("Discount percent is invalid.");
        }
        if(rentalRequest.getRentalDays() < 1 || rentalRequest.getRentalDays() > 300) {
            throw new Exception("Invalid input for number of rental days.");
        }
        ToolSupply currentTool = this.toolSupplyRepository.findById(rentalRequest.getToolCode()).orElseThrow(EntityNotFoundException::new);
        //I know this doesn't account for if the date changes, or if there's a rental before that changes to one further back but I spent like 4 hours trying to get my H2 databases to work and I am more depressed than I've ever been in my life
        if(currentTool.getReturnDate().isBefore(rentalRequest.getCheckoutDate()) || currentTool.getCheckoutDate().isAfter(rentalRequest.getCheckoutDate().plusDays(rentalRequest.getRentalDays()))) {
            currentTool.setCheckoutDate(rentalRequest.getCheckoutDate());
            currentTool.setRentalDays(rentalRequest.getRentalDays());
            currentTool.setReturnDate(rentalRequest.getCheckoutDate().plusDays(rentalRequest.getRentalDays()));
            currentTool.setChargeableDays(getChargableDays(currentTool));
            rentalAgreement = generateRentalAgreement(currentTool, rentalRequest);
            toolSupplyRepository.save(currentTool);
        }
        else
            throw new Exception("Rental date for specified tool is currently unavailable.");
        System.out.println(rentalAgreement);
        return rentalAgreement;
    }

    //generate rentalAgreement with all math taken care of
    private RentalAgreement generateRentalAgreement(ToolSupply tool, RentalRequest rentalRequest) {
        double preDiscount = tool.getCharge() * tool.getChargeableDays();
        double discountPercent = rentalRequest.getDiscountPercent() * 0.01;
        BigDecimal discountExact = new BigDecimal(preDiscount * discountPercent).setScale(2, RoundingMode.HALF_UP);
        double discountAmount = discountExact.doubleValue();
        double finalCharge = preDiscount - discountAmount;

        return new RentalAgreement(tool.getToolCode(),
                                    tool.getToolLabel(),
                                    tool.getBrand(),
                                    tool.getCheckoutDate(),
                                    tool.getReturnDate(),
                                    tool.getRentalDays(),
                                    tool.getChargeableDays(),
                                    tool.getCharge(),
                                    preDiscount,
                                    rentalRequest.getDiscountPercent(),
                                    discountAmount,
                                    finalCharge);
    }

    //find all chargable days for a given rental request
    private Integer getChargableDays(ToolSupply tool) {
        Calendar cal = Calendar.getInstance();
        Instant instant = tool.getCheckoutDate().atTime(0, 0, 0).toInstant(ZoneOffset.UTC);
        Date date = Date.from(instant);
        cal.setTime(date);
        int chargableDays = 0;
        for(int i = 0; i < tool.getRentalDays(); i++)
        {
            if((cal.get(Calendar.DATE) == getInstanceOfDay(cal.get(Calendar.YEAR), Calendar.SEPTEMBER, Calendar.MONDAY, 1) || 
                cal.get(Calendar.DATE) == observedHoliday(cal, Calendar.JULY, 4)) && 
                tool.isHdCharge()) { 
                chargableDays++;
            }
            else {
                if(isWeekend(cal) && tool.isWeCharge())
                    chargableDays++;
                else if(!isWeekend(cal) && tool.isWdCharge())
                    chargableDays++;
                cal.add(Calendar.DATE, 1);
            }
            cal.add(Calendar.DATE, 1);
        }
        return chargableDays;
    }

    //find the nearest observable day for holidays
    private int observedHoliday(Calendar cal, int month, int day) {
        if(cal.get(Calendar.MONTH) == month && cal.get(Calendar.DATE) == day && !isWeekend(cal)){
            return day;
        }
        if(cal.get(month) == month && cal.get(Calendar.DATE) == day && isWeekend(cal)) {
            if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
                return day-1;
            else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
                return day+1;    
        }
        return -1;
    }

    //check to see if the day lands on a saturday or sunday
    private boolean isWeekend(Calendar cal) {
        return cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
    }
    
    //find the date of a specific day in a given month, eg, first monday of september
    public static int getInstanceOfDay(int year, int month, int day, int value) {
        cacheCalendar.set(Calendar.DAY_OF_WEEK, day);
        cacheCalendar.set(Calendar.DAY_OF_WEEK_IN_MONTH, value);
        cacheCalendar.set(Calendar.MONTH, month);
        cacheCalendar.set(Calendar.YEAR, year);
        return cacheCalendar.get(Calendar.DATE);
    }
}
