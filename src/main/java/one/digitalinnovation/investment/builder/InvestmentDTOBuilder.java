package one.digitalinnovation.investment.builder;

import lombok.Builder;
import one.digitalinnovation.investment.dto.InvestmentDTO;
import one.digitalinnovation.investment.enums.InvestmentType;
import one.digitalinnovation.investment.enums.LiquidityFrequency;

import java.time.LocalDate;

@Builder
public class InvestmentDTOBuilder {

    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private String name = "Tesouro SELIC 2024";

    @Builder.Default
    private InvestmentType type = InvestmentType.BOND;

    @Builder.Default
    private Double value = 1000D;

    @Builder.Default
    private LiquidityFrequency liquidityFrequency = LiquidityFrequency.AT_EXPIRATION;

    @Builder.Default
    private Double yield = 1.22122;

    @Builder.Default
    private LocalDate initialDate = LocalDate.now();

    @Builder.Default
    private LocalDate expirationDate = LocalDate.of(2024, 9, 1);

    public InvestmentDTO toInvestmentDTO() {
        return new InvestmentDTO(id, name, type, value, liquidityFrequency, yield, initialDate, expirationDate);
    }
}
