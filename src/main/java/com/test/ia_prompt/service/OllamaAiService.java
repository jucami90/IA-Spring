package com.test.ia_prompt.service;

import com.test.ia_prompt.record.Answer;
import com.test.ia_prompt.record.Question;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class OllamaAiService implements BoardService {

    private final ChatClient chatClient;
    private final RulesService rulesService;

    public OllamaAiService(OllamaChatModel ollamaChatModel,
                           RulesService rulesService) {
        this.chatClient = ChatClient.builder(ollamaChatModel).build();
        this.rulesService = rulesService;
    }

    @Value("classpath:/promptTemplates/questionPromptTemplate.st")
    Resource questionPromptTemplate;

    @Override
    public Answer askQuestion(Question question) {

        var rules = rulesService.getRulesFor(question.gameTitle());

        var answerText = chatClient.prompt()
                .user(userSpec -> userSpec
                        .text(questionPromptTemplate)
                        .param("gameTitle", question.gameTitle())
                        .param("question", question.question())
                        .param("rules", rules))
                .call()
                .content();

        return new Answer(question.gameTitle(), answerText);
    }
}
