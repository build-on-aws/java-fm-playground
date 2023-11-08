package aws.community.examples.bedrock.aimodels;

public interface LLM {
    public record Request(String prompt, Double temperature, Integer maxTokens) { }
    public record Response(String completion) { }
}
