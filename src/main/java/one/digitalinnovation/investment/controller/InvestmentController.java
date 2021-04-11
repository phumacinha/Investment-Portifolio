package one.digitalinnovation.investment.controller;

import lombok.AllArgsConstructor;
import one.digitalinnovation.investment.dto.AmountDTO;
import one.digitalinnovation.investment.dto.InvestmentDTO;
import one.digitalinnovation.investment.exception.InsufficientBalanceForWithdrawalException;
import one.digitalinnovation.investment.exception.InvestmentAlreadyRegisteredException;
import one.digitalinnovation.investment.exception.InvestmentInvalidExpirationDateException;
import one.digitalinnovation.investment.exception.InvestmentNotFoundException;
import one.digitalinnovation.investment.service.InvestmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/investments")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class InvestmentController {

    private final InvestmentService investmentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InvestmentDTO createInvestment(@RequestBody @Valid InvestmentDTO investmentDTO)
            throws InvestmentAlreadyRegisteredException, InvestmentInvalidExpirationDateException {
        return investmentService.createInvestment(investmentDTO);
    }

    @GetMapping("/{name}")
    public InvestmentDTO findByName(@PathVariable String name) throws InvestmentNotFoundException {
        return investmentService.findByName(name);
    }

    @GetMapping("/search/{name}")
    public List<InvestmentDTO> searchByName(@PathVariable String name) {
        return investmentService.searchByName(name);
    }

    @GetMapping
    public List<InvestmentDTO> listInvestments() {
        return investmentService.listAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) throws InvestmentNotFoundException {
        investmentService.deleteById(id);
    }

    @PatchMapping("/{id}/apply")
    public InvestmentDTO applyInInvestment(@PathVariable Long id, @RequestBody @Valid AmountDTO amountDTO) throws InvestmentNotFoundException {
        return investmentService.apply(id, amountDTO.getAmount());
    }

    @PatchMapping("/{id}/withdraw")
    public InvestmentDTO withdrawFromInvestment(@PathVariable Long id, @RequestBody @Valid AmountDTO amountDTO) throws InvestmentNotFoundException, InsufficientBalanceForWithdrawalException {
        return investmentService.withdraw(id, amountDTO.getAmount());
    }
}
