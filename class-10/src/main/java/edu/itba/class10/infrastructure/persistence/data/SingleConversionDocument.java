package edu.itba.class10.infrastructure.persistence.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "single_conversions")
public class SingleConversionDocument {
	@MongoId
	private String id;
	@Indexed
	private String date;
	private String baseCurrency;
	private String targetCurrency;
	private double originalAmount;
	private double convertedAmount;
}
