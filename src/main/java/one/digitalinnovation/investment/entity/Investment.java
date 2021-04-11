package one.digitalinnovation.investment.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

import one.digitalinnovation.investment.enums.InvestmentType;
import one.digitalinnovation.investment.enums.LiquidityFrequency;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Investment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private InvestmentType type;

    @Column(nullable = false)
    private Double value;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LiquidityFrequency liquidityFrequency;

    @Column
    private Double yield;

    @Column(nullable = false)
    private LocalDate initialDate;

    @Column
    private LocalDate expirationDate;
}
