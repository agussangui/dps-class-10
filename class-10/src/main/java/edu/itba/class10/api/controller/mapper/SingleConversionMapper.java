package edu.itba.class10.api.controller.mapper;

import edu.itba.class10.converter.api.domain.ConvertedAmount;
import edu.itba.class10.converter.api.domain.SingleConversionRecord;
import edu.itba.class10.converter.api.domain.SingleConversionRequest;
import edu.itba.class10.domain.entity.money.Currency;
import edu.itba.class10.domain.entity.money.MoneyAmount;
import edu.itba.class10.domain.persistence.SingleConversionEntity;

import java.math.BigDecimal;

public class SingleConversionMapper {
	public MoneyAmount toMoneyAmount(final SingleConversionRequest request) {
		return MoneyAmount.create(Currency.valueOf(request.getFrom().name()), BigDecimal.valueOf(request.getAmount()));
	}

	public Currency toCurrency(final SingleConversionRequest request) {
		return Currency.valueOf(request.getTo().name());
	}

	public ConvertedAmount toConvertedAmount(final MoneyAmount result) {
		return new ConvertedAmount()
				.currency(edu.itba.class10.converter.api.domain.Currency.valueOf(result.currency().name()))
				.amount(result.amount().doubleValue());
	}

	public SingleConversionRecord toSingleConversionRecord(final SingleConversionEntity entity) {
		final var record = new SingleConversionRecord();
		record.setDate(entity.date());
		record.setFrom(this.toConvertedAmount(entity.from()));
		record.setTo(this.toConvertedAmount(entity.to()));
		return record;
	}
}
