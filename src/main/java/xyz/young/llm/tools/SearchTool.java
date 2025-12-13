package xyz.young.llm.tools;

import java.io.IOException;
import java.text.MessageFormat;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchTool {

    private static final String URL = "https://serpapi.com/search.json?engine=google";
    private static final String API_KEY = "008697c270fde6498083fcf20733c0f84d755a5aea9e42a4e36d4d2c8f7f1a92";

    public static void main(String[] args) {
        SearchTool searchTool = new SearchTool();
        searchTool.search("最新款的小米手机是什么");
    }

    @Tool(description = "一个基于SerpApi的网页搜索引擎工具,它会智能地解析搜索结果，优先返回直接答案或知识图谱信息。")
    public String search(@ToolParam(description = "搜索的内容") String query) {
        OkHttpClient client = new OkHttpClient();
        String queryUrl =
            MessageFormat.format("https://serpapi.com/search.json?engine=google&q={0}&api_key={1}", query, API_KEY);
        Request request = new Request.Builder().url(queryUrl).build();
        try (Response response = client.newCall(request).execute()) {
            System.out.println(response.body().string());
            return response.body().string();
        } catch (IOException e) {
            return MessageFormat.format("查询异常，无法获取{0}的查询结果", query);
        }
    }
}
