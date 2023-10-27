package aws.community.examples.bedrock;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.json.JSONArray;
import org.json.JSONObject;
import software.amazon.awssdk.services.bedrock.BedrockClient;
import software.amazon.awssdk.services.bedrock.model.*;

import java.util.List;

public class BedrockHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    public APIGatewayProxyResponseEvent handleRequest(
            final APIGatewayProxyRequestEvent input,
            final Context context
    ) {

        try (BedrockClient bedrockClient = BedrockClient.builder().build()) {
            ListFoundationModelsRequest request = ListFoundationModelsRequest.builder().build();
            ListFoundationModelsResponse response = bedrockClient.listFoundationModels(request);
            List<FoundationModelSummary> models = response.modelSummaries();

            JSONArray array = new JSONArray();

            for (FoundationModelSummary model : models) {
                array.put(new JSONObject()
                        .put("modelId", model.modelId())
                );
            }
            JSONObject body = new JSONObject().put("foundationModels", array);

            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(200)
                    .withBody(body.toString());
        }
    }
}