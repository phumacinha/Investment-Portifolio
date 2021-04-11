package one.digitalinnovation.investment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InsufficientBalanceForWithdrawalException extends Exception {
    public InsufficientBalanceForWithdrawalException(Double withdrawAmount) {
        super(String.format("Insufficient balance to withdraw $ %.2f.", withdrawAmount));
    }
}
