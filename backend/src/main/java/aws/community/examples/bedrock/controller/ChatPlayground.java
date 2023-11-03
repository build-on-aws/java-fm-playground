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
    public InvokeLlmChatResponse invokeLlm(@PathVariable String modelId, @RequestBody InvokeLlmChatRequest requestBody) {

        switch (modelId) {
            case "anthropic.claude-v2":
                return claudeV2(requestBody);
            default:
                return null;
        }
    }

    private InvokeLlmChatResponse claudeV2(InvokeLlmChatRequest requestBody) {
        JSONObject jsonBody = new JSONObject()
                .put("prompt", "Human: " + requestBody.prompt + " Assistant:")
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

        String completion = jsonObject.getString("completion");

        return new InvokeLlmChatResponse(completion);
    }

    public record InvokeLlmChatRequest(String prompt) { }

    public record InvokeLlmChatResponse(String text) { }

}
