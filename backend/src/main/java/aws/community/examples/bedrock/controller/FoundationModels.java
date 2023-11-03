package aws.community.examples.bedrock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.bedrock.BedrockClient;
import software.amazon.awssdk.services.bedrock.model.*;

import java.util.List;

@RestController
public class FoundationModels {

    private final BedrockClient client;

    @Autowired
    public FoundationModels(final BedrockClient client) {
        this.client = client;
    }

    @GetMapping("/foundation-models")
    public List<FoundationModelListItem> listFoundationModels() {


        ListFoundationModelsRequest request = ListFoundationModelsRequest.builder().build();
        ListFoundationModelsResponse response = client.listFoundationModels(request);

        return response.modelSummaries().stream().map(
                model -> new FoundationModelListItem(
                        model.modelId(),
                        model.modelName(),
                        model.providerName()
                )
        ).toList();
    }

    public record FoundationModelListItem(String modelId, String modelName, String providerName) { }

    @GetMapping("/foundation-models/model")
    public getFoundationModel getFoundationModel(@RequestParam(value = "id") String id) {

        GetFoundationModelRequest request = GetFoundationModelRequest.builder()
                .modelIdentifier(id)
                .build();
        GetFoundationModelResponse response = client.getFoundationModel(request);

        FoundationModelDetails model = response.modelDetails();

        return new getFoundationModel(
                model.modelArn(),
                model.modelId(),
                model.modelName(),
                model.providerName(),
                String.join(", ", model.customizationsSupportedAsStrings()),
                String.join(", ", model.outputModalitiesAsStrings())
        );
    }

    public record getFoundationModel(
            String modelArn,
            String modelId,
            String modelName,
            String providerName,
            String customizationsSupported,
            String outputModalities
    ) { }
}
