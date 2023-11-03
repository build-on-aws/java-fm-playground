package aws.community.examples.bedrock.models;

public abstract class Llm {
    public record ChatRequest(String prompt) { }
}
