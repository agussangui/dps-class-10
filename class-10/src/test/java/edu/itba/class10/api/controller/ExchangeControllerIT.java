package edu.itba.class10.api.controller;

import edu.itba.class10.IntegrationTest;
import edu.itba.class10.boot.Application;
import edu.itba.class10.domain.entity.money.Currency;
import edu.itba.class10.domain.entity.money.MoneyAmount;
import edu.itba.class10.domain.persistence.SingleConversionEntity;
import edu.itba.class10.domain.usecases.exchangerate.CurrencyConverter;
import edu.itba.class10.domain.usecases.exchangerate.GetAllConversions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ExchangeController.class)
@ContextConfiguration(classes = Application.class)
class ExchangeControllerIT extends IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CurrencyConverter currencyConverter;

    @MockitoBean
    private GetAllConversions getAllConversions;

    @Test
    void whenConvert_thenControllerReturnsSavedConversions() throws Exception {
        // Given
        final var from = MoneyAmount.create(Currency.EUR, BigDecimal.valueOf(100));
        final var to = MoneyAmount.create(Currency.USD, BigDecimal.valueOf(102));
        final var entity = new SingleConversionEntity(LocalDate.now(), from, to);

        when(this.getAllConversions.get()).thenReturn(List.of(entity));

        // When & Then
        this.mockMvc.perform(get("/v1/exchange/conversion")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].from.currency", is("EUR")))
                .andExpect(jsonPath("$[0].from.amount", is(100.0)))
                .andExpect(jsonPath("$[0].to.currency", is("USD")))
                .andExpect(jsonPath("$[0].to.amount", is(102.0)));
    }
}