package edu.itba.class10.domain.persistence;

import java.util.List;

public interface ExchangePersistence {
	void save(SingleConversionEntity entity);

	List<SingleConversionEntity> findAll();
}
