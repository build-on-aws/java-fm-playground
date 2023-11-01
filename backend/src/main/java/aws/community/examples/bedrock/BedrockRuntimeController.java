package aws.community.examples.bedrock;

import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;

public class BedrockRuntimeController {
    static String awsRegion;

    @Value("${awsRegion}")
    public void setAwsRegion(String awsRegion) {
        BedrockRuntimeController.awsRegion = awsRegion;
    }

    static BedrockRuntimeClient client() {
        return BedrockRuntimeClient.builder()
                .region(Region.of(BedrockController.awsRegion))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }
}
