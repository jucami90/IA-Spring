package com.test.ia_prompt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.test.ia_prompt.record.Question;
import com.test.ia_prompt.service.OllamaAiService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;

import java.io.IOException;
import java.nio.charset.Charset;

@EnableWireMock(
        @ConfigureWireMock(baseUrlProperties = "ollama.base.url"))
@SpringBootTest(properties = {
        "spring.ai.openai.api-key=test",
        "spring.ai.anthropic.api-key=test",
        "spring.ai.ollama.base-url=${ollama.base.url}"
})
public class OllamaAiServiceTests {

    @Value("classpath:/test-ollama-response.json")
    Resource responseResource;

    @Autowired
    OllamaChatModel ollamaChatModel;

    @BeforeEach
    public void setup() throws IOException {
        var cannedResponse =
                responseResource.getContentAsString(Charset.defaultCharset());
        var mapper = new ObjectMapper();
        var responseNode = mapper.readTree(cannedResponse);
        WireMock.stubFor(WireMock.post("/api/chat")
                .willReturn(ResponseDefinitionBuilder.okForJson(responseNode)));
    }

    @Test
    public void testAskQuestion() {
        var ollamaService = new OllamaAiService(ollamaChatModel);
        var answer = ollamaService.askQuestion(
                new Question("Capitals","What is the capital of France?"));
        Assertions.assertThat(answer).isNotNull();
        Assertions.assertThat(answer.answer()).isEqualTo("Paris");
    }
}