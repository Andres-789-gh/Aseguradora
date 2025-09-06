package com.exe.app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Table(name = "claims")
public class Claim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate claimDate;

    @NotBlank
    @Column(nullable = false, length = 225)
    private String description;

    @Column(nullable = false)
    private Double estimatedAmount;

    @NotBlank
    @Column(nullable = false, length = 50)
    private String status;

    @ManyToOne
    @JoinColumn(name = "id_policy", nullable = false)
    private Policy policy;

    @OneToMany(mappedBy = "claim")
    private List<Evaluation> evaluations;
}
