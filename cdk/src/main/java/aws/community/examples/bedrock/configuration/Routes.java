package aws.community.examples.bedrock.configuration;

import aws.community.examples.bedrock.resources.GetFoundationModel;
import aws.community.examples.bedrock.resources.ListFoundationModels;
import software.amazon.awscdk.services.apigatewayv2.alpha.AddRoutesOptions;
import software.amazon.awscdk.services.apigatewayv2.alpha.HttpMethod;
import software.amazon.awscdk.services.apigatewayv2.alpha.PayloadFormatVersion;
import software.amazon.awscdk.services.apigatewayv2.integrations.alpha.HttpLambdaIntegration;
import software.amazon.awscdk.services.apigatewayv2.integrations.alpha.HttpLambdaIntegrationProps;
import software.amazon.awscdk.services.lambda.Alias;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.s3.assets.AssetOptions;
import software.constructs.Construct;

import java.util.List;

public class Routes {
    public static AddRoutesOptions listFoundationModels(Construct scope, AssetOptions assetOptions) {
        String path = "/foundation-models";
        HttpMethod httpMethod = HttpMethod.GET;

        Alias function = ListFoundationModels.create(scope, assetOptions);

        HttpLambdaIntegration listFoundationModelsIntegration = new HttpLambdaIntegration(
                "listFoundationModels", function,
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

    public static AddRoutesOptions getFoundationModel(Construct scope, AssetOptions assetOptions) {
        String path = "/foundation-models/{model}";
        HttpMethod httpMethod = HttpMethod.GET;

        Alias function = GetFoundationModel.create(scope, assetOptions);

        HttpLambdaIntegration getFoundationModelIntegration = new HttpLambdaIntegration(
                "getFoundationModels", function,
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
