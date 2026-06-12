package edu.itba.class10.api.controller;

import edu.itba.class10.ExchangeApi;
import edu.itba.class10.api.controller.mapper.SingleConversionMapper;
import edu.itba.class10.converter.api.domain.ConvertedAmount;
import edu.itba.class10.converter.api.domain.SingleConversionRecord;
import edu.itba.class10.converter.api.domain.SingleConversionRequest;
import edu.itba.class10.domain.persistence.SingleConversionEntity;
import edu.itba.class10.domain.usecases.exchangerate.CurrencyConverter;
import edu.itba.class10.domain.usecases.exchangerate.GetAllConversions;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ExchangeController implements ExchangeApi {

	private final CurrencyConverter currencyConverter;
	private final SingleConversionMapper singleConversionMapper = new SingleConversionMapper();
	private final GetAllConversions getAllConversions;

	@Override
	@RequestMapping("v1/exchange/convert")
	public ResponseEntity<ConvertedAmount> convertToSingleCurrency(final SingleConversionRequest request) {
		final var moneyAmount = this.singleConversionMapper.toMoneyAmount(request);
		final var requestedCurrency = this.singleConversionMapper.toCurrency(request);
		final var result = this.currencyConverter.convert(moneyAmount, requestedCurrency);
		final var convertedAmount = this.singleConversionMapper.toConvertedAmount(result);
		return ResponseEntity.ok(convertedAmount);
	}


	@Override
	@RequestMapping("v1/exchange/conversion")
	public ResponseEntity<List<SingleConversionRecord>> getAllConversions() {
		final var conversions = this.getAllConversions.get();
		final var records = conversions.stream().map(this.singleConversionMapper::toSingleConversionRecord).toList();
		return ResponseEntity.ok(records);
	}

}
