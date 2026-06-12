package edu.itba.class10.application.usecase;

import edu.itba.class10.domain.persistence.ExchangePersistence;
import edu.itba.class10.domain.persistence.SingleConversionEntity;
import edu.itba.class10.domain.usecases.exchangerate.GetAllConversions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllConversionsImpl implements GetAllConversions {
    private final ExchangePersistence exchangePersistence;


    @Override
    public List<SingleConversionEntity> get() {
        return exchangePersistence.findAll();
    }
}
