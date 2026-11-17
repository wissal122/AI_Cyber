
package com.AI_Cyber.backend;

import com.google.genai.Client;
import com.google.genai.ResponseStream;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.stereotype.Service;

@Service
public class GeminiService {

    private final Client client;

    public GeminiService() {
        String apiKey = System.getenv("GEMINI_API_KEY");

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException(
                    "GEMINI_API_KEY is missing. Please configure your API key."
            );
        }

        this.client = Client.builder()
                .apiKey(apiKey)
                .build();
    }

    public ResponseStream<GenerateContentResponse> askStream(String message) {

        int maxAttempts = 3;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {

            try {
                System.out.println("Gemini attempt: " + attempt);

                return client.models.generateContentStream(
                        "gemini-3.7-flash",
                        message,
                        null
                );

            } catch (RuntimeException e) {

                String error = e.getMessage();

                boolean temporaryError = error != null &&
                        (error.contains("503") ||
                         error.contains("high demand"));

                if (!temporaryError || attempt == maxAttempts) {
                    throw e;
                }

                long delay = attempt * 2000L;

                System.out.println(
                        "Gemini temporarily unavailable. Retrying in "
                        + delay / 1000 + " seconds..."
                );

                try {
                    Thread.sleep(delay);

                } catch (InterruptedException interruptedException) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(
                            "Gemini retry interrupted.",
                            interruptedException
                    );
                }
            }
        }

        throw new IllegalStateException("Unable to contact Gemini.");
    }
}
