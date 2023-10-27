package aws.community.examples.bedrock.resources;

import software.amazon.awscdk.Duration;
import software.amazon.awscdk.services.iam.Effect;
import software.amazon.awscdk.services.iam.PolicyStatement;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.FunctionProps;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.s3.assets.AssetOptions;
import software.constructs.Construct;

import java.util.List;

public class ListFoundationModels  {
    public static Function create(Construct scope, AssetOptions assetOptions) {

        Function listFoundationModels = new Function(scope, "ListFoundationModels", FunctionProps.builder()
                .runtime(Runtime.JAVA_11)
                .code(Code.fromAsset("../application/", assetOptions))
                .handler("aws.community.examples.bedrock.ListFoundationModels")
                .memorySize(512)
                .timeout(Duration.minutes(1))
                .build());

        PolicyStatement bedrockPermissions = PolicyStatement.Builder.create()
                .effect(Effect.ALLOW)
                .actions(List.of("bedrock:ListFoundationModels"))
                .resources(List.of("*"))
                .build();

        listFoundationModels.addToRolePolicy(bedrockPermissions);

        return listFoundationModels;
    }
}
