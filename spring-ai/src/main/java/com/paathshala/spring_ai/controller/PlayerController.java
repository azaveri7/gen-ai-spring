package com.paathshala.spring_ai.controller;

import com.paathshala.spring_ai.model.Player;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class PlayerController {

    private final ChatClient chatClient;

    // Define the system prompt once
    private static final String SYSTEM_PROMPT = """
            You are a helpful assistant that generates data in a strict JSON format.
            Do not include any introductory text, plain text lists, or markdown formatting (e.g., ```json).
            Only provide the raw JSON array containing the list of player achievements.
            """;

    public PlayerController(ChatClient.Builder builder) {
        this.chatClient = builder
                .defaultSystem(SYSTEM_PROMPT)
                .build();
    }

    @GetMapping("/player-achievements")
    public List<Player> getPlayerAchievements(@RequestParam  String name) {

        BeanOutputConverter<List<Player>> converter = new BeanOutputConverter<>(
                new ParameterizedTypeReference<List<Player>>() {
                }
        );

        /*String message = """
                Generate a list of career achievements for the sportsperson {sports}.
                Include the player as the key and achievements as the value for it.
                """;

        PromptTemplate template = new PromptTemplate(message);*/
        // The fluent API handles the converter and formatting instructions automatically
        String userMessage = """
                Generate a list of career achievements for the sportsperson {name}.
                Map the achievements list into the 'achievements' field of the Player object.
                """;

        //Prompt promt = template.create(Map.of("sports", name, "format", converter.getFormat()));

        /*ChatResponse response =  chatClient
                .prompt(promt)
                .call()
                .chatResponse();

        return response.getResult().getOutput().getText();*/
        /*Generation result = chatClient
                .prompt(promt)
                .call()
                .chatResponse()
                .getResult();

        return converter.convert(result.getOutput().getText());*/
        return chatClient.prompt()
                .user(u -> u.text(userMessage).param("name", name))
                // .entity() automatically maps the *entire* valid JSON response
                // into the specified Java type (List<Player>).
                .call()
                .entity(new ParameterizedTypeReference<List<Player>>() {});
    }

}
