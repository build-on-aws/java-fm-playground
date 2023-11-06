package aws.community.examples.bedrock.controller;

import aws.community.examples.bedrock.models.Claude;
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

    @PostMapping("/foundation-models/model/text/{modelId}/invoke")
    public Claude.Response invokeLlm(@PathVariable String modelId, @RequestBody Claude.Request body) {
        return switch (modelId) {
            case "anthropic.claude-v2" -> Claude.invoke(body, modelId, Claude.Type.TEXT, client);
            default -> null;
        };
    }
}
