import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.matthew.CurrencyRateApplication;
import ru.matthew.dto.CurrencyConversionRequest;
import ru.matthew.exception.CurrencyNotFoundException;
import ru.matthew.exception.ServiceUnavailableException;
import ru.matthew.exception.UnsupportedCurrencyException;
import ru.matthew.service.CurrencyRateCacheService;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = CurrencyRateApplication.class)
@AutoConfigureMockMvc
public class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurrencyRateCacheService currencyRateCacheService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // Проверка валидации для эндпоинта /currencies/convert
    @ParameterizedTest
    @MethodSource("provideInvalidCurrencyRequests")
    void convertCurrency_WhenInvalidRequest_ThrowsBadRequest(CurrencyConversionRequest request) throws Exception {
        // Act & Assert
        mockMvc.perform(post("/currencies/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    private static Stream<CurrencyConversionRequest> provideInvalidCurrencyRequests() {
        return Stream.of(
                createRequest(-10d, "USD", "EUR"),
                createRequest(100d, "USD", ""),
                createRequest(100d, null, "USD")
        );
    }

    private static CurrencyConversionRequest createRequest(double amount, String fromCurrency, String toCurrency) {
        CurrencyConversionRequest request = new CurrencyConversionRequest();
        request.setAmount(amount);
        request.setFromCurrency(fromCurrency);
        request.setToCurrency(toCurrency);
        return request;
    }


    @Test
    void convertCurrency_WhenExternalServiceUnavailable_ThrowsServiceUnavailableException() throws Exception {
        // Arrange
        CurrencyConversionRequest request = new CurrencyConversionRequest();
        request.setAmount(100d);
        request.setFromCurrency("USD");
        request.setToCurrency("RUB");

        when(currencyRateCacheService.getCurrencyRates()).thenThrow(new ServiceUnavailableException("Currency service is unavailable", 3600));

        // Act & Assert
        mockMvc.perform(post("/currencies/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isServiceUnavailable())
                .andExpect(header().string("Retry-After", "3600"))
                .andExpect(jsonPath("$.message").value("Currency service is unavailable"));
    }

    // Проверка правильности конвертации для эндпоинта /currencies/convert
    @Test
    void convertCurrency_WhenValidRequest_ReturnsConvertedAmount() throws Exception {
        // Arrange
        CurrencyConversionRequest request = new CurrencyConversionRequest();
        request.setAmount(100d);
        request.setFromCurrency("USD");
        request.setToCurrency("RUB");

        Map<String, Double> rates = new HashMap<>();
        rates.put("USD", 94.0);

        when(currencyRateCacheService.getCurrencyRates()).thenReturn(rates);

        // Act
        mockMvc.perform(post("/currencies/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fromCurrency").value("USD"))
                .andExpect(jsonPath("$.toCurrency").value("RUB"))
                .andExpect(jsonPath("$.convertedAmount").value(9400.0));
    }


    // Проверка валидации для эндпоинта /currencies/rates/{code}
    @ParameterizedTest
    @MethodSource("provideCurrencyCodesForValidation")
    void getCurrencyRate_WhenInvalidCode_ThrowsExpectedException(String currencyCode, Exception expectedException, String expectedMessage) throws Exception {
        // Arrange
        when(currencyRateCacheService.getCurrencyRates()).thenThrow(expectedException);

        // Act & Assert
        mockMvc.perform(get("/currencies/rates/{code}", currencyCode))
                .andExpect(status().is(expectedException instanceof CurrencyNotFoundException ? 404 : 400))
                .andExpect(jsonPath("$.message").value(expectedMessage));
    }

    private static Stream<Object[]> provideCurrencyCodesForValidation() {
        return Stream.of(
                new Object[]{"INVALID_CODE", new UnsupportedCurrencyException("Unsupported currency code"), "Unsupported currency code: INVALID_CODE"},
                new Object[]{"XXX", new CurrencyNotFoundException("Currency rate not found for code: XXX"), "Currency rate not found for code: XXX"}
        );
    }
}
