package com.exe.app.Dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PolicyDto {
    private Long id;
    private Long clientId;
    private String clientName;
    private String policyNumber;
    private String policyType;
    private Double premium;
    private LocalDate validFrom;
    private LocalDate validUntil;
}
