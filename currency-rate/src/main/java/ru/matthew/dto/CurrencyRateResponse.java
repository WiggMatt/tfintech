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
@Schema(description = "Сущность запроса на получение значения валюты")
public class CurrencyRateResponse {
    @Schema(description = "Строковый код валюты")
    private String code;
    @Schema(description = "Значение валюты")
    private double rate;
}
