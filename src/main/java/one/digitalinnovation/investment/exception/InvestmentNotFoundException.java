package one.digitalinnovation.investment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InvestmentNotFoundException extends Exception {
    public InvestmentNotFoundException(String investmentName) {
        super(String.format("Investment with name %s not found.", investmentName));
    }

    public InvestmentNotFoundException(Long id) {
            super(String.format("Investment with id %s not found.", id));
    }
}
