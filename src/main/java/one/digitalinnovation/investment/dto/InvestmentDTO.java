package one.digitalinnovation.investment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import one.digitalinnovation.investment.enums.InvestmentType;
import one.digitalinnovation.investment.enums.LiquidityFrequency;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvestmentDTO {

    private Long id;

    @NotNull
    @Size(min = 3, max = 100)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    private InvestmentType type;

    @NotNull
    @Positive
    private Double value;

    @NotNull
    @Enumerated(EnumType.STRING)
    private LiquidityFrequency liquidityFrequency;

    @Positive
    private Double yield;

    @NotNull
    private LocalDate initialDate;

    private LocalDate expirationDate;
}
