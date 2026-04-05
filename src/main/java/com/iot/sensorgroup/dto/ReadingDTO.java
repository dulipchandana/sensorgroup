package com.iot.sensorgroup.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReadingDTO {

    @JsonProperty("type")
    @Field("type")
    private String type;

    @JsonProperty("unit")
    @Field("unit")
    private Double unit;

    @JsonProperty("timestamp")
    @Field("timestamp")
    private String timestamp;
}
