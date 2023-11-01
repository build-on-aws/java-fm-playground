package aws.community.examples.bedrock;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@CrossOrigin(origins = "*")
public class InvokeClaudeV2 extends BedrockRuntimeController {

    @PostMapping("/foundation-models/model/claudev2/invoke")
    public ClaudeV2InvocationResponse invoke(@RequestBody ClaudeV2ChatRequest body) {

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

            return new ClaudeV2InvocationResponse(completion);
        }
    }

    private static String extractPrompt(ClaudeV2ChatRequest body) {
        StringBuilder conversationBuilder = new StringBuilder();
        for (ChatMessage message : body.conversation) {
            conversationBuilder
                    .append(message.sender)
                    .append(": ")
                    .append(message.message)
                    .append("\n\n");
        }

        return conversationBuilder.toString().trim();
    }

    public record ClaudeV2InvocationResponse(String text) { }

    public record ChatMessage(String sender, String message) { }

    public record ClaudeV2ChatRequest(List<ChatMessage> conversation) { }
}
