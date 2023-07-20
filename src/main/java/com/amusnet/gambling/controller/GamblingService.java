package com.amusnet.gambling.controller;

import com.amusnet.gambling.dto.GameActivityDto;
import com.amusnet.gambling.dto.PlayerDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class GamblingService {

    private RestTemplate restTemplate = new RestTemplate();

    public double sumGGR() {
        List<GameActivityDto> gameActivityDtos = retrieveActivityByPlayerIds(0,20*30);
        double sumOfBetAmount = gameActivityDtos.stream().map(gameActivityDto -> gameActivityDto.getBetAmount()).reduce(0.0,Double::sum);
        double sumOfWinAmount = gameActivityDtos.stream().map(gameActivityDto -> gameActivityDto.getWinAmount()).reduce(0.0,Double::sum);
        double result = sumOfBetAmount - sumOfWinAmount;
        log.info(String.valueOf(result));
        return result;
    }

    private List<PlayerDto> retrievePlayerQuantity(Integer page, Integer pageSize) {
        String url = "https://challenge.dev.amusnetgaming.net/players?page="+page+"&pageSize="+pageSize;
        ObjectMapper mapper = new ObjectMapper();
        List<PlayerDto> playerDtoList = mapper.convertValue(this.restTemplate.getForObject(url ,List.class), new TypeReference<List<PlayerDto>>() {
        });
        return playerDtoList;
    }

    private List<GameActivityDto> retrieveActivityByPlayerIds(Integer page, Integer pageSize) {
        Map<String, Object> playerIdParams = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        List<PlayerDto> firstThirtyPlayer = retrievePlayerQuantity(0,30);
        List<Integer> ids = firstThirtyPlayer.stream().map(playerDto -> playerDto.getId()).collect(Collectors.toList());
        playerIdParams.put("playerIds",ids);
        playerIdParams.put("page",page);
        playerIdParams.put("pageSize",pageSize);
        StringBuilder url = new StringBuilder("https://challenge.dev.amusnetgaming.net/game-activity?");
        buildUrl(url,playerIdParams);
        List<GameActivityDto> gameActivityDtos = mapper.convertValue(this.restTemplate.getForObject(url.toString(), List.class), new TypeReference<List<GameActivityDto>>() {
        });
        return gameActivityDtos;
    }

    private void buildUrl(StringBuilder url,Map<String, Object> playerIdParams){
        List<PlayerDto> playerDtos = (List<PlayerDto>) playerIdParams.get("playerIds");
        for(int i = 0; i < playerDtos.size(); ++i) {
            url.append("playerIds="+playerDtos.get(i)+"&");
        }
        Integer page = (Integer) playerIdParams.get("page");
        Integer pageSize = (Integer) playerIdParams.get("pageSize");
        url.append("page="+page);
        url.append("&pageSize="+pageSize);
    }

}
