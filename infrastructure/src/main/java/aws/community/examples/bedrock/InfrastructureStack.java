package aws.community.examples.bedrock;

import software.amazon.awscdk.*;
import software.amazon.awscdk.services.iam.Effect;
import software.amazon.awscdk.services.iam.Policy;
import software.amazon.awscdk.services.iam.PolicyProps;
import software.amazon.awscdk.services.iam.PolicyStatement;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.FunctionProps;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.s3.assets.AssetOptions;
import software.constructs.Construct;

import java.util.Arrays;
import java.util.List;

import static java.util.Collections.singletonList;
import static software.amazon.awscdk.BundlingOutput.ARCHIVED;

public class InfrastructureStack extends Stack {
    public InfrastructureStack(final App parent, final String id) {
        this(parent, id, null);
    }

    public InfrastructureStack(final Construct parent, final String id, final StackProps props) {
        super(parent, id, props);

        // Mount local .m2 inside the container to avoid repeat downloads
        List<DockerVolume> volume = singletonList(
                DockerVolume.builder()
                        .hostPath(System.getProperty("user.home") + "/.m2/")
                        .containerPath("/root/.m2/")
                        .build()
        );

        List<String> functionOnePackagingInstructions = Arrays.asList(
                "/bin/sh",
                "-c",
                "cd Bedrock && mvn clean install " +
                "&& cp /asset-input/Bedrock/target/bedrock.jar /asset-output/"
        );

        BundlingOptions bundlingOptions = BundlingOptions.builder()
                .command(functionOnePackagingInstructions)
                .image(Runtime.JAVA_11.getBundlingImage())
                .user("root")
                .outputType(ARCHIVED)
                .volumes(volume)
                .build();

        AssetOptions functionOneBuilderOptions = AssetOptions.builder()
                .bundling(bundlingOptions)
                .build();


        Function bedrockHandler = new Function(this, "BedrockHandler", FunctionProps.builder()
                .runtime(Runtime.JAVA_11)
                .code(Code.fromAsset("../application/", functionOneBuilderOptions))
                .handler("aws.community.examples.bedrock.BedrockHandler")
                .memorySize(512)
                .timeout(Duration.minutes(1))
                .build());

        PolicyStatement bedrockPermissions = PolicyStatement.Builder.create()
                .effect(Effect.ALLOW)
                .actions(List.of("bedrock:ListFoundationModels"))
                .resources(List.of("*"))
                .build();

        bedrockHandler.addToRolePolicy(bedrockPermissions);
    }
}
