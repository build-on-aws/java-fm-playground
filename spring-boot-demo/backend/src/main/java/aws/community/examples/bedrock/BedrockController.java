package aws.community.examples.bedrock;

import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.bedrock.BedrockClient;

public class BedrockController {
    static String awsRegion;

    @Value("${awsRegion}")
    public void setAwsRegion(String awsRegion) {
        BedrockController.awsRegion = awsRegion;
    }

    static BedrockClient client() {
        return BedrockClient.builder()
                .region(Region.of(BedrockController.awsRegion))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }
}
