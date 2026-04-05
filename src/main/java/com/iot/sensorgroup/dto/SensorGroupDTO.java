package com.iot.sensorgroup.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "sensorgroups")
public class SensorGroupDTO {

    @Id
    @JsonProperty("_id")
    private Long id;

    @JsonProperty("name")
    @Field("name")
    private String name;

    @JsonProperty("services")
    @Field("services")
    private List<String> services;

    @JsonProperty("readings")
    @Field("readings")
    private List<ReadingDTO> readings;
}
