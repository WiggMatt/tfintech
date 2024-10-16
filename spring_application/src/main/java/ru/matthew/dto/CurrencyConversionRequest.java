package ru.matthew.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyConversionRequest {
    @NotNull(message = "From currency is required")
    @NotEmpty(message = "From currency cannot be empty or null")
    private String fromCurrency;
    @NotNull(message = "To currency is required")
    @NotEmpty(message = "To currency cannot be empty or null")
    private String toCurrency;
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than zero")
    private Double amount;
}
