package ru.matthew.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location implements Identifiable<String> {
    private String slug;
    private String name;

    @Override
    @JsonIgnore
    public String getKey() {
        return slug;
    }
}
