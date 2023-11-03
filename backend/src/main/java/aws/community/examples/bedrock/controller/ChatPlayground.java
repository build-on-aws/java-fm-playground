package aws.community.examples.bedrock.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelRequest;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelResponse;

import java.nio.charset.StandardCharsets;

@RestController
public class ChatPlayground {

    private final BedrockRuntimeClient client;

    @Autowired
    public ChatPlayground(final BedrockRuntimeClient client) {
        this.client = client;
    }

    @PostMapping("/foundation-models/model/{modelId}/invoke")
    public String invokeLlm(@PathVariable String modelId, @RequestBody LlmChatRequest body) {

        return switch (modelId) {
            case "anthropic.claude-v2" -> invokeClaudeV2(body.prompt);
            default -> null;
        };

    }

    private String invokeClaudeV2(String prompt) {
        JSONObject jsonBody = new JSONObject()
                .put("prompt", "Human: " + prompt + " Assistant:")
                .put("temperature", 0.8)
                .put("max_tokens_to_sample", 1024);

        SdkBytes sdkBytesBody = SdkBytes.fromUtf8String(
                jsonBody.toString()
        );

        InvokeModelRequest request = InvokeModelRequest.builder()
                .modelId("anthropic.claude-v2")
                .body(sdkBytesBody)
                .build();

        InvokeModelResponse response = client.invokeModel(request);

        JSONObject jsonObject = new JSONObject(
                response.body().asString(StandardCharsets.UTF_8)
        );

        return jsonObject.getString("completion");
    }

    public record LlmChatRequest(String prompt) { }

}
