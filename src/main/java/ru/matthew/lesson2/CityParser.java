package ru.matthew.lesson2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CityParser {

    private static final Logger logger = LoggerFactory.getLogger(CityParser.class);

    public City parseCityJson(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        logger.debug("Инициализация ObjectMapper для чтения JSON файла");

        try {
            logger.info("Чтение JSON из файла: {}", filePath);
            City city = objectMapper.readValue(new File(filePath), City.class);
            logger.debug("Полученный объект City: {}", city);
            return city;
        } catch (JsonProcessingException e) {
            logger.error("Ошибка парсинга JSON в файле {}: {}", filePath, e.getMessage());
            logger.warn("Проверьте формат JSON в файле {}", filePath);
        } catch (IOException e) {
            logger.error("Ошибка чтения файла {}: {}", filePath, e.getMessage());
        }
        return null;
    }

    public String toXML(City city) {
        XmlMapper xmlMapper = new XmlMapper();
        logger.debug("Инициализация XmlMapper для конвертации объекта в XML");

        try {
            logger.info("Конвертация объекта City в XML");
            String xml = xmlMapper.writeValueAsString(city);
            logger.debug("Сгенерированный XML: {}", xml);
            return xml;
        } catch (Exception e) {
            logger.error("Ошибка конвертации в XML: {}", e.getMessage());
            logger.warn("Убедитесь, что объект City корректно заполнен перед конвертацией");
        }
        return null;
    }

    public void saveXMLToFile(String xmlData, String filePath) {
        logger.debug("Инициализация записи XML в файл");

        try (FileWriter writer = new FileWriter(filePath)) {
            logger.info("Сохранение XML в файл: {}", filePath);
            writer.write(xmlData);
            logger.info("XML успешно сохранен в файл {}", filePath);
        } catch (IOException e) {
            logger.error("Ошибка сохранения XML в файл {}: {}", filePath, e.getMessage());
            logger.warn("Проверьте доступность файла и права записи");
        }
    }
}

