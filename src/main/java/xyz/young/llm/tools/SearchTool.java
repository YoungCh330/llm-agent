package xyz.young.llm.tools;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Objects;
import java.util.Optional;

public class SearchTool {

    private static final String URL = "https://serpapi.com/search.json";
    private static final String API_KEY = "008697c270fde6498083fcf20733c0f84d755a5aea9e42a4e36d4d2c8f7f1a92";


    @Tool(description = "一个基于SerpApi的网页搜索引擎工具,它会智能地解析搜索结果，优先返回直接答案或知识图谱信息。")
    public String search(@ToolParam(description = "搜索的内容") String query) {
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder builder = Objects.requireNonNull(HttpUrl.parse(URL)).newBuilder();
        builder.addQueryParameter("api_key", API_KEY);
        builder.addQueryParameter("engine", "google");
        builder.addQueryParameter("q", query);
        builder.addQueryParameter("gl", "cn");
        builder.addQueryParameter("hl", "zh-cn");
        Request request = new Request.Builder().url(builder.build()).build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            ObjectMapper mapper = new ObjectMapper();
            if (response.body() != null) {
                JsonNode responseJson = mapper.readTree(response.body().string());
                Optional<JsonNode> results = Optional.of(responseJson.get("organic_results"));
                ArrayNode resultsJson = new ArrayNode(mapper.getNodeFactory());
                results.ifPresent(jsonNode -> {
                    int size = Math.min(jsonNode.size(), 3);
                    for (int i = 0; i < size; i++) {
                        resultsJson.add(jsonNode.get(i));
                    }
                });
                return resultsJson.toString();
            }
            return MessageFormat.format("查询异常，无法获取{0}的查询结果", query);
        } catch (IOException e) {
            return MessageFormat.format("查询异常，无法获取{0}的查询结果", query);
        }
    }
}
