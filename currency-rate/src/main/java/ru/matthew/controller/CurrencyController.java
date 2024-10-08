package ru.matthew.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.matthew.dto.CurrencyConversionRequest;
import ru.matthew.dto.CurrencyConversionResponse;
import ru.matthew.dto.CurrencyRateResponse;
import ru.matthew.dto.ErrorResponse;
import ru.matthew.service.CurrencyService;

@Tag(name = "CurrencyController", description = "Контроллер для работы с валютами")
@RestController
@RequestMapping("/currencies")
@Slf4j
public class CurrencyController {
    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @Operation(summary = "Получить курс валюты", description = "Возвращает курс для указанного кода валюты")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Конвертация успешна",
                    content = @Content(mediaType = "application/json", schema =
                    @Schema(implementation = CurrencyRateResponse.class), examples = @ExampleObject(value = """
                                {
                                    "code": "USD",
                                    "rate": 94.78
                                }
                            """))),
            @ApiResponse(responseCode = "400", description = "Неподдерживаемый код валюты",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "Пример ошибки 400",
                                    value = "{\n  \"error\": \"Bad Request\",\n  \"message\": \"Unsupported currency code: EUR\"\n}"
                            ))),
            @ApiResponse(responseCode = "404", description = "Курс валюты не найден",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "Пример ошибки 404",
                                    value = "{\n  \"error\": \"Not Found\",\n  \"message\": \"Currency rate not found for code: GBP\"\n}"
                            ))),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "Пример ошибки 500",
                                    value = "{\n  \"error\": \"Internal Server Error\",\n  \"message\": \"Unexpected error occurred\"\n}"
                            )))
    })
    @GetMapping("/rates/{code}")
    public CurrencyRateResponse getCurrencyRate(@PathVariable String code) {
        log.info("Получен запрос на получение курса валюты для кода: {}", code);
        return currencyService.searchCurrencyRate(code);
    }

    @Operation(summary = "Конвертировать валюту", description = "Конвертирует сумму из одной валюты в другую")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Конвертация успешна",
                    content = @Content(mediaType = "application/json", schema =
                    @Schema(implementation = CurrencyConversionResponse.class), examples = @ExampleObject(value = """
                                {
                                    "fromCurrency": "USD",
                                    "toCurrency": "RUB",
                                    "convertedAmount": 7500.0
                                }
                            """))),
            @ApiResponse(responseCode = "400", description = "Неподдерживаемый код валюты",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "Пример ошибки 400",
                                    value = "{\n  \"error\": \"Bad Request\",\n  \"message\": \"Unsupported currency code: EUR\"\n}"
                            ))),
            @ApiResponse(responseCode = "404", description = "Курс валюты не найден",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "Пример ошибки 404",
                                    value = "{\n  \"error\": \"Not Found\",\n  \"message\": \"Currency rate not found for code: GBP\"\n}"
                            ))),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "Пример ошибки 500",
                                    value = "{\n  \"error\": \"Internal Server Error\",\n  \"message\": \"Unexpected error occurred\"\n}"
                            )))
    })
    @PostMapping("/convert")
    public CurrencyConversionResponse convertCurrency(@Valid @RequestBody CurrencyConversionRequest request) {
        log.info("Получен запрос на конвертацию валюты: {} из {} в {}",
                request.getAmount(),
                request.getFromCurrency(),
                request.getToCurrency());
        double convertedAmount = currencyService.convertCurrency(request.getFromCurrency(),
                request.getToCurrency(),
                request.getAmount());
        return new CurrencyConversionResponse(request.getFromCurrency(), request.getToCurrency(), convertedAmount);
    }
}
