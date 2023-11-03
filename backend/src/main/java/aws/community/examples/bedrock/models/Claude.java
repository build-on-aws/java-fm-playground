package aws.community.examples.bedrock.models;

import org.json.JSONObject;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelRequest;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelResponse;

import java.nio.charset.StandardCharsets;

public class Claude {
    public static String invoke(Llm.ChatRequest body, BedrockRuntimeClient client) {

        JSONObject jsonBody = new JSONObject()
                .put("prompt", "Human: " + body.prompt() + " Assistant:")
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
}
