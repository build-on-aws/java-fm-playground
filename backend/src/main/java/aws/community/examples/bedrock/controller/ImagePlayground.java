package aws.community.examples.bedrock.controller;

import aws.community.examples.bedrock.models.StableDiffusion;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelRequest;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelResponse;

import java.util.List;

@RestController
public class ImagePlayground {

    @PostMapping ("/foundation-models/model/image/{modelId}/invoke")
    public StableDiffusion.Response invoke(@PathVariable String modelId, @RequestBody StableDiffusion.Request body) {

        return switch (modelId) {
            case "stability.stable-diffusion-xl" -> StableDiffusion.invoke(body, client);
            default -> null;
        };
    }

    private final BedrockRuntimeClient client;

    @Autowired
    public ImagePlayground(final BedrockRuntimeClient client) {
        this.client = client;
    }

}
