package xyz.young.llm;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.young.llm.tools.SearchTool;

@Configuration
public class ChatConfiguration {

    @Bean
    public ChatClient chatClient(ChatModel chatModel) {
        //defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build()).
        return ChatClient.builder(chatModel).defaultTools(new SearchTool()).build();
    }

    @Bean
    public ChatMemory chatMemory() {
        return MessageWindowChatMemory.builder().build();
    }
}
