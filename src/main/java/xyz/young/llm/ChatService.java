package xyz.young.llm;

import io.micrometer.common.util.StringUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.young.llm.agent.ReActAgent;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ChatService {

    private final static String PROMPT = """
            请注意，你是一个有能力调用外部工具的智能助手。请严格按照以下格式进行回应:
            Thought: 你的思考过程，用于分析问题、拆解任务和规划下一步行动。
            Action: 你决定采取的行动，必须是以下格式之一:
            `{{tool_name}}`:调用一个可用工具。
            `Finish[最终答案]`:当你认为已经获得最终答案时。
            当你收集到足够的信息，能够回答用户的最终问题时，你必须在Action:字段后使用 finish(answer=) 来输出最终答案。
            现在，请开始解决以下问题:
            Question: {question}
            History: {history}""";


    @Autowired
    private ReActAgent reActAgent;

    @Autowired
    private ChatClient chatClient;

    @RequestMapping("chat")
    public String chat(String prompt, String voice) {
        return chatClient.prompt().system(sp -> sp.param("voice", voice)).user(u -> u.text("告诉我五部由{composer}主演的动作电影").param("composer", prompt)).advisors(new SimpleLoggerAdvisor()).call().content();
    }

    @RequestMapping("reActAgent")
    public String reActAgent(String query, int maxSteps) {
        int currentStep = 0;
        List<String> history = new ArrayList<>();
        while (currentStep < maxSteps) {
            currentStep += 1;
            String result = chatClient.prompt().user(u -> u.text(PROMPT).param("question", query).param("history", history)).call().content();
            String action = reActAgent.parseAction(result);
            if (StringUtils.isNotEmpty(action) && action.startsWith("Finish")) {
                return reActAgent.parseFinish(action);
            }
            String thought = reActAgent.parseThought(result);
            history.add(String.format("Thought:%s", thought));
            history.add(String.format("Action:%s", action));
        }
        return history.toString();
    }
}
