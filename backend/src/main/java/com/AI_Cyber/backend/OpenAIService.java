
package com.AI_Cyber.backend;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseCreateParams;

public class OpenAIService {

    private final OpenAIClient client;

    public OpenAIService() {
        this.client = OpenAIOkHttpClient.fromEnv();
    }

    public String ask(String message) {

        ResponseCreateParams params = ResponseCreateParams.builder()
                .model("gpt-6-astra")
                .input(message)
                .build();

        Response response = client.responses().create(params);

        return response.output()
                .stream()
                .flatMap(item -> item.message().stream())
                .flatMap(messageItem -> messageItem.content().stream())
                .flatMap(content -> content.outputText().stream())
                .map(outputText -> outputText.text())
                .findFirst()
                .orElse("Désolé, je n'ai pas pu générer une réponse.");
    }
}
