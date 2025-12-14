package xyz.young.llm.agent;

import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ReActAgent {

    private static final Pattern THOUGHT_PATTERN = Pattern.compile("Thought: (.*)");

    private static final Pattern ACTION_PATTERN = Pattern.compile("Action: (.*)");

    private static final Pattern FINISH_PATTERN = Pattern.compile("Finish\\[(.*)]");

    /**
     * 获取大模型的执行行动前的思考
     *
     * @param text 大模型的输出
     * @return 大模型的思考过程
     */
    public String parseThought(String text) {
        Matcher matcher = THOUGHT_PATTERN.matcher(text);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    /**
     * 获取大模型的执行的行动
     *
     * @param text 大模型的输出
     * @return 大模型执行的行动
     */
    public String parseAction(String text) {
        Matcher matcher = ACTION_PATTERN.matcher(text);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    /**
     * 获取最终结果
     *
     * @param text action字符串
     * @return 最终结果
     */
    public String parseFinish(String text) {
        Matcher matcher = FINISH_PATTERN.matcher(text);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
