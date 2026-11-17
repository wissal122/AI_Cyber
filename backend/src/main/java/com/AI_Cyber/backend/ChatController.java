
package com.AI_Cyber.backend;

import com.google.genai.ResponseStream;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@CrossOrigin(
    origins = "http://localhost:4200",
    methods = {
        org.springframework.web.bind.annotation.RequestMethod.POST,
        org.springframework.web.bind.annotation.RequestMethod.OPTIONS
    }
)
@RestController
@RequestMapping("/api")
public class ChatController {

    private final GeminiService geminiService;

    public ChatController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @PostMapping("/chat")
    public SseEmitter chat(@RequestBody ChatRequest request) {

        SseEmitter emitter = new SseEmitter(120000L);

        new Thread(() -> {

            try (ResponseStream<GenerateContentResponse> stream =
                         geminiService.askStream(request.getMessage())) {

                for (GenerateContentResponse response : stream) {

                    String text = response.text();

                    if (text != null && !text.isEmpty()) {
                        emitter.send(
                            SseEmitter.event()
                                .data(text)
                        );
                    }
                }

                emitter.complete();

            } catch (Exception e) {

                System.err.println("Erreur Gemini : " + e.getMessage());

                try {
                    emitter.send(
                        SseEmitter.event()
                            .data("Désolé, le service IA est temporairement indisponible. Veuillez réessayer dans quelques instants.")
                    );

                    emitter.complete();

                } catch (Exception ignored) {
                    emitter.completeWithError(e);
                }
            }

        }).start();

        return emitter;
    }
}
