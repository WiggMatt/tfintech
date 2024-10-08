package ru.matthew.dto;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Hidden
@Schema(description = "Сущность ответа на конвертацию валюты")
public class CurrencyConversionResponse {
    @Schema(description = "Из какой валюты происходила конвертация")
    private String fromCurrency;
    @Schema(description = "В какую валюту происходила конвертация")
    private String toCurrency;
    @Schema(description = "Сконвертированное значение")
    private double convertedAmount;
}
