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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

import one.digitalinnovation.investment.enums.InvestmentType;

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
    private double value;

    @Column
    private double monthYield;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date initialDate;

    @Column
    @Temporal(TemporalType.DATE)
    private Date endDate;
}
