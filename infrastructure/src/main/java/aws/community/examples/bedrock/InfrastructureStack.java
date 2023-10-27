package aws.community.examples.bedrock;

import aws.community.examples.bedrock.resources.Routes;
import software.amazon.awscdk.App;
import software.amazon.awscdk.RemovalPolicy;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.apigatewayv2.alpha.HttpApi;
import software.amazon.awscdk.services.apigatewayv2.alpha.HttpApiProps;
import software.amazon.awscdk.services.s3.BlockPublicAccess;
import software.amazon.awscdk.services.s3.Bucket;
import software.amazon.awscdk.services.s3.BucketEncryption;
import software.amazon.awscdk.services.s3.BucketProps;
import software.amazon.awscdk.services.s3.deployment.BucketDeployment;
import software.amazon.awscdk.services.s3.deployment.BucketDeploymentProps;
import software.amazon.awscdk.services.s3.deployment.Source;
import software.constructs.Construct;

import java.util.List;

public class InfrastructureStack extends Stack {
    public InfrastructureStack(final App parent, final String id) {
        this(parent, id, null);
    }

    public InfrastructureStack(final Construct parent, final String id, final StackProps props) {
        super(parent, id, props);

        HttpApi api = new HttpApi(this, "api", HttpApiProps.builder()
                .apiName("java-fm-playground-api")
                .build()
        );

        api.addRoutes(Routes.listFoundationModels(this));
        api.addRoutes(Routes.getFoundationModel(this));

        Bucket bucket = new Bucket(this, "FrontendBucket", BucketProps.builder()
                .encryption(BucketEncryption.S3_MANAGED)
                .blockPublicAccess(BlockPublicAccess.BLOCK_ALL)
                .removalPolicy(RemovalPolicy.DESTROY)
                .build());

        BucketDeployment frontend = new BucketDeployment(this, "FrontendDeployment", BucketDeploymentProps.builder()
                .sources(List.of(Source.asset("../frontend")))
                .destinationBucket(bucket)
                .build());
    }
}
