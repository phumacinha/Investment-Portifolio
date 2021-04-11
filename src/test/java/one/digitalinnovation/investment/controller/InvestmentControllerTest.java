package one.digitalinnovation.investment.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import one.digitalinnovation.investment.builder.InvestmentDTOBuilder;
import one.digitalinnovation.investment.dto.InvestmentDTO;
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

import static one.digitalinnovation.investment.utils.JsonConvertionUtils.asJsonString;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class InvestmentControllerTest {

    private static final String INVESTMENT_API_URL_PATH = "/api/v1/investments";
    private static final long VALID_INVESTMENT_ID = 1L;
    private static final long INVALID_INVESTMENT_ID = 2L;

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
}