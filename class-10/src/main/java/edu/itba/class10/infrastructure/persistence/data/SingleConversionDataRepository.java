package edu.itba.class10.infrastructure.persistence.data;

import edu.itba.class10.domain.persistence.ExchangePersistence;
import edu.itba.class10.domain.persistence.SingleConversionEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
@Profile("nosql")
public class SingleConversionDataRepository implements ExchangePersistence {

	private final SingleConversionDataMapper singleConversionDataMapper;
	private final SingleConversionMongoRepository singleConversionMongoRepository;

	@Override
	public void save(final SingleConversionEntity entity) {
		this.singleConversionMongoRepository.save(this.singleConversionDataMapper.toDocument(entity));
	}

	@Override
	public List<SingleConversionEntity> findAll() {
		return this.singleConversionMongoRepository.findAll().stream()
				.map(this.singleConversionDataMapper::toEntity)
				.toList();
	}
}

