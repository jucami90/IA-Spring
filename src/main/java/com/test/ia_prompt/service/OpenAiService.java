package com.test.ia_prompt.service;

import com.test.ia_prompt.record.Answer;
import com.test.ia_prompt.record.Question;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

@Service
public class OpenAiService implements BoardService {

    private final ChatClient chatClient;

    public OpenAiService(OpenAiChatModel openAiChatModel) {
        this.chatClient = ChatClient.builder(openAiChatModel).build();
    }

    @Override
    public Answer askQuestion(Question question) {
        var answerText = chatClient.prompt()
                .user(question.question())
                .call()
                .content();
        return new Answer(answerText);
    }

}
