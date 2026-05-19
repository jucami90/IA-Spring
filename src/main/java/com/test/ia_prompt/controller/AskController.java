package com.test.ia_prompt.controller;

import com.test.ia_prompt.record.Answer;
import com.test.ia_prompt.record.Question;
import com.test.ia_prompt.service.AnthropicAiService;
import com.test.ia_prompt.service.OllamaAiService;
import com.test.ia_prompt.service.OpenAiService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AskController {

    private final OpenAiService openAiService;
    private final AnthropicAiService anthropicAiService;
    private final OllamaAiService ollamaAiService;

    public AskController(OpenAiService openAiService, AnthropicAiService anthropicAiService, OllamaAiService ollamaAiService) {
        this.openAiService = openAiService;
        this.anthropicAiService = anthropicAiService;
        this.ollamaAiService = ollamaAiService;
    }

    @PostMapping(path="/ask", produces="application/json")
    public Answer ask(@RequestBody Question question) {
        return openAiService.askQuestion(question);
    }

    @PostMapping(path="/ask/anthropic", produces="application/json")
    public Answer askAnthropic(@RequestBody Question question) {
        return anthropicAiService.askQuestion(question);
    }

    @PostMapping(path="/ask/ollama", produces="application/json")
    public Answer askOllama(@RequestBody Question question) {
        return ollamaAiService.askQuestion(question);
    }

}