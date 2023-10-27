package aws.community.examples.bedrock.resources;

import software.amazon.awscdk.services.apigatewayv2.alpha.AddRoutesOptions;
import software.amazon.awscdk.services.apigatewayv2.alpha.HttpMethod;
import software.amazon.awscdk.services.apigatewayv2.alpha.PayloadFormatVersion;
import software.amazon.awscdk.services.apigatewayv2.integrations.alpha.HttpLambdaIntegration;
import software.amazon.awscdk.services.apigatewayv2.integrations.alpha.HttpLambdaIntegrationProps;
import software.amazon.awscdk.services.lambda.Function;
import software.constructs.Construct;

import java.util.List;

public class Routes {
    public static AddRoutesOptions listFoundationModels(Construct scope) {
        String path = "/foundation-models";
        HttpMethod httpMethod = HttpMethod.GET;

        Function listFoundationModels = ListFoundationModels.create(scope);

        HttpLambdaIntegration listFoundationModelsIntegration = new HttpLambdaIntegration(
                "listFoundationModels", listFoundationModels,
                HttpLambdaIntegrationProps.builder()
                        .payloadFormatVersion(PayloadFormatVersion.VERSION_2_0)
                        .build()
        );

        return AddRoutesOptions.builder()
                .path(path)
                .methods(List.of(httpMethod))
                .integration(listFoundationModelsIntegration)
                .build();
    }

    public static AddRoutesOptions getFoundationModel(Construct scope) {
        String path = "/foundation-models/{model}";
        HttpMethod httpMethod = HttpMethod.GET;

        Function getFoundationModels = GetFoundationModel.create(scope);

        HttpLambdaIntegration getFoundationModelIntegration = new HttpLambdaIntegration(
                "getFoundationModels", getFoundationModels,
                HttpLambdaIntegrationProps.builder()
                        .payloadFormatVersion(PayloadFormatVersion.VERSION_2_0)
                        .build()
        );

        return AddRoutesOptions.builder()
                .path(path)
                .methods(List.of(httpMethod))
                .integration(getFoundationModelIntegration)
                .build();
    }
}
