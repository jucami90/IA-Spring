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
    private static final String questionPromptTemplate = """
    You are a helpful assistant, answering questions about tabletop games.
    If you don't know anything about the game or don't know the answer,
    say "I don't know".

    The game is {gameTitle}.

    The question is: {question}.
    """;

    @Override
    public Answer askQuestion(Question question) {
        var answerText = chatClient.prompt()
                .user(userSpec -> userSpec
                        .text(questionPromptTemplate)
                        .param("gameTitle", question.gameTitle())
                        .param("question", question.question()))
                .call()
                .content();

        return new Answer(question.gameTitle(), answerText);
    }
}
