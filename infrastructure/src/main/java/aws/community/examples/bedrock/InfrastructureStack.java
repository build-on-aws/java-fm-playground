package aws.community.examples.bedrock;

import aws.community.examples.bedrock.resources.BedrockFunction;
import aws.community.examples.bedrock.resources.Routes;
import software.amazon.awscdk.*;
import software.amazon.awscdk.services.apigatewayv2.alpha.CorsPreflightOptions;
import software.amazon.awscdk.services.apigatewayv2.alpha.HttpApi;
import software.amazon.awscdk.services.apigatewayv2.alpha.HttpApiProps;
import software.amazon.awscdk.services.cloudfront.*;
import software.amazon.awscdk.services.cloudfront.origins.S3Origin;
import software.amazon.awscdk.services.cloudfront.origins.S3OriginProps;
import software.amazon.awscdk.services.iam.Effect;
import software.amazon.awscdk.services.iam.PolicyStatement;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.s3.BlockPublicAccess;
import software.amazon.awscdk.services.s3.Bucket;
import software.amazon.awscdk.services.s3.BucketEncryption;
import software.amazon.awscdk.services.s3.BucketProps;
import software.amazon.awscdk.services.s3.assets.AssetOptions;
import software.amazon.awscdk.services.s3.deployment.BucketDeployment;
import software.amazon.awscdk.services.s3.deployment.BucketDeploymentProps;
import software.amazon.awscdk.services.s3.deployment.Source;
import software.amazon.awscdk.triggers.TriggerFunction;
import software.amazon.awscdk.triggers.TriggerFunctionProps;
import software.constructs.Construct;

import java.util.List;
import java.util.Map;
import java.util.Objects;


public class InfrastructureStack extends Stack {
    public InfrastructureStack(final App parent, final String id) {
        this(parent, id, null);
    }

    public InfrastructureStack(final Construct parent, final String id, final StackProps props) {
        super(parent, id, props);

        HttpApi api = createApi();

        AssetOptions assetOptions = BedrockFunction.prepareAssetOptions();
        api.addRoutes(Routes.listFoundationModels(this, assetOptions));
        api.addRoutes(Routes.getFoundationModel(this, assetOptions));

        Bucket frontendBucket = deployFrontendBucket();
        Distribution distribution = deployCloudFrontDistribution(frontendBucket);

        injectApiUrl(frontendBucket, api, assetOptions);

        CfnOutput frontendUrl = new CfnOutput(this, "frontendUrl", CfnOutputProps.builder()
                .value(distribution.getDistributionDomainName())
                .build());
    }

    private HttpApi createApi() {
        return new HttpApi(this, "api", HttpApiProps.builder()
                .apiName("java-fm-playground-api")
                .corsPreflight(CorsPreflightOptions.builder()
                        .allowOrigins(List.of("*"))
                        .build()
                )
                .build()
        );
    }

    private void injectApiUrl(Bucket frontendBucket, HttpApi api, AssetOptions assetOptions) {
        TriggerFunction triggerFunction = new TriggerFunction(this, "InjectApiUrlIntoFrontend", TriggerFunctionProps.builder()
                .runtime(Runtime.JAVA_11)
                .code(Code.fromAsset("../application/", assetOptions))
                .handler("aws.community.examples.bedrock.InjectApiUrl")
                .memorySize(512)
                .timeout(Duration.minutes(1))
                .environment(Map.of(
                        "API_URL", (Objects.requireNonNull(api.getUrl())),
                        "FRONTEND_BUCKET", frontendBucket.getBucketName()
                ))
                .executeAfter(List.of(frontendBucket))
                .build()
        );

        PolicyStatement permissions = PolicyStatement.Builder.create()
                .effect(Effect.ALLOW)
                .actions(List.of("s3:PutObject"))
                .resources(List.of(
                        frontendBucket.getBucketArn(),
                        frontendBucket.getBucketArn() + "/*"
                ))
                .build();

        triggerFunction.addToRolePolicy(permissions);
    }

    private Bucket deployFrontendBucket() {
        Bucket bucket = new Bucket(this, "FrontendBucket", BucketProps.builder()
                .encryption(BucketEncryption.S3_MANAGED)
                .blockPublicAccess(BlockPublicAccess.BLOCK_ALL)
                .removalPolicy(RemovalPolicy.DESTROY)
                .build()
        );

        new BucketDeployment(this, "FrontendDeployment", BucketDeploymentProps.builder()
                .sources(List.of(Source.asset("../frontend")))
                .destinationBucket(bucket)
                .build()
        );
        return bucket;
    }

    private Distribution deployCloudFrontDistribution(Bucket bucket) {
        OriginAccessIdentity oai = new OriginAccessIdentity(this, "OriginAccessControl", OriginAccessIdentityProps.builder()
                .comment("Java FM Playground OAI")
                .build()
        );

        S3Origin origin = new S3Origin(bucket, S3OriginProps.builder()
                .originAccessIdentity(oai)
                .build()
        );

        return new Distribution(this, "CloudFrontDistribution", DistributionProps.builder()
                .defaultBehavior(BehaviorOptions.builder()
                        .origin(origin)
                        .cachePolicy(CachePolicy.CACHING_DISABLED)
                        .viewerProtocolPolicy(ViewerProtocolPolicy.ALLOW_ALL)
                        .allowedMethods(AllowedMethods.ALLOW_ALL)
                        .build()
                )
                .errorResponses(List.of(ErrorResponse.builder()
                        .httpStatus(403)
                        .responseHttpStatus(200)
                        .responsePagePath("/index.html")
                        .build()
                ))
                .build()
        );
    }
}
