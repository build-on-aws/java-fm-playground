package aws.community.examples.bedrock.aimodels;

import org.json.JSONObject;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelRequest;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelResponse;

public class Jurassic2 {
    public static final String MODEL_ID = "ai21.j2-mid-v1";

    public static String invoke(BedrockRuntimeClient client, String prompt, double temperature, int maxTokens) {
        JSONObject jsonBody = new JSONObject()
                .put("prompt", prompt)
                .put("temperature", temperature)
                .put("maxTokens", maxTokens);

        InvokeModelRequest request = InvokeModelRequest.builder()
                .modelId(MODEL_ID)
                .body(SdkBytes.fromUtf8String(jsonBody.toString()))
                .build();

        InvokeModelResponse response = client.invokeModel(request);

        String completion = new JSONObject(response.body().asUtf8String())
                .getJSONArray("completions")
                .getJSONObject(0)
                .getJSONObject("data")
                .getString("text");

        if (completion.startsWith("\n")) {
            completion = completion.substring(1);
        }

        return completion;
    }
}
