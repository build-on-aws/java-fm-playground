package aws.community.examples.bedrock.resources;

import software.amazon.awscdk.BundlingOptions;
import software.amazon.awscdk.DockerVolume;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.s3.assets.AssetOptions;

import java.util.Arrays;
import java.util.List;

import static java.util.Collections.singletonList;
import static software.amazon.awscdk.BundlingOutput.ARCHIVED;

public abstract class BedrockFunction {

    public static AssetOptions prepareAssetOptions() {

        // Mount local .m2 inside the container to avoid repeat downloads
        List<DockerVolume> volume = singletonList(
                DockerVolume.builder()
                        .hostPath(System.getProperty("user.home") + "/.m2/")
                        .containerPath("/root/.m2/")
                        .build()
        );

        List<String> packagingInstructions = Arrays.asList(
                "/bin/sh",
                "-c",
                "cd Bedrock && mvn clean install " +
                        "&& cp /asset-input/Bedrock/target/bedrock.jar /asset-output/"
        );

        BundlingOptions bundlingOptions = BundlingOptions.builder()
                .command(packagingInstructions)
                .image(Runtime.JAVA_11.getBundlingImage())
                .user("root")
                .outputType(ARCHIVED)
                .volumes(volume)
                .build();

        return AssetOptions.builder().bundling(bundlingOptions).build();
    }
}
