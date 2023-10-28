package aws.community.examples.bedrock.stacks;

import aws.community.examples.bedrock.configuration.CustomAssetOptions;
import aws.community.examples.bedrock.configuration.Routes;
import software.amazon.awscdk.*;
import software.amazon.awscdk.services.apigatewayv2.alpha.CorsPreflightOptions;
import software.amazon.awscdk.services.apigatewayv2.alpha.HttpApi;
import software.amazon.awscdk.services.apigatewayv2.alpha.HttpApiProps;
import software.amazon.awscdk.services.cloudfront.*;
import software.amazon.awscdk.services.cloudfront.origins.S3Origin;
import software.amazon.awscdk.services.cloudfront.origins.S3OriginProps;
import software.amazon.awscdk.services.s3.BlockPublicAccess;
import software.amazon.awscdk.services.s3.Bucket;
import software.amazon.awscdk.services.s3.BucketEncryption;
import software.amazon.awscdk.services.s3.BucketProps;
import software.amazon.awscdk.services.s3.assets.AssetOptions;
import software.constructs.Construct;

import java.util.List;


public class BackendStack extends Stack {
    public BackendStack(final App parent, final String id) {
        this(parent, id, null);
    }

    public BackendStack(final Construct parent, final String id, final StackProps props) {
        super(parent, id, props);

        HttpApi api = createApi();

        AssetOptions assetOptions = CustomAssetOptions.prepare();
        api.addRoutes(Routes.listFoundationModels(this, assetOptions));
        api.addRoutes(Routes.getFoundationModel(this, assetOptions));

        Bucket frontendBucket = deployFrontendBucket();
        Distribution distribution = deployCloudFrontDistribution(frontendBucket);

        new CfnOutput(this, "ApiBaseUrl", CfnOutputProps.builder()
                .exportName("ApiBaseUrl")
                .value(api.getUrl())
                .build()
        );

        new CfnOutput(this, "FrontendBucketArn", CfnOutputProps.builder()
                .exportName("FrontendBucketArn")
                .value(frontendBucket.getBucketArn())
                .build()
        );

        new CfnOutput(this, "FrontendUrl", CfnOutputProps.builder()
                .exportName("FrontendUrl")
                .value(distribution.getDistributionDomainName())
                .build());
    }

    private HttpApi createApi() {
        return new HttpApi(this, "Api", HttpApiProps.builder()
                .apiName("java-fm-playground-api")
                .corsPreflight(CorsPreflightOptions.builder()
                        .allowOrigins(List.of("*"))
                        .build()
                )
                .build()
        );
    }

    private Bucket deployFrontendBucket() {
        return new Bucket(this, "FrontendBucket", BucketProps.builder()
                .encryption(BucketEncryption.S3_MANAGED)
                .blockPublicAccess(BlockPublicAccess.BLOCK_ALL)
                .removalPolicy(RemovalPolicy.RETAIN)
                .build()
        );
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
