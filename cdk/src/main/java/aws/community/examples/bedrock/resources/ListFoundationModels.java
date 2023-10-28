package aws.community.examples.bedrock.resources;

import software.amazon.awscdk.Duration;
import software.amazon.awscdk.services.iam.Effect;
import software.amazon.awscdk.services.iam.PolicyStatement;
import software.amazon.awscdk.services.lambda.*;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.s3.assets.AssetOptions;
import software.constructs.Construct;

import java.util.List;

public class ListFoundationModels  {
    public static Alias create(Construct scope, AssetOptions assetOptions) {

        Function function = new Function(scope, "ListFoundationModels", FunctionProps.builder()
                .runtime(Runtime.JAVA_11)
                .code(Code.fromAsset("../backend/", assetOptions))
                .handler("aws.community.examples.bedrock.ListFoundationModels")
                .memorySize(512)
                .timeout(Duration.minutes(1))
                .snapStart(SnapStartConf.ON_PUBLISHED_VERSIONS)
                .build()
        );

        PolicyStatement bedrockPermissions = PolicyStatement.Builder.create()
                .effect(Effect.ALLOW)
                .actions(List.of("bedrock:ListFoundationModels"))
                .resources(List.of("*"))
                .build();

        function.addToRolePolicy(bedrockPermissions);

        Version version = new Version(scope, "ListFoundationModelsVersion", VersionProps.builder()
                .lambda(function)
                .build()
        );

        return new Alias(scope, "ListFoundationModelsAlias", AliasProps.builder()
                .aliasName("ListFoundationModels")
                .version(version)
                .build()
        );
    }
}
