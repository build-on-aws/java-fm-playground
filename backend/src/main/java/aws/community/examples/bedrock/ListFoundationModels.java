package aws.community.examples.bedrock;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.bedrock.BedrockClient;
import software.amazon.awssdk.services.bedrock.model.*;

import java.util.List;

@RestController
public class ListFoundationModels {
    static String awsRegion;

    @Value("${awsRegion}")
    public void setAwsRegion(String awsRegion) {
        ListFoundationModels.awsRegion = awsRegion;
    }

    static BedrockClient client() {
        return BedrockClient.builder()
                .region(Region.of(ListFoundationModels.awsRegion))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    @GetMapping("/foundation-models")
    public List<FoundationModelListItem> listFoundationModels() {

        try (BedrockClient bedrock = ListFoundationModels.client()) {

            ListFoundationModelsRequest request = ListFoundationModelsRequest.builder().build();
            ListFoundationModelsResponse response = bedrock.listFoundationModels(request);

            return response.modelSummaries().stream().map(
                    model -> new FoundationModelListItem(
                            model.modelId(),
                            model.modelName(),
                            model.providerName()
                    )
            ).toList();
        }
    }

    public record FoundationModelListItem(String modelId, String modelName, String providerName) { }

    @GetMapping("/foundation-models/model")
    public getFoundationModel getFoundationModel(@RequestParam(value = "id") String id) {

        try (BedrockClient bedrock = ListFoundationModels.client()) {

            GetFoundationModelRequest request = GetFoundationModelRequest.builder()
                    .modelIdentifier(id)
                    .build();
            GetFoundationModelResponse response = bedrock.getFoundationModel(request);

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
