package edu.itba.class10.api.controller;

import edu.itba.class10.IntegrationTest;
import edu.itba.class10.boot.Application;
import edu.itba.class10.infrastructure.persistence.relational.SingleConversionJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
@ActiveProfiles("sql")
class ExchangeControllerIT extends IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SingleConversionJpaRepository singleConversionJpaRepository;

    @BeforeEach
    void clearDatabase() {
        this.singleConversionJpaRepository.deleteAll();
    }

    @Test
    void whenConvert_thenControllerReturnsSavedConversions() throws Exception {
        // Given
        final var requestJson = """
                {
                  "from": "EUR",
                  "to": "USD",
                  "amount": 100.0
                }
                """;

        // When & Then - Convert (E2E flow: calls WireMock and saves to H2)
        this.mockMvc.perform(post("/v1/exchange/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currency", is("USD")))
                .andExpect(jsonPath("$.amount", is(10200.0)));

        // When & Then - Retrieve saved conversions from H2 database
        this.mockMvc.perform(get("/v1/exchange/conversion")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].from.currency", is("EUR")))
                .andExpect(jsonPath("$[0].from.amount", is(100.0)))
                .andExpect(jsonPath("$[0].to.currency", is("USD")))
                .andExpect(jsonPath("$[0].to.amount", is(10200.0)));
    }
}