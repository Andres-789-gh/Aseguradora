package com.exe.app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "evaluations")
@Data
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate evaluationDate;

    @NotBlank
    @Column(nullable = false, length = 255)
    private String verdict;

    @Column(nullable = false)
    private Double approvedAmount;

    @ManyToOne
    @JoinColumn(name = "id_claim", nullable = false)
    private Claim claim;

    @ManyToOne
    @JoinColumn(name = "id_adjuster", nullable = false)
    private Adjuster adjuster;
}
