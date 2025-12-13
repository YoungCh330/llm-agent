package xyz.young.llm;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import xyz.young.llm.tools.TravelTools;

@RestController
public class ChatService {

    @Autowired
    private ChatMemory chatMemory;

    @Autowired
    private ChatClient chatClient;

    @RequestMapping("chat")
    public String chat(String prompt, String voice) {
        return chatClient.prompt().system(sp -> sp.param("voice", voice))
            .user(u -> u.text("告诉我五部由{composer}主演的动作电影").param("composer", prompt)).advisors(new SimpleLoggerAdvisor())
            .call().content();
    }

    @RequestMapping("agent")
    public String agent(String city) throws JsonProcessingException {
        return chatClient.prompt().user(u -> u.text("请帮我规划一个{city}在当前天气的情况下值得去的景点").param("city", city))
            .tools(new TravelTools()).call().content();
    }
}
