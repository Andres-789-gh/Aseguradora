package com.exe.app.Dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EvaluationDto {
    private Long id;
    private Long claimId;
    private String claimDescription;
    private Long adjusterId;
    private String adjusterName;
    private LocalDate evaluationDate;
    private String verdict;
    private Double approvedAmount;
}
