package com.test.ia_prompt.service;

import com.test.ia_prompt.record.Answer;
import com.test.ia_prompt.record.Question;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.stereotype.Service;

@Service
public class OllamaAiService implements BoardService {

    private final ChatClient chatClient;

    public OllamaAiService(OllamaChatModel ollamaChatModel) {
        this.chatClient = ChatClient.builder(ollamaChatModel).build();
    }

    @Override
    public Answer askQuestion(Question question) {
        String prompt = "Answer this question about " + question.gameTitle() +
                ": " + question.question();

        var answerText = chatClient.prompt()
                .user(prompt)
                .call()
                .content();
        return new Answer(question.gameTitle(), answerText);
    }

}
