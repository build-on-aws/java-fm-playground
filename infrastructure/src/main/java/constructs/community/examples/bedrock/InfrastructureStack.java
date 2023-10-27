package constructs.community.examples.bedrock;

import constructs.community.examples.bedrock.constructs.GetFoundationModel;
import constructs.community.examples.bedrock.constructs.ListFoundationModels;
import software.amazon.awscdk.App;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.apigatewayv2.alpha.*;
import software.amazon.awscdk.services.apigatewayv2.integrations.alpha.HttpLambdaIntegration;
import software.amazon.awscdk.services.apigatewayv2.integrations.alpha.HttpLambdaIntegrationProps;
import software.amazon.awscdk.services.lambda.Function;
import software.constructs.Construct;

import java.util.List;

public class InfrastructureStack extends Stack {
    public InfrastructureStack(final App parent, final String id) {
        this(parent, id, null);
    }

    public InfrastructureStack(final Construct parent, final String id, final StackProps props) {
        super(parent, id, props);

        HttpApi api = new HttpApi(this, "api", HttpApiProps.builder()
                .apiName("java-fm-playground-api")
                .build()
        );

        Function listFoundationModels = ListFoundationModels.create(this);

        HttpLambdaIntegration listFoundationModelsIntegration = new HttpLambdaIntegration(
                "listFoundationModels", listFoundationModels,
                HttpLambdaIntegrationProps.builder()
                        .payloadFormatVersion(PayloadFormatVersion.VERSION_2_0)
                        .build()
        );

        api.addRoutes(AddRoutesOptions.builder()
                .path("/foundation-models")
                .methods(List.of(HttpMethod.GET))
                .integration(listFoundationModelsIntegration)
                .build()
        );

        Function getFoundationModels = GetFoundationModel.create(this);

        HttpLambdaIntegration getFoundationModelIntegration = new HttpLambdaIntegration(
                "getFoundationModels", getFoundationModels,
                HttpLambdaIntegrationProps.builder()
                        .payloadFormatVersion(PayloadFormatVersion.VERSION_2_0)
                        .build()
        );

        api.addRoutes(AddRoutesOptions.builder()
                .path("/foundation-models/{model}")
                .methods(List.of(HttpMethod.GET))
                .integration(getFoundationModelIntegration)
                .build()
        );
    }
}
