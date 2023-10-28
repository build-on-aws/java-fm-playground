package aws.community.examples.bedrock;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.json.JSONObject;
import software.amazon.awssdk.services.bedrock.BedrockClient;
import software.amazon.awssdk.services.bedrock.model.FoundationModelDetails;
import software.amazon.awssdk.services.bedrock.model.GetFoundationModelRequest;
import software.amazon.awssdk.services.bedrock.model.GetFoundationModelResponse;
import software.amazon.awssdk.services.bedrock.model.ResourceNotFoundException;

public class GetFoundationModel implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, Context context) {

        String modelId = event.getPathParameters().get("model");

        try (BedrockClient bedrockClient = BedrockClient.builder().build()) {
            GetFoundationModelRequest request = GetFoundationModelRequest.builder()
                    .modelIdentifier(modelId)
                    .build();

            GetFoundationModelResponse response;

            response = bedrockClient.getFoundationModel(request);

            FoundationModelDetails model = response.modelDetails();

            JSONObject body = new JSONObject()
                    .put("modelId", model.modelId())
                    .put("providerName", model.providerName())
                    .put("modelName", model.modelName());

            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(200)
                    .withBody(body.toString());

        } catch (Exception e) {
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(500)
                    .withBody(e.getMessage());
        }
    }
}
