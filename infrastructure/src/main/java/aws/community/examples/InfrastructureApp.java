package aws.community.examples;

import software.amazon.awscdk.App;

public final class InfrastructureApp {
    public static void main(final String[] args) {
        App app = new App();

        new aws.community.examples.InfrastructureStack(app, "JavaFMPlayground");

        app.synth();
    }
}
