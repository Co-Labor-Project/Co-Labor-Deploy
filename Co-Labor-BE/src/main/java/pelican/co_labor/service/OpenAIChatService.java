package pelican.co_labor.service;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OpenAIChatService {
    private final ChatClient chatClient;

    @Autowired
    public OpenAIChatService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String getGptResponse(String userMessage) {
        List<Message> messages = new ArrayList<>();
        messages.add(new SystemMessage("You are a legal chatbot helping foreign workers in Korea."));
        messages.add(new UserMessage(userMessage));

        Prompt prompt = new Prompt(messages);

        ChatResponse response = chatClient.call(prompt);

        return response.getResult().getOutput().getContent();
    }
}