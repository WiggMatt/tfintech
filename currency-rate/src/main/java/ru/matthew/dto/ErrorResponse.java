package ru.matthew.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Ответ на ошибку")
public class ErrorResponse {
    @Schema(description = "Код ошибки")
    private String error;
    @Schema(description = "Сообщение об ошибке")
    private String message;
}
