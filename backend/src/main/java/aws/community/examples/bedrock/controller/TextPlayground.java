package aws.community.examples.bedrock.controller;

import aws.community.examples.bedrock.aimodels.Claude;
import aws.community.examples.bedrock.aimodels.Jurassic2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.AccessDeniedException;

import static aws.community.examples.bedrock.aimodels.LLM.Request;
import static aws.community.examples.bedrock.aimodels.LLM.Response;

@RestController
public class TextPlayground {

    private static final Logger logger = LoggerFactory.getLogger(TextPlayground.class);

    private final BedrockRuntimeClient client;

    @Autowired
    public TextPlayground(final BedrockRuntimeClient client) {
        this.client = client;
    }

    @PostMapping("/foundation-models/model/text/{modelId}/invoke")
    public Response invoke(@PathVariable String modelId, @RequestBody Request request) {
        try {

            return switch (modelId) {

                case Claude.MODEL_ID -> {
                    String completion = Claude.invoke(client, request.prompt(), request.temperature(), request.maxTokens());
                    yield new Response(completion);
                }

                case Jurassic2.MODEL_ID -> {
                    String completion = Jurassic2.invoke(client, request.prompt(), request.temperature(), request.maxTokens());
                    yield new Response(completion);
                }

                default -> throw new IllegalArgumentException("Unsupported model ID");
            };

        } catch (AccessDeniedException e) {
            logger.error("Access Denied: %s".formatted(e.getMessage()));
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            logger.error("Exception: %s".formatted(e.getMessage()));
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
