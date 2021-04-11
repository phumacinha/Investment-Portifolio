package one.digitalinnovation.investment.service;

import lombok.AllArgsConstructor;
import one.digitalinnovation.investment.dto.InvestmentDTO;
import one.digitalinnovation.investment.entity.Investment;
import one.digitalinnovation.investment.exception.InvestmentAlreadyRegisteredException;
import one.digitalinnovation.investment.exception.InvestmentInvalidExpirationDateException;
import one.digitalinnovation.investment.exception.InvestmentNotFoundException;
import one.digitalinnovation.investment.mapper.InvestmentMapper;
import one.digitalinnovation.investment.repository.InvestmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class InvestmentService {

    private final InvestmentRepository investmentRepository;
    private final InvestmentMapper investmentMapper = InvestmentMapper.INSTANCE;

    public InvestmentDTO createInvestment(InvestmentDTO investmentDTO) throws InvestmentAlreadyRegisteredException, InvestmentInvalidExpirationDateException {
        verifyIfExpirationDateIsAfterInitialDate(investmentDTO.getInitialDate(), investmentDTO.getExpirationDate());
        verifyIfIsAlreadyRegistered(investmentDTO.getName());
        Investment investment = investmentMapper.toModel(investmentDTO);
        Investment savedInvestment = investmentRepository.save(investment);
        return investmentMapper.toDTO(savedInvestment);
    }

    public InvestmentDTO findByName(String investmentName) throws InvestmentNotFoundException {
        Investment foundInvestment = investmentRepository.findByName(investmentName)
                    .orElseThrow(() -> new InvestmentNotFoundException(investmentName));
        return investmentMapper.toDTO(foundInvestment);
    }

    public List<InvestmentDTO> searchByName(String investmentName) {
        return investmentRepository
                .findByNameContaining(investmentName)
                .stream()
                .map(investmentMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<InvestmentDTO> listAll() {
        return investmentRepository.findAll().stream().map(investmentMapper::toDTO).collect(Collectors.toList());
    }

    public void deleteById(Long id) throws InvestmentNotFoundException {
        verifyIfExists(id);
        investmentRepository.deleteById(id);
    }

    public InvestmentDTO increment(Long id, double valueToIncrement) throws InvestmentNotFoundException {
        Investment investmentToIncrementValue = verifyIfExists(id);
        double valueAfterIncrement = investmentToIncrementValue.getValue() + valueToIncrement;
        investmentToIncrementValue.setValue(valueAfterIncrement);

        return investmentMapper.toDTO(investmentToIncrementValue);
    }

    public InvestmentDTO decrement(Long id, double valueToDecrement) throws InvestmentNotFoundException {
        Investment investmentToDecrementValue = verifyIfExists(id);
        double valueAfterIncrement = investmentToDecrementValue.getValue() + valueToDecrement;
        investmentToDecrementValue.setValue(valueAfterIncrement);

        return investmentMapper.toDTO(investmentToDecrementValue);
    }

    private void verifyIfIsAlreadyRegistered(String name) throws InvestmentAlreadyRegisteredException {
        Optional<Investment> optSavedInvestment = investmentRepository.findByName(name);
        if (optSavedInvestment.isPresent()) {
            throw new InvestmentAlreadyRegisteredException(name);
        }
    }

    private Investment verifyIfExists(Long id) throws InvestmentNotFoundException {
        return investmentRepository.findById(id).orElseThrow(() -> new InvestmentNotFoundException(id));
    }

    private void verifyIfExpirationDateIsAfterInitialDate(LocalDate initialDate, LocalDate expirationDate) throws InvestmentInvalidExpirationDateException {
        if (expirationDate != null && expirationDate.isBefore(initialDate)) {
            throw new InvestmentInvalidExpirationDateException();
        }
    }
}
