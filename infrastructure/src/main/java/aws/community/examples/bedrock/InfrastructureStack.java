package aws.community.examples.bedrock;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.lambda.Function;
import software.constructs.Construct;

public class InfrastructureStack extends Stack {
    public InfrastructureStack(final App parent, final String id) {
        this(parent, id, null);
    }

    public InfrastructureStack(final Construct parent, final String id, final StackProps props) {
        super(parent, id, props);

        Function bedrockHandler = BedrockHandlerFunction.create(this);

    }
}
