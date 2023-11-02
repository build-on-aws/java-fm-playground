package aws.community.examples.bedrock;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelRequest;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelResponse;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class InvokeStableDiffusion extends BedrockRuntimeController {

    @PostMapping ("/foundation-models/model/stability.stable-diffusion-xl/invoke")
    public InvokeStableDiffusionXlResponse invoke(@RequestBody InvokeStableDiffusionXlRequest body) {
        try (BedrockRuntimeClient client = BedrockRuntimeController.client()) {
            String prompt = body.prompt;

            JSONArray promptsJson = new JSONArray(List.of(new JSONObject().put("text", prompt)));

            JSONObject jsonBody = new JSONObject()
                    .put("text_prompts", promptsJson)
                    .put("cfg_scale", 20)
                    .put("steps", 50);

            SdkBytes sdkBytesBody = SdkBytes.fromUtf8String(jsonBody.toString());

            InvokeModelRequest request = InvokeModelRequest.builder()
                    .modelId("stability.stable-diffusion-xl")
                    .body(sdkBytesBody)
                    .build();

            InvokeModelResponse response = client.invokeModel(request);

            byte[] bytes = new JSONObject(response.body().asUtf8String())
                    .getJSONArray("artifacts")
                    .getJSONObject(0)
                    .get("base64")
                    .toString()
                    .getBytes();

            return new InvokeStableDiffusionXlResponse(bytes);
        }
    }

    public record InvokeStableDiffusionXlRequest(String prompt) { }

    public record InvokeStableDiffusionXlResponse(byte[] imageByteArray) { }
}
