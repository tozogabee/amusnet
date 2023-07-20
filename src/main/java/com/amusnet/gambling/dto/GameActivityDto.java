package com.amusnet.gambling.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GameActivityDto {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("playerId")
    private Integer playerId;

    @JsonProperty("betAmount")
    private Double betAmount;

    @JsonProperty("winAmount")
    private Double winAmount;

    @JsonProperty("currency")
    private String currency;
}
