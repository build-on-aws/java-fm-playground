package aws.community.examples.bedrock;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class InjectApiUrl implements RequestHandler<Object, String> {
    @Override
    public String handleRequest(Object event, Context context) {

        try (S3Client s3 = S3Client.builder().build()) {
            String apiUrl = System.getenv("API_URL");
            String bucket = System.getenv("FRONTEND_BUCKET");
            String key = "config.js";

            String config = "const frontendConfig = {\n" +
                    "    apiDomain: \"" + apiUrl + "\"\n" +
                    "}";

            Path path = Paths.get("/tmp/config.js");

            try {
                Files.writeString(path, config);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build();

            PutObjectResponse response = s3.putObject(request, path);
            System.out.println("File uploaded: " + response.eTag());
        }

        return null;
    }
}
