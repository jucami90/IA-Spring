# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Run the application
./gradlew bootRun

# Run all tests
./gradlew test

# Run a single test class
./gradlew test --tests "com.test.ia_prompt.IaPromptApplicationTests"

# Run a single test method
./gradlew test --tests "com.test.ia_prompt.IaPromptApplicationTests.testAskQuestion"

# Build without running tests
./gradlew build -x test
```

## Environment

The app requires the `SPRING_AI_OPENAI_API_KEY` environment variable to be set at runtime. Tests do **not** need it — WireMock intercepts all OpenAI calls.

```bash
export SPRING_AI_OPENAI_API_KEY="sk-..."
```

## Architecture

Single REST endpoint that proxies questions to OpenAI via Spring AI:

```
POST /ask  { "question": "..." }  →  { "answer": "..." }
```

**Request flow:** `AskController` receives a `Question` record, delegates to `BoardService` (interface), which is implemented by `SpringAiService`. `SpringAiService` uses Spring AI's `ChatClient` to call OpenAI's chat completions API and wraps the string response in an `Answer` record.

**Key design decision:** `BoardService` is an interface separating the controller from the Spring AI implementation. If a new AI provider or logic is needed, only the service implementation changes.

**Testing strategy:** Integration tests use `wiremock-spring-boot`. `@EnableWireMock` starts a WireMock server and injects its base URL into `spring.ai.openai.base-url`, redirecting all OpenAI calls to the mock. The canned response is loaded from `src/test/resources/test-openai-response.json`.

## Stack

- Java 25, Spring Boot 3.5, Spring AI 1.1.6
- `spring-ai-starter-model-openai` for the OpenAI integration
- `wiremock-spring-boot` for integration tests