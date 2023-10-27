package aws.community.examples.bedrock;

import aws.community.examples.bedrock.resources.Routes;
import software.amazon.awscdk.App;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.apigatewayv2.alpha.HttpApi;
import software.amazon.awscdk.services.apigatewayv2.alpha.HttpApiProps;
import software.constructs.Construct;

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

        api.addRoutes(Routes.listFoundationModels(this));
        api.addRoutes(Routes.getFoundationModel(this));
    }
}
