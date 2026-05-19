package com.test.ia_prompt;

import com.test.ia_prompt.record.Question;
import com.test.ia_prompt.service.OpenAiService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;

import java.io.IOException;
import java.nio.charset.Charset;

@EnableWireMock(
		@ConfigureWireMock(baseUrlProperties = "openai.base.url"))
@SpringBootTest(properties = {
		"spring.ai.openai.base-url=${openai.base.url}",
		"spring.ai.anthropic.api-key=test",
		"spring.ai.ollama.base-url=http://localhost:11434"
})
public class IaPromptApplicationTests {

	@Value("classpath:/test-openai-response.json")
	Resource responseResource;

	@Autowired
	OpenAiChatModel openAiChatModel;

	@BeforeEach
	public void setup() throws IOException {
		var cannedResponse =
				responseResource.getContentAsString(Charset.defaultCharset());
		var mapper = new ObjectMapper();
		var responseNode = mapper.readTree(cannedResponse);
		WireMock.stubFor(WireMock.post("/v1/chat/completions")
				.willReturn(ResponseDefinitionBuilder.okForJson(responseNode)));
	}

	@Test
	public void testAskQuestion() {
		var boardGameService =
				new OpenAiService(openAiChatModel);
		var answer =
				boardGameService.askQuestion(
						new Question("What is the capital of France?"));
		Assertions.assertThat(answer).isNotNull();
		Assertions.assertThat(answer.answer()).isEqualTo("Paris");
	}
}
