package aws.community.examples.bedrock;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.bedrock.BedrockClient;
import software.amazon.awssdk.services.bedrock.model.ListFoundationModelsRequest;
import software.amazon.awssdk.services.bedrock.model.ListFoundationModelsResponse;

import java.util.List;

@RestController
public class ListFoundationModels extends BedrockController {

    @GetMapping("/foundation-models")
    public List<FoundationModelListItem> listFoundationModels() {

        try (BedrockClient bedrock = BedrockController.client()) {

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
}
