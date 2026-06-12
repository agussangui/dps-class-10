package edu.itba.class10.boot.persistence;

import edu.itba.class10.boot.config.NoSqlConfig;
import edu.itba.class10.domain.entity.money.Currency;
import edu.itba.class10.domain.entity.money.MoneyAmount;
import edu.itba.class10.domain.persistence.SingleConversionEntity;
import edu.itba.class10.infrastructure.persistence.data.SingleConversionDataMapper;
import edu.itba.class10.infrastructure.persistence.data.SingleConversionDataRepository;
import edu.itba.class10.infrastructure.persistence.data.SingleConversionMongoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@DataMongoTest
@ActiveProfiles("nosql")
@Import({SingleConversionDataRepository.class, SingleConversionDataMapper.class, NoSqlConfig.class})
class NoSqlConversionRepositoryTest {

	@Autowired
	private SingleConversionDataRepository singleConversionDataRepository;

	@Autowired
	private SingleConversionMongoRepository singleConversionMongoRepository;

	@BeforeEach
	void setUp() {
		this.singleConversionMongoRepository.deleteAll();
	}

	@Test
	void savesConversionEntity() {
		// Given
		final var fromAmount = MoneyAmount.create(Currency.USD, BigDecimal.valueOf(100));
		final var toAmount = MoneyAmount.create(Currency.EUR, BigDecimal.valueOf(90));
		final var singleConversionEntity = new SingleConversionEntity(LocalDate.of(2025, 10, 28), fromAmount, toAmount);

		// When
		this.singleConversionDataRepository.save(singleConversionEntity);

		// Then
		final var persisted = this.singleConversionDataRepository.findAll();
		assertThat(persisted.size(), is(1));
		assertThat(persisted.get(0).date(), is(LocalDate.of(2025, 10, 28)));
		assertThat(persisted.get(0).from().currency(), is(Currency.USD));
		assertThat(persisted.get(0).from().amount().doubleValue(), is(100.0));
		assertThat(persisted.get(0).to().currency(), is(Currency.EUR));
		assertThat(persisted.get(0).to().amount().doubleValue(), is(90.0));
	}

	@Test
	void retrievesAllConversions() {
		// Given
		final var fromAmount1 = MoneyAmount.create(Currency.USD, BigDecimal.valueOf(100));
		final var toAmount1 = MoneyAmount.create(Currency.EUR, BigDecimal.valueOf(90));
		final var singleConversionEntity1 = new SingleConversionEntity(LocalDate.of(2025, 10, 28), fromAmount1, toAmount1);
		this.singleConversionDataRepository.save(singleConversionEntity1);

		final var fromAmount2 = MoneyAmount.create(Currency.EUR, BigDecimal.valueOf(200));
		final var toAmount2 = MoneyAmount.create(Currency.USD, BigDecimal.valueOf(220));
		final var singleConversionEntity2 = new SingleConversionEntity(LocalDate.of(2025, 10, 29), fromAmount2, toAmount2);
		this.singleConversionDataRepository.save(singleConversionEntity2);

		// When
		final var allConversions = this.singleConversionDataRepository.findAll();

		// Then
		assertThat(allConversions.size(), is(2));
		assertThat(allConversions.get(0).date(), is(LocalDate.of(2025, 10, 28)));
		assertThat(allConversions.get(0).from().currency(), is(Currency.USD));
		assertThat(allConversions.get(1).date(), is(LocalDate.of(2025, 10, 29)));
		assertThat(allConversions.get(1).from().currency(), is(Currency.EUR));
	}
}