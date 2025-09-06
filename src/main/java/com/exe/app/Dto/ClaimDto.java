package com.exe.app.Dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ClaimDto {
    private Long id;
    private Long policyId;
    private String policyNumber;
    private LocalDate claimDate;
    private String description;
    private Double estimatedAmount;
    private String status;
}
