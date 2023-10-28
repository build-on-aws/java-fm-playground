package aws.community.examples.bedrock.resources;

import software.amazon.awscdk.Duration;
import software.amazon.awscdk.services.iam.Effect;
import software.amazon.awscdk.services.iam.PolicyStatement;
import software.amazon.awscdk.services.lambda.*;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.s3.assets.AssetOptions;
import software.constructs.Construct;

import java.util.List;

public class GetFoundationModel {
    public static Function create(Construct scope, AssetOptions assetOptions) {

        Function getFoundationModel = new Function(scope, "GetFoundationModel", FunctionProps.builder()
                .runtime(Runtime.JAVA_11)
                .code(Code.fromAsset("../application/", assetOptions))
                .handler("aws.community.examples.bedrock.GetFoundationModel")
                .memorySize(512)
                .timeout(Duration.minutes(1))
                .snapStart(SnapStartConf.ON_PUBLISHED_VERSIONS)
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
