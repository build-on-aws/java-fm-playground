package aws.community.examples.bedrock.resources;

import software.amazon.awscdk.RemovalPolicy;
import software.amazon.awscdk.services.apigatewayv2.alpha.HttpApi;
import software.amazon.awscdk.services.apigatewayv2.alpha.HttpApiProps;
import software.amazon.awscdk.services.cloudfront.*;
import software.amazon.awscdk.services.cloudfront.origins.S3Origin;
import software.amazon.awscdk.services.cloudfront.origins.S3OriginProps;
import software.amazon.awscdk.services.s3.BlockPublicAccess;
import software.amazon.awscdk.services.s3.Bucket;
import software.amazon.awscdk.services.s3.BucketEncryption;
import software.amazon.awscdk.services.s3.BucketProps;
import software.amazon.awscdk.services.s3.deployment.BucketDeployment;
import software.amazon.awscdk.services.s3.deployment.BucketDeploymentProps;
import software.amazon.awscdk.services.s3.deployment.Source;
import software.constructs.Construct;

import java.util.List;

public class Deployment {
    private final Construct scope;
    public String url;

    public Deployment(Construct scope) {
        this.scope = scope;
    }

    public Deployment withApi() {
        HttpApi api = new HttpApi(scope, "api", HttpApiProps.builder()
                .apiName("java-fm-playground-api")
                .build()
        );

        api.addRoutes(Routes.listFoundationModels(scope));
        api.addRoutes(Routes.getFoundationModel(scope));

        return this;
    }

    public Deployment withFrontend() {
        Bucket frontendBucket = deployFrontendBucket();
        deployCloudFrontDistribution(frontendBucket);
        return this;
    }

    private Bucket deployFrontendBucket() {
        Bucket bucket = new Bucket(scope, "FrontendBucket", BucketProps.builder()
                .encryption(BucketEncryption.S3_MANAGED)
                .blockPublicAccess(BlockPublicAccess.BLOCK_ALL)
                .removalPolicy(RemovalPolicy.DESTROY)
                .build()
        );

        BucketDeployment frontend = new BucketDeployment(scope, "FrontendDeployment", BucketDeploymentProps.builder()
                .sources(List.of(Source.asset("../frontend")))
                .destinationBucket(bucket)
                .build()
        );
        return bucket;
    }

    private void deployCloudFrontDistribution(Bucket bucket) {
        OriginAccessIdentity oai = new OriginAccessIdentity(scope, "OriginAccessControl", OriginAccessIdentityProps.builder()
                .comment("Java FM Playground OAI")
                .build()
        );

        S3Origin origin = new S3Origin(bucket, S3OriginProps.builder()
                .originAccessIdentity(oai)
                .build()
        );

        Distribution distribution = new Distribution(scope, "CloudFrontDistribution", DistributionProps.builder()
                .defaultBehavior(BehaviorOptions.builder()
                        .origin(origin)
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

        this.url = distribution.getDistributionDomainName();
    }

}
