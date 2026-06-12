package edu.itba.class10.api.controller;

import edu.itba.class10.converter.api.domain.Currency;
import edu.itba.class10.converter.api.domain.SingleConversionRecord;
import edu.itba.class10.converter.api.domain.SingleConversionRequest;
import edu.itba.class10.domain.entity.money.MoneyAmount;
import edu.itba.class10.domain.persistence.SingleConversionEntity;
import edu.itba.class10.domain.usecases.exchangerate.CurrencyConverter;
import edu.itba.class10.domain.usecases.exchangerate.GetAllConversions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExchangeControllerTest {

	@Mock
	private CurrencyConverter currencyConverter;
	@Mock
	private GetAllConversions getAllConversions;


	@Test
	void testSingleRequest() {
		// Given
		final var requestedCurrency = edu.itba.class10.domain.entity.money.Currency.EUR;
		final var expectedAmount = MoneyAmount.create(requestedCurrency, BigDecimal.valueOf(87.0));
		final var requestedAmount = MoneyAmount.create(edu.itba.class10.domain.entity.money.Currency.USD,
				BigDecimal.valueOf(100));

		final var request = new SingleConversionRequest().to(Currency.EUR).from(Currency.USD).amount(100.0);
		final var controller = new ExchangeController(this.currencyConverter,this.getAllConversions);

		when(this.currencyConverter.convert(requestedAmount, requestedCurrency)).thenReturn(expectedAmount);

		// When
		final var result = controller.convertToSingleCurrency(request);

		// Then
		Assertions.assertEquals(expectedAmount.amount().doubleValue(), result.getBody().getAmount());
		Assertions.assertEquals(expectedAmount.currency().name(), result.getBody().getCurrency().name());
	}



	@Test
	void testGetAllConversions() {
		// Given
		final var date = LocalDate.now();
		final var from = MoneyAmount.create(edu.itba.class10.domain.entity.money.Currency.EUR, BigDecimal.valueOf(100));
		final var to = MoneyAmount.create(edu.itba.class10.domain.entity.money.Currency.USD, BigDecimal.valueOf(102));
		final var entity = new SingleConversionEntity(date, from, to);

		when(this.getAllConversions.get()).thenReturn(List.of(entity));

		final var controller = new ExchangeController(this.currencyConverter, this.getAllConversions);

		// When
		final ResponseEntity<List<SingleConversionRecord>> response = controller.getAllConversions();

		// Then
		Assertions.assertEquals(200, response.getStatusCodeValue());
		Assertions.assertNotNull(response.getBody());
		Assertions.assertEquals(1, response.getBody().size());
		final var record = response.getBody().get(0);
		Assertions.assertEquals(from.amount().doubleValue(), record.getFrom().getAmount());
		Assertions.assertEquals(from.currency().name(), record.getFrom().getCurrency().name());
		Assertions.assertEquals(to.amount().doubleValue(), record.getTo().getAmount());
		Assertions.assertEquals(to.currency().name(), record.getTo().getCurrency().name());
	}
}
