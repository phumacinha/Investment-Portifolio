package one.digitalinnovation.investment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.util.Date;

import one.digitalinnovation.investment.enums.InvestmentType;

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
    @DecimalMin(value = "0.01")
    private double value;

    @DecimalMin(value = "0.01")
    private double monthYield;

    @NotNull
    @PastOrPresent
    private Date initialDate;

    @Future
    private Date endDate;
}
