package com.toolRental.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.toolRental.model.RentalAgreement;
import com.toolRental.model.RentalRequest;
import com.toolRental.model.ToolSupply;
import com.toolRental.service.ToolRentalService;
import lombok.RequiredArgsConstructor;

@RestController
@RestControllerAdvice
@RequestMapping("/tools")
@RequiredArgsConstructor
public class ToolRentalController {
    private final ToolRentalService service;
        
    @GetMapping("/fetch/{toolCode}")
    @ResponseStatus(HttpStatus.OK)
    public ToolSupply retrieveUser(@PathVariable String toolCode) {
        return this.service.findOneTool(toolCode);
    }

    @PostMapping("/requestRental")
    @ResponseStatus(HttpStatus.OK)
    public RentalAgreement performRentalRequest(RentalRequest rentalRequest) {
        try {
            return this.service.performRentalRequest(rentalRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
