package aws.community.examples.bedrock.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.services.bedrock.BedrockClient;
import software.amazon.awssdk.services.bedrock.model.*;
import software.amazon.awssdk.services.bedrock.model.AccessDeniedException;

import java.util.List;

@RestController
public class FoundationModels {

    private static final Logger logger = LoggerFactory.getLogger(FoundationModels.class);

    private final BedrockClient client;

    @Autowired
    public FoundationModels(final BedrockClient client) {
        this.client = client;
    }

    @GetMapping("/foundation-models")
    public List<FoundationModelListItem> listFoundationModels() {
        try {

        ListFoundationModelsRequest request = ListFoundationModelsRequest.builder().build();
        ListFoundationModelsResponse response = client.listFoundationModels(request);

        return response.modelSummaries().stream().map(
                model -> new FoundationModelListItem(
                        model.modelId(),
                        model.modelName(),
                        model.providerName()
                )
        ).toList();

        } catch (AccessDeniedException e) {
            logger.error("Access Denied: %s".formatted(e.getMessage()));
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            logger.error("Exception: %s".formatted(e.getMessage()));
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public record FoundationModelListItem(String modelId, String modelName, String providerName) { }

    @GetMapping("/foundation-models/model/{modelId}")
    public GetFoundationModel getFoundationModel(@PathVariable String modelId) {
        try {

            GetFoundationModelRequest request = GetFoundationModelRequest.builder()
                    .modelIdentifier(modelId)
                    .build();
            GetFoundationModelResponse response = client.getFoundationModel(request);

            FoundationModelDetails model = response.modelDetails();

            return new GetFoundationModel(
                    model.modelArn(),
                    model.modelId(),
                    model.modelName(),
                    model.providerName(),
                    String.join(", ", model.customizationsSupportedAsStrings()),
                    String.join(", ", model.outputModalitiesAsStrings())
            );

        } catch (AccessDeniedException e) {
            logger.error("Access Denied: %s".formatted(e.getMessage()));
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            logger.error("Exception: %s".formatted(e.getMessage()));
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public record GetFoundationModel(
            String modelArn,
            String modelId,
            String modelName,
            String providerName,
            String customizationsSupported,
            String outputModalities
    ) { }
}
