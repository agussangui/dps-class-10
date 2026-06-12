package edu.itba.class10.domain.usecases.exchangerate;

import edu.itba.class10.domain.persistence.SingleConversionEntity;

import java.util.List;

public interface GetAllConversions {

    List<SingleConversionEntity> get();
}
