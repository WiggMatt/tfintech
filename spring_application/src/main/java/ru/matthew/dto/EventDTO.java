package ru.matthew.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDTO {
    private int id;
    private String title;
    private String description;
    private DateRangeDTO[] dates;
    private String currency;
    private String price;
}
