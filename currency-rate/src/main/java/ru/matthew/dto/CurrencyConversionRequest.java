package ru.matthew.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Сущность запроса на конвертацию валюты")
public class CurrencyConversionRequest {
    @NotNull(message = "From currency is required")
    @NotEmpty(message = "From currency cannot be empty or null")
    @Schema(description = "Из какой валюты конвертировать")
    private String fromCurrency;
    @NotNull(message = "To currency is required")
    @NotEmpty(message = "To currency cannot be empty or null")
    @Schema(description = "В какую валюту конвертировать")
    private String toCurrency;
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than zero")
    @Schema(description = "Сумма к конвертации")
    private Double amount;
}
