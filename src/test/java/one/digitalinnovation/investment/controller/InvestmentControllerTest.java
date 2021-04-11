package one.digitalinnovation.investment.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import one.digitalinnovation.investment.builder.InvestmentDTOBuilder;
import one.digitalinnovation.investment.dto.AmountDTO;
import one.digitalinnovation.investment.dto.InvestmentDTO;
import one.digitalinnovation.investment.entity.Investment;
import one.digitalinnovation.investment.exception.InsufficientBalanceForWithdrawalException;
import one.digitalinnovation.investment.exception.InvestmentNotFoundException;
import one.digitalinnovation.investment.service.InvestmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static one.digitalinnovation.investment.utils.JsonConversionUtils.asJsonString;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class InvestmentControllerTest {

    private static final String INVESTMENT_API_URL_PATH = "/api/v1/investments";
    private static final long VALID_INVESTMENT_ID = 1L;
    private static final long INVALID_INVESTMENT_ID = 2L;
    private static final String INVESTMENT_API_SUBPATH_APPLICATION_URL = "/apply";
    private static final String INVESTMENT_API_SUBPATH_WITHDRAWAL_URL = "/withdraw";


    private MockMvc mockMvc;

    @Mock
    private InvestmentService investmentService;

    @InjectMocks
    private InvestmentController investmentController;

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.registerModules(new JavaTimeModule());

        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new
                MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter.setObjectMapper(objectMapper);

        mockMvc = MockMvcBuilders.standaloneSetup(investmentController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setMessageConverters(mappingJackson2HttpMessageConverter)
                .build();
    }

    // create
    @Test
    void whenPOSTIsCalledThenAnInvestmentIsCreated() throws Exception {
        // given
        InvestmentDTO investmentDTO = InvestmentDTOBuilder.builder().build().toInvestmentDTO();

        // when
        when(investmentService.createInvestment(investmentDTO)).thenReturn(investmentDTO);

        // then
        mockMvc.perform(post(INVESTMENT_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(investmentDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(investmentDTO.getName())))
                .andExpect(jsonPath("$.initialDate", is(investmentDTO.getInitialDate().toString())))
                .andExpect(jsonPath("$.expirationDate", is(investmentDTO.getExpirationDate().toString())));
    }

    @Test
    void whenPOSTIsCalledWithoutRequiredFieldThanAnErrorIsReturned() throws Exception {
        // given
        InvestmentDTO investmentDTO = InvestmentDTOBuilder.builder().build().toInvestmentDTO();
        investmentDTO.setName(null);

        // then
        mockMvc.perform(post(INVESTMENT_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(investmentDTO)))
                .andExpect(status().isBadRequest());
    }

    // get
    @Test
    void whenGETIsCalledWithValidNameThenOkStatusIsReturned() throws Exception {
        // given
        InvestmentDTO investmentDTO = InvestmentDTOBuilder.builder().build().toInvestmentDTO();

        // when
        when(investmentService.findByName(investmentDTO.getName())).thenReturn(investmentDTO);

        // then
        String endPoint = INVESTMENT_API_URL_PATH + "/" + investmentDTO.getName();
        mockMvc.perform(get(endPoint).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(investmentDTO.getName())))
                .andExpect(jsonPath("$.initialDate", is(investmentDTO.getInitialDate().toString())))
                .andExpect(jsonPath("$.expirationDate", is(investmentDTO.getExpirationDate().toString())));

    }

    @Test
    void whenGETIsCalledWithNoRegisteredNameThenNotFoundStatusIsReturned() throws Exception {
        // given
        InvestmentDTO investmentDTO = InvestmentDTOBuilder.builder().build().toInvestmentDTO();

        // when
        when(investmentService.findByName(investmentDTO.getName())).thenThrow(InvestmentNotFoundException.class);

        // then
        String endPoint = INVESTMENT_API_URL_PATH + "/" + investmentDTO.getName();
        mockMvc.perform(get(endPoint).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    // list
    @Test
    void whenGETListWithInvestmentsIsCalledThenOkStatusIsReturned() throws Exception {
        // given
        InvestmentDTO investmentDTO = InvestmentDTOBuilder.builder().build().toInvestmentDTO();

        // when
        when(investmentService.listAll()).thenReturn(Collections.singletonList(investmentDTO));

        // then
        mockMvc.perform(get(INVESTMENT_API_URL_PATH).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(investmentDTO.getName())))
                .andExpect(jsonPath("$[0].initialDate", is(investmentDTO.getInitialDate().toString())))
                .andExpect(jsonPath("$[0].expirationDate", is(investmentDTO.getExpirationDate().toString())));

    }

    // delete
    @Test
    void whenDELETEIsCalledWithValidIdThenNoContentStatusIsReturned() throws Exception {
        // when
        doNothing().when(investmentService).deleteById(VALID_INVESTMENT_ID);

        // then
        String endPoint = INVESTMENT_API_URL_PATH + "/" + VALID_INVESTMENT_ID;
        mockMvc.perform(delete(endPoint).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

    }

    @Test
    void whenDELETEIsCalledWithInvalidIdThenNotFoundStatusIsReturned() throws Exception {
        // when
        doThrow(InvestmentNotFoundException.class).when(investmentService).deleteById(INVALID_INVESTMENT_ID);

        // then
        String endPoint = INVESTMENT_API_URL_PATH + "/" + INVALID_INVESTMENT_ID;
        mockMvc.perform(delete(endPoint).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    // apply
    @Test
    void whenPATCHIsCalledToApplyInvestmentThenOkStatusIsReturned() throws Exception {
        // given
        AmountDTO amountDTO = AmountDTO.builder().amount(250D).build();

        InvestmentDTO investmentDTO = InvestmentDTOBuilder.builder().build().toInvestmentDTO();

        // when
        when(investmentService.apply(VALID_INVESTMENT_ID, amountDTO.getAmount())).thenReturn(investmentDTO);

        // then
        String endPoint = INVESTMENT_API_URL_PATH + "/" + VALID_INVESTMENT_ID + INVESTMENT_API_SUBPATH_APPLICATION_URL;
        mockMvc.perform(patch(endPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(amountDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void whenPATCHisCalledWithInvalidInvestmentIdThenNotFoundStatusIsReturned() throws Exception {
        // given
        AmountDTO amountDTO = AmountDTO.builder().amount(250D).build();

        // when
        when(investmentService.apply(INVALID_INVESTMENT_ID, amountDTO.getAmount())).thenThrow(InvestmentNotFoundException.class);

        // then
        String endPoint = INVESTMENT_API_URL_PATH + "/" + INVALID_INVESTMENT_ID + INVESTMENT_API_SUBPATH_APPLICATION_URL;
        mockMvc.perform(patch(endPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(amountDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenPATCHisCalledWithAmountLessThanZeroThenReturnError() throws Exception {
        // given
        AmountDTO amountDTO = AmountDTO.builder().amount(-1D).build();

        // then
        String endPoint = INVESTMENT_API_URL_PATH + "/" + VALID_INVESTMENT_ID + INVESTMENT_API_SUBPATH_APPLICATION_URL;
        mockMvc.perform(patch(endPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(amountDTO)))
                .andExpect(status().isBadRequest());
    }

    // withdraw
    @Test
    void whenPATCHIsCalledToWithdrawThenOkStatusIsReturned() throws Exception {
        // given
        AmountDTO amountDTO = AmountDTO.builder().amount(250D).build();
        InvestmentDTO investmentDTO = InvestmentDTOBuilder.builder().build().toInvestmentDTO();

        // when
        when(investmentService.withdraw(investmentDTO.getId(), amountDTO.getAmount())).thenReturn(investmentDTO);

        // then
        String endPoint = INVESTMENT_API_URL_PATH + "/" + investmentDTO.getId() + INVESTMENT_API_SUBPATH_WITHDRAWAL_URL;
        mockMvc.perform(patch(endPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(amountDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void whenPATCHIsCalledWithInvalidInvestmentIdThenNotFoundStatusIsReturned() throws Exception {
        // given
        AmountDTO amountDTO = AmountDTO.builder().amount(250D).build();

        // when
        when(investmentService.withdraw(INVALID_INVESTMENT_ID, amountDTO.getAmount())).thenThrow(InvestmentNotFoundException.class);

        // then
        String endPoint = INVESTMENT_API_URL_PATH + "/" + INVALID_INVESTMENT_ID + INVESTMENT_API_SUBPATH_WITHDRAWAL_URL;
        mockMvc.perform(patch(endPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(amountDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenPATCHIsCalledWithAmountLessThanZeroThenReturnError() throws Exception {
        // given
        AmountDTO amountDTO = AmountDTO.builder().amount(-1D).build();

        // then
        String endPoint = INVESTMENT_API_URL_PATH + "/" + VALID_INVESTMENT_ID + INVESTMENT_API_SUBPATH_WITHDRAWAL_URL;
        mockMvc.perform(patch(endPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(amountDTO)))
                .andExpect(status().isBadRequest());
    }
}