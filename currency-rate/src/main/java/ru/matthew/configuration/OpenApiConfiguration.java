package ru.matthew.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Currency Rate API",
                description = "API for currency conversion", version = "1.0.0",
                contact = @Contact(
                        name = "Konyukhov Matvey",
                        email = "konyukhov.matt@yandex.ru"
                )
        )
)
public class OpenApiConfiguration {
}
