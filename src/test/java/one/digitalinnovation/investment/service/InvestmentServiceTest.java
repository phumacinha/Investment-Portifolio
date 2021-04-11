package one.digitalinnovation.investment.service;

import one.digitalinnovation.investment.builder.InvestmentDTOBuilder;
import one.digitalinnovation.investment.dto.InvestmentDTO;
import one.digitalinnovation.investment.entity.Investment;
import one.digitalinnovation.investment.exception.InvestmentAlreadyRegisteredException;
import one.digitalinnovation.investment.exception.InvestmentInvalidExpirationDateException;
import one.digitalinnovation.investment.exception.InvestmentNotFoundException;
import one.digitalinnovation.investment.mapper.InvestmentMapper;
import one.digitalinnovation.investment.repository.InvestmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InvestmentServiceTest {

    private static final long INVALID_INVESTMENT_ID = 1L;
    private final InvestmentMapper investmentMapper = InvestmentMapper.INSTANCE;
    @Mock
    private InvestmentRepository investmentRepository;
    @InjectMocks
    private InvestmentService investmentService;

    @Test
    void whenInvestmentInformedThenItShouldBeCreated() throws InvestmentAlreadyRegisteredException, InvestmentInvalidExpirationDateException {
        // given
        InvestmentDTO expectedInvestmentDTO = InvestmentDTOBuilder.builder().build().toInvestmentDTO();
        Investment expectedSavedInvestment = investmentMapper.toModel(expectedInvestmentDTO);

        // when
        when(investmentRepository.findByName(expectedInvestmentDTO.getName())).thenReturn(Optional.empty());
        when(investmentRepository.save(expectedSavedInvestment)).thenReturn(expectedSavedInvestment);

        // then
        InvestmentDTO createdInvestmentDTO = investmentService.createInvestment(expectedInvestmentDTO);

        assertThat(createdInvestmentDTO.getId(), is(equalTo(expectedInvestmentDTO.getId())));
        assertThat(createdInvestmentDTO.getName(), is(equalTo(expectedInvestmentDTO.getName())));
        assertThat(createdInvestmentDTO.getValue(), is(equalTo(expectedInvestmentDTO.getValue())));
        assertThat(createdInvestmentDTO.getInitialDate(), is(lessThan(createdInvestmentDTO.getExpirationDate())));
    }

    @Test
    void whenAlreadyRegisteredInvestmentInformedThanAnExceptionShouldBeThrown() {
        // given
        InvestmentDTO expectedInvestmentDTO = InvestmentDTOBuilder.builder().build().toInvestmentDTO();
        Investment duplicatedInvestment = investmentMapper.toModel(expectedInvestmentDTO);

        // when
        when(investmentRepository.findByName(expectedInvestmentDTO.getName())).thenReturn(Optional.of(duplicatedInvestment));

        // then
        assertThrows(InvestmentAlreadyRegisteredException.class, () -> investmentService.createInvestment(expectedInvestmentDTO));
    }

    @Test
    void whenInvestmentWithInitialDateGreaterThanExpirationDateInformedThanExceptionShouldBeThrown() {
        // given
        InvestmentDTO expectedInvestmentDTO = InvestmentDTOBuilder.builder()
                .initialDate(LocalDate.of(2000, 1, 1))
                .expirationDate(LocalDate.of(1970, 1, 1))
                .build().toInvestmentDTO();

        // then
        assertThrows(InvestmentInvalidExpirationDateException.class, () -> investmentService.createInvestment(expectedInvestmentDTO));
    }

    @Test
    void whenValidInvestmentNameIsGivenThenReturnAnInvestment() throws InvestmentNotFoundException {
        // given
        InvestmentDTO expectedFoundInvestmentDTO = InvestmentDTOBuilder.builder().build().toInvestmentDTO();
        Investment expectedFoundInvestment = investmentMapper.toModel(expectedFoundInvestmentDTO);

        // when
        when(investmentRepository.findByName(expectedFoundInvestment.getName())).thenReturn(Optional.of(expectedFoundInvestment));

        // then
        InvestmentDTO foundInvestmentDTO = investmentService.findByName(expectedFoundInvestmentDTO.getName());

        assertThat(foundInvestmentDTO, is(equalTo(expectedFoundInvestmentDTO)));
    }

    @Test
    void whenNoRegisteredInvestmentNameIsGivenThenThrowAnException() {
        // given
        InvestmentDTO expectedFoundInvestmentDTO = InvestmentDTOBuilder.builder().build().toInvestmentDTO();

        // when
        when(investmentRepository.findByName(expectedFoundInvestmentDTO.getName())).thenReturn(Optional.empty());

        // then
        assertThrows(InvestmentNotFoundException.class, () -> investmentService.findByName(expectedFoundInvestmentDTO.getName()));
    }

    @Test
    void whenListInvestmentIsCalledThenReturnAListOfInvestments() {
        // given
        InvestmentDTO expectedInvestmentDTO = InvestmentDTOBuilder.builder().build().toInvestmentDTO();
        Investment expectedFoundInvestment = investmentMapper.toModel(expectedInvestmentDTO);

        // when
        when(investmentRepository.findAll()).thenReturn(Collections.singletonList(expectedFoundInvestment));

        // then
        List<InvestmentDTO> foundInvestmentDTO = investmentService.listAll();

        assertThat(foundInvestmentDTO, is(not(empty())));
        assertThat(foundInvestmentDTO.get(0), is(equalTo(expectedInvestmentDTO)));
    }

    @Test
    void whenListInvestmentIsCalledThenReturnAnEmptyListOfInvestments() {
        // when
        when(investmentRepository.findAll()).thenReturn(emptyList());

        // then
        List<InvestmentDTO> foundInvestmentDTO = investmentService.listAll();

        assertThat(foundInvestmentDTO, is(empty()));
    }
}