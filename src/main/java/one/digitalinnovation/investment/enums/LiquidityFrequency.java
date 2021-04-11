package one.digitalinnovation.investment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LiquidityFrequency {
    DAILY("Daily"),
    WEEKLY("Weekly"),
    MONTHLY("Monthly"),
    YEARLY("Yearly"),
    AT_EXPIRATION("At expiration"),
    VARIABLE("Variable");

    private final String description;
}
