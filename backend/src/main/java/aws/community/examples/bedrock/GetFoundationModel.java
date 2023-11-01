package aws.community.examples.bedrock;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.bedrock.BedrockClient;
import software.amazon.awssdk.services.bedrock.model.FoundationModelDetails;
import software.amazon.awssdk.services.bedrock.model.GetFoundationModelRequest;
import software.amazon.awssdk.services.bedrock.model.GetFoundationModelResponse;

@RestController
@CrossOrigin(origins = "*")
public class GetFoundationModel extends BedrockController {

    @GetMapping("/foundation-models/model")
    public getFoundationModel getFoundationModel(@RequestParam(value = "id") String id) {

        try (BedrockClient bedrock = BedrockController.client()) {

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