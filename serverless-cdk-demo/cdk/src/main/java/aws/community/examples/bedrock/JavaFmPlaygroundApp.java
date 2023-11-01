package aws.community.examples.bedrock;

import aws.community.examples.bedrock.stacks.BackendStack;
import aws.community.examples.bedrock.stacks.FrontendStack;
import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;

public final class JavaFmPlaygroundApp {
    public static void main(final String[] args) {
        App app = new App();

        Environment environment = Environment.builder()
                .region("us-east-1")
                .build();

        new BackendStack(
                app, "Backend",
                StackProps.builder()
                        .env(environment)
                        .stackName("JavaFMPlayground-Backend")
                        .build()
        );

        new FrontendStack(
                app, "Frontend",
                StackProps.builder()
                        .env(environment)
                        .stackName("JavaFMPlayground-Frontend")
                        .build()
        );

        app.synth();
    }
}