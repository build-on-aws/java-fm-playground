package aws.community.examples.bedrock.aimodels;

import org.json.JSONObject;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelRequest;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelResponse;

public class Claude {
    private static final String MODEL_ID = "anthropic.claude-v2";

    public static String invoke(BedrockRuntimeClient client, String prompt, double temperature, int maxTokens) {
        JSONObject jsonBody = new JSONObject()
                .put("prompt", "Human: " + prompt + " Assistant:")
                .put("temperature", temperature)
                .put("max_tokens_to_sample", maxTokens);

        InvokeModelRequest request = InvokeModelRequest.builder()
                .modelId(MODEL_ID)
                .body(SdkBytes.fromUtf8String(jsonBody.toString()))
                .build();

        InvokeModelResponse response = client.invokeModel(request);

        String completion = new JSONObject(response.body().asUtf8String())
                .getString("completion");

        return completion;
    }
}
