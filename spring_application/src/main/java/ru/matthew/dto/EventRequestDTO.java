package ru.matthew.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestDTO {
    @NotNull(message = "Параметр budget обязательный и не может быть пустым")
    @Positive(message = "Параметр budget не может быть меньше 0")
    private double budget;
    @NotBlank(message = "Параметр currency обязательный и не может быть пустым")
    private String currency;
    private String dateFrom;
    private String dateTo;
}
