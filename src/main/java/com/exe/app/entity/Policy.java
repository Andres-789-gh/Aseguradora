package com.exe.app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "policies")
@Data
public class Policy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_client", nullable = false)
    private Client client;

    @NotBlank
    @Column(nullable = false, unique = true, length = 50)
    private String policyNumber;

    @NotBlank
    @Column(nullable = false, length = 50)
    private String policyType;

    @NotNull
    @Column(nullable = false)
    private Double premium;

    @NotNull
    @Column(nullable = false)
    private LocalDate validFrom;

    @NotBlank
    @Column(nullable = false)
    private LocalDate validUntil;

    @OneToMany(mappedBy = "policy")
    private List<Claim> claims;
}
