package ru.matthew.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyConversionResponse {
    private String fromCurrency;
    private String toCurrency;
    private double convertedAmount;
}
