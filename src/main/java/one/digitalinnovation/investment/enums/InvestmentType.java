package one.digitalinnovation.investment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InvestmentType {
    STOCK("Stock"),
    BOND("Bond"),
    CRYPTOCURRENCY("Cryptocurrency"),
    INVESTMENT_FUND("Investment Funds"),
    SAVING_ACCOUNT("Saving account"),
    BUILDING_SOCIETY("Building society");

    private final String description;
}
