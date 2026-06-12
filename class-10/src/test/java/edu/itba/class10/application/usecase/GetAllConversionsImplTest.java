package edu.itba.class10.application.usecase;

import edu.itba.class10.domain.persistence.ExchangePersistence;
import edu.itba.class10.domain.persistence.SingleConversionEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetAllConversionsImplTest {

    @Mock
    private ExchangePersistence exchangePersistence;

    @Test
    void shouldReturnAllFromPersistence() {
        // Given
        final var sample = new SingleConversionEntity(LocalDate.now(), null, null);
        when(this.exchangePersistence.findAll()).thenReturn(List.of(sample));

        final var impl = new GetAllConversionsImpl(this.exchangePersistence);

        // When
        final var result = impl.get();

        // Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(sample, result.getFirst());
    }
}

