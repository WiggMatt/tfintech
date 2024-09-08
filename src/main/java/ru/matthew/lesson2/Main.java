package ru.matthew.lesson2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        CityParser cityParser = new CityParser();

        logger.info("Тестирование обработки файла с ошибками");
        City cityError = cityParser.parseCityJson("city-error.json");

        if (cityError == null) {
            logger.info("Обработка файла с ошибками завершена. Объект City не создан.");
        } else {
            logger.info("Объект City успешно создан из файла с ошибками. Это не должно произойти.");
        }

        logger.info("Тестирование обработки корректного JSON файла");
        City city = cityParser.parseCityJson("city.json");

        if (city != null) {
            String xmlData = cityParser.toXML(city);
            if (xmlData != null) {
                cityParser.saveXMLToFile(xmlData, "city.xml");
            }
        }
    }
}

