package one.digitalinnovation.investment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvestmentAlreadyRegisteredException extends Exception {
    public InvestmentAlreadyRegisteredException(String investmentName) {
        super(String.format("Investment with name %s already registered in the system.", investmentName));
    }
}
