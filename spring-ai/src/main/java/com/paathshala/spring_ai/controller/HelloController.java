package com.paathshala.spring_ai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class HelloController {


    private final ChatClient chatClient;

    @Value("classpath:/prompts/celeb-details.st")
    private Resource celelPrompt;

    public HelloController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping
    public String prompt(@RequestParam String message) {
        return chatClient
                .prompt(message)
                .call()
                .chatResponse()
                .getResult()
                .getOutput()
                .getText();
    }

    @GetMapping("/celeb-details")
    public String getCelebDetails(@RequestParam String name) {
        String message = """
                List the details of the famour personality {name} along with their career achievements.
                Show the details in the readable format""";

        PromptTemplate template = new PromptTemplate(message);
        Prompt prompt = template.create(
                Map.of("name", name)
        );

        return chatClient
                .prompt(prompt)
                .call()
                .chatResponse()
                .getResult()
                .getOutput()
                .getText();
    }

    @GetMapping("/celeb-details-file-prompt")
    public String getCelebDetailsWithPromptInFile(@RequestParam String name) {

        PromptTemplate template = new PromptTemplate(celelPrompt);
        Prompt prompt = template.create(
                Map.of("name", name)
        );

        return chatClient
                .prompt(prompt)
                .call()
                .chatResponse()
                .getResult()
                .getOutput()
                .getText();
    }

    @GetMapping("/sports")
    public String getSportsDetail(@RequestParam String name) {
        String message = """
                List the details of the Sport %s along with their rules and regulations. 
                Show the details in the readable format""";

       String systemMessage = """
               You are a smart virtual assistant. Your task is to give the details about the sports.
               If someone ask about something else and you do not know the answer, just say that you do not know the answer.
               """;

        UserMessage userMessage = new UserMessage(String.format(message, name));

        SystemMessage systemMessage1 = new SystemMessage(systemMessage);

        Prompt prompt = new Prompt(List.of(userMessage, systemMessage1));

        return chatClient
                .prompt(prompt)
                .call()
                .chatResponse()
                .getResult()
                .getOutput()
                .getText();

    }
}
