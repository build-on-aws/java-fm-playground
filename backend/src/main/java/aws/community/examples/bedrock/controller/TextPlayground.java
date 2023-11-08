package aws.community.examples.bedrock.controller;

import aws.community.examples.bedrock.aimodels.Claude;
import aws.community.examples.bedrock.aimodels.Jurassic2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;

@RestController
public class TextPlayground {

    private final BedrockRuntimeClient client;

    @Autowired
    public TextPlayground(final BedrockRuntimeClient client) {
        this.client = client;
    }

    @PostMapping("/foundation-aimodels/model/text/{modelId}/invoke")
    public Response invoke(@PathVariable String modelId, @RequestBody Request request) {
        return switch (modelId) {

            case "anthropic.claude-v2" -> {
                String completion = Claude.invoke(client, request.prompt(), request.temperature(), request.maxTokens());
                yield new Response(completion);
            }

            case "ai21.j2-mid-v1" -> {
                String completion = Jurassic2.invoke(client, request.prompt(), request.temperature(), request.maxTokens());
                yield new Response(completion);
            }

            default -> throw new IllegalArgumentException("Unsupported model ID");
        };
    }

    public record Request(String prompt, Double temperature, Integer maxTokens) { }
    public record Response(String completion) { }
}
