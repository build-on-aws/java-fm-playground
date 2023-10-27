package constructs.community.examples.bedrock.constructs;

import software.amazon.awscdk.BundlingOptions;
import software.amazon.awscdk.DockerVolume;
import software.amazon.awscdk.Duration;
import software.amazon.awscdk.Stack;

import java.util.Arrays;
import java.util.List;

import software.amazon.awscdk.services.iam.Effect;
import software.amazon.awscdk.services.iam.PolicyStatement;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.FunctionProps;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.s3.assets.AssetOptions;

import static java.util.Collections.singletonList;
import static software.amazon.awscdk.BundlingOutput.ARCHIVED;

public class GetFoundationModel {
    public static Function create(Stack scope) {
        // Mount local .m2 inside the container to avoid repeat downloads
        List<DockerVolume> volume = singletonList(
                DockerVolume.builder()
                        .hostPath(System.getProperty("user.home") + "/.m2/")
                        .containerPath("/root/.m2/")
                        .build()
        );

        List<String> packagingInstructions = Arrays.asList(
                "/bin/sh",
                "-c",
                "cd Bedrock && mvn clean install " +
                "&& cp /asset-input/Bedrock/target/bedrock.jar /asset-output/"
        );

        BundlingOptions bundlingOptions = BundlingOptions.builder()
                .command(packagingInstructions)
                .image(Runtime.JAVA_11.getBundlingImage())
                .user("root")
                .outputType(ARCHIVED)
                .volumes(volume)
                .build();

        AssetOptions assetOptions = AssetOptions.builder()
                .bundling(bundlingOptions)
                .build();

        Function getFoundationModel = new Function(scope, "GetFoundationModel", FunctionProps.builder()
                .runtime(Runtime.JAVA_11)
                .code(Code.fromAsset("../application/", assetOptions))
                .handler("aws.community.examples.bedrock.GetFoundationModel")
                .memorySize(512)
                .timeout(Duration.minutes(1))
                .build());

        PolicyStatement bedrockPermissions = PolicyStatement.Builder.create()
                .effect(Effect.ALLOW)
                .actions(List.of("bedrock:GetFoundationModel"))
                .resources(List.of("*"))
                .build();

        getFoundationModel.addToRolePolicy(bedrockPermissions);

        return getFoundationModel;
    }
}
