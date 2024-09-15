package ru.matthew.lesson2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class City {
    private String slug;
    private Coords coords;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Coords {
        private double lat;
        private double lon;
    }
}
