package aws.community.examples.bedrock;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelRequest;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelResponse;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
public class ChatPlayground {

    @PostMapping("/foundation-models/model/anthropic.claude-v2/invoke")
    public InvokeClaudeV2ChatResponse invokeClaudeV2(@RequestBody InvokeClaudeV2ChatRequest body) {

        try (BedrockRuntimeClient client = BedrockRuntimeController.client()) {
            String prompt = extractPrompt(body);

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

            String completion = jsonObject.getString("completion");

            return new InvokeClaudeV2ChatResponse(completion);
        }
    }

    private static String extractPrompt(InvokeClaudeV2ChatRequest body) {
        StringBuilder conversationBuilder = new StringBuilder();
        for (ClaudeV2ChatMessage message : body.conversation) {
            conversationBuilder
                    .append(message.sender)
                    .append(": ")
                    .append(message.message)
                    .append("\n\n");
        }

        return conversationBuilder.toString().trim();
    }

    public record InvokeClaudeV2ChatRequest(List<ClaudeV2ChatMessage> conversation) { }

    public record InvokeClaudeV2ChatResponse(String text) { }

    public record ClaudeV2ChatMessage(String sender, String message) { }
}
