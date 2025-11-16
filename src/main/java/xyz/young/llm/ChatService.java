package xyz.young.llm;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class ChatService {

    @Autowired
    private ChatClient chatClient;

    @RequestMapping("chat")
    public Flux<String> chat(String prompt) {
        return chatClient.prompt().user(
                u -> u.text("Tell me the names of 5 movies whose soundtrack was composed by {composer}").param("composer", prompt))
                .stream().content();
    }
}
