package aws.community.examples.bedrock;

import aws.community.examples.bedrock.resources.Deployment;
import software.amazon.awscdk.*;
import software.constructs.Construct;

public class InfrastructureStack extends Stack {
    public InfrastructureStack(final App parent, final String id) {
        this(parent, id, null);
    }

    public InfrastructureStack(final Construct parent, final String id, final StackProps props) {
        super(parent, id, props);

        Deployment deployment = new Deployment(this).withApi().withFrontend();

        CfnOutput output = new CfnOutput(this, "URL", CfnOutputProps.builder()
                .value(deployment.url)
                .build());
    }
}
