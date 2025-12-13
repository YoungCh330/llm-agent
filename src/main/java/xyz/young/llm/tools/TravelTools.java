package xyz.young.llm.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TravelTools {

    private static final String WEATHER_URL = "https://wttr.in/%s?format=j1";

    @Tool(description = "获取城市的天气情况")
    public String getWeather(@ToolParam(description = "城市的名称") String city) throws JsonProcessingException {
        WebClient client = WebClient.builder().baseUrl(String.format(WEATHER_URL, city)).build();
        String response = client.get().retrieve().bodyToMono(String.class).block();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode weather = objectMapper.readTree(response);
        return String.format("%s当前天气:%s,气温%.1f摄氏度", city,
            weather.get("current_condition").get(0).get("weatherDesc").get(0).get("value").asText(),
            weather.get("current_condition").get(0).get("temp_C").asDouble());
    }
}
