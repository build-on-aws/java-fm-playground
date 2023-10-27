package constructs.community.examples.bedrock;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;

public final class InfrastructureApp {
    public static void main(final String[] args) {
        App app = new App();

        Environment environment = Environment.builder()
                .region("us-east-1")
                .build();

        new InfrastructureStack(
                app, "JavaFMPlayground",
                StackProps.builder().env(environment).build()
        );

        app.synth();
    }
}
