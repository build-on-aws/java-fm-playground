package constructs.community.examples.bedrock;

import constructs.community.examples.bedrock.constructs.BedrockHandlerFunction;
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

        Function bedrockHandler = BedrockHandlerFunction.create(this);

        HttpApi api = new HttpApi(this, "api", HttpApiProps.builder()
                .apiName("java-fm-playground-api")
                .build()
        );

        api.addRoutes(
                AddRoutesOptions.builder()
                        .path("/foundation-models")
                        .methods(List.of(HttpMethod.GET))
                        .integration(new HttpLambdaIntegration(
                                "bedrockHandler", bedrockHandler,
                                HttpLambdaIntegrationProps.builder()
                                        .payloadFormatVersion(PayloadFormatVersion.VERSION_2_0)
                                        .build()
                        ))
                        .build()
        );
    }
}
