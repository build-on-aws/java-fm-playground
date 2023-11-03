package aws.community.examples.bedrock.models;

import org.json.JSONObject;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelRequest;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelResponse;

public class Claude {
    public static Response invoke(Request body, String modelId, BedrockRuntimeClient client) {

        JSONObject jsonBody = new JSONObject()
                .put("prompt", "Human: " + body.prompt() + " Assistant:")
                .put("temperature", 0.8)
                .put("max_tokens_to_sample", 1024);

        InvokeModelRequest request = InvokeModelRequest.builder()
                .modelId(modelId)
                .body(SdkBytes.fromUtf8String(jsonBody.toString()))
                .build();

        InvokeModelResponse response = client.invokeModel(request);

        String completion = new JSONObject(response.body().asUtf8String())
                .getString("completion");

        return new Response(completion);
    }

    public record Request(String prompt) { }
    public record Response(String completion) { }
}
