package com.test.ia_prompt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.test.ia_prompt.record.Question;
import com.test.ia_prompt.service.AnthropicAiService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.anthropic.AnthropicChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;

import java.io.IOException;
import java.nio.charset.Charset;

@EnableWireMock(
        @ConfigureWireMock(baseUrlProperties = "anthropic.base.url"))
@SpringBootTest(properties = {
        "spring.ai.openai.api-key=test",
        "spring.ai.anthropic.base-url=${anthropic.base.url}",
        "spring.ai.ollama.base-url=http://localhost:11434"
})
public class AnthropicAiServiceTests {

    @Value("classpath:/test-anthropic-response.json")
    Resource responseResource;

    @Autowired
    AnthropicChatModel anthropicChatModel;

    @BeforeEach
    public void setup() throws IOException {
        var cannedResponse =
                responseResource.getContentAsString(Charset.defaultCharset());
        var mapper = new ObjectMapper();
        var responseNode = mapper.readTree(cannedResponse);
        WireMock.stubFor(WireMock.post("/v1/messages")
                .willReturn(ResponseDefinitionBuilder.okForJson(responseNode)));
    }

    @Test
    public void testAskQuestion() {
        var anthropicService = new AnthropicAiService(anthropicChatModel);
        var answer = anthropicService.askQuestion(
                new Question("Capitals","What is the capital of France?"));
        Assertions.assertThat(answer).isNotNull();
        Assertions.assertThat(answer.answer()).isEqualTo("Paris");
    }
}