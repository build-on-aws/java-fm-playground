package aws.community.examples.bedrock;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Fn;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.s3.Bucket;
import software.amazon.awscdk.services.s3.IBucket;
import software.amazon.awscdk.services.s3.deployment.BucketDeployment;
import software.amazon.awscdk.services.s3.deployment.BucketDeploymentProps;
import software.amazon.awscdk.services.s3.deployment.Source;

import java.util.List;

public class FrontendStack extends Stack {
    public FrontendStack(final App parent, final String id) {
        this(parent, id, null);
    }

    public FrontendStack(final App parent, final String id,  StackProps props) {
        super(parent, id, props);

        IBucket bucket = Bucket.fromBucketArn(this, "FrontendBucketArn", Fn.importValue("FrontendBucketArn"));

        new BucketDeployment(this, "FrontendDeployment", BucketDeploymentProps.builder()
                .sources(List.of(Source.asset("../frontend")))
                .destinationBucket(bucket)
                .build()
        );
    }
}
