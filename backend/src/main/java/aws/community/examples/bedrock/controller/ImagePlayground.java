package aws.community.examples.bedrock.controller;

import aws.community.examples.bedrock.aimodels.StableDiffusion;
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

    @PostMapping ("/foundation-aimodels/model/image/stability.stable-diffusion-xl/invoke")
    public StableDiffusion.Response invoke(@RequestBody StableDiffusion.Request body) {

        return StableDiffusion.invoke(client, body.prompt(), body.stylePreset());

    }
}
