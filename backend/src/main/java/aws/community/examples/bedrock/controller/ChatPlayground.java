package aws.community.examples.bedrock.controller;

import aws.community.examples.bedrock.aimodels.Claude;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.AccessDeniedException;

import static aws.community.examples.bedrock.aimodels.LLM.Request;
import static aws.community.examples.bedrock.aimodels.LLM.Response;

@RestController
public class ChatPlayground {

    private static final Logger logger = LoggerFactory.getLogger(ChatPlayground.class);

    private final BedrockRuntimeClient client;

    @Autowired
    public ChatPlayground(final BedrockRuntimeClient client) {
        this.client = client;
    }

    @PostMapping("/foundation-models/model/chat/anthropic.claude-v2/invoke")
    public Response invoke(@RequestBody Request body) {
        try {

            String systemPrompt =
                    """
                            Take the role of a friendly chat bot. Your responses are brief.
                            You sometimes use emojis where appropriate, but you don't overdo it.
                            You engage human in a dialog by regularly asking questions,
                            except when Human indicates that the conversation is over.
                            """;

            String prompt = systemPrompt + "\n\n" + body.prompt();

            return new Response(Claude.invoke(client, prompt, 0.8, 300));

        } catch (AccessDeniedException e) {
            logger.error("Access Denied: %s".formatted(e.getMessage()));
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            logger.error("Exception: %s".formatted(e.getMessage()));
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
