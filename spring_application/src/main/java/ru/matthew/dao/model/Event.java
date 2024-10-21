package ru.matthew.dao.model;

import jakarta.persistence.ManyToOne;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    private Long id;
    private String title;
    private Timestamp date;
    private String description;
    private Double price;

    @ManyToOne
    private Location locations;
}
