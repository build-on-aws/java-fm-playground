package aws.community.examples.bedrock.controller;

import aws.community.examples.bedrock.models.Claude;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;

@RestController
public class TextPlayground {

    private final BedrockRuntimeClient client;

    @Autowired
    public TextPlayground(final BedrockRuntimeClient client) {
        this.client = client;
    }

    @PostMapping("/foundation-models/model/text/{modelId}/invoke")
    public Claude.Response invokeLlm(@PathVariable String modelId, @RequestBody Claude.Request body) {
        return Claude.invoke(client, body.prompt(), extractTemperature(body), extractMaxTokens(body));
    }

    private static int extractMaxTokens(Claude.Request body) {
        int maxTokens = 300;

        if (body.maxTokens() != null) {
            if (body.maxTokens() > 2048) {
                maxTokens = 2048;
            } else if (body.maxTokens() < 85) {
                maxTokens = 85;
            } else {
                maxTokens = body.maxTokens();
            }
        }

        return maxTokens;
    }

    private static double extractTemperature(Claude.Request body) {
        double temperature = 0.8;

        if (body.temperature() != null) {
            if (body.temperature() > 2) {
                temperature = 2;
            } else if (body.temperature() < 0) {
                temperature = 0;
            } else {
                temperature = body.temperature();
            }
        }
        return temperature;
    }
}
