package aws.community.examples.bedrock.models;

import org.json.JSONObject;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelRequest;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelResponse;

public class Claude {
    public enum Type {
        CHAT,
        TEXT
    }

    public static Response invoke(Request body, String modelId, Type type, BedrockRuntimeClient client) {

        String systemPrompt = "";
        if (type ==Type.CHAT) {
            systemPrompt =
                """
                Take the role of a friendly chat bot. Your responses are brief.
                You sometimes use emojis where appropriate, but you don't overdo it.
                You engage human in a dialog by regularly asking questions,
                except when Human indicates that the conversation is over.
                """;
        }

        JSONObject jsonBody = new JSONObject()
                .put("prompt", "Human: " + systemPrompt + " " + body.prompt() + " Assistant:")
                .put("temperature", extractTemperature(body))
                .put("max_tokens_to_sample", extractMaxTokens(body));

        InvokeModelRequest request = InvokeModelRequest.builder()
                .modelId(modelId)
                .body(SdkBytes.fromUtf8String(jsonBody.toString()))
                .build();

        InvokeModelResponse response = client.invokeModel(request);

        String completion = new JSONObject(response.body().asUtf8String())
                .getString("completion");

        return new Response(completion);
    }

    private static int extractMaxTokens(Request body) {
        int maxTokens = 300;
        
        if (body.maxTokens != null) {
            if (body.maxTokens > 2048) {
                maxTokens = 2048;
            } else if (body.maxTokens < 85) {
                maxTokens = 85;
            } else {
                maxTokens = body.maxTokens;
            }
        }
        
        return maxTokens;
    }

    private static double extractTemperature(Request body) {
        double temperature = 0.8;

        if (body.temperature != null) {
            if (body.temperature > 2) {
                temperature = 2;
            } else if (body.temperature < 0) {
                temperature = 0;
            } else {
                temperature = body.temperature;
            }
        }
        return temperature;
    }

    public record Request(String prompt, Double temperature, Integer maxTokens) { }
    public record Response(String completion) { }
}
