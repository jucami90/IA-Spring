package com.test.ia_prompt.service;

import com.test.ia_prompt.record.Answer;
import com.test.ia_prompt.record.Question;
import org.springframework.ai.anthropic.AnthropicChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AnthropicAiService implements BoardService {

    private final ChatClient chatClient;

    public AnthropicAiService(AnthropicChatModel anthropicChatModel) {
        this.chatClient = ChatClient.builder(anthropicChatModel).build();
    }

    @Override
    public Answer askQuestion(Question question) {
        var answerText = chatClient.prompt()
                .user(question.question())
                .call()
                .content();
        return new Answer(question.gameTitle(), answerText);
    }

}