package aws.community.examples.bedrock.controller;

import aws.community.examples.bedrock.models.StableDiffusion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;

@RestController
public class ImagePlayground {

    private final BedrockRuntimeClient client;

    @Autowired
    public ImagePlayground(final BedrockRuntimeClient client) {
        this.client = client;
    }

    @PostMapping ("/foundation-models/model/image/{modelId}/invoke")
    public StableDiffusion.Response invoke(@PathVariable String modelId, @RequestBody StableDiffusion.Request body) {

        return switch (modelId) {
            case "stability.stable-diffusion-xl" -> StableDiffusion.invoke(body, client);
            default -> null;
        };
    }
}
