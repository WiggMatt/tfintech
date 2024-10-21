package ru.matthew.dao.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaceCategory implements Identifiable<Integer>{
    private int id;
    private String slug;
    private String name;

    @Override
    @JsonIgnore
    public Integer getKey() {
        return id;
    }
}
