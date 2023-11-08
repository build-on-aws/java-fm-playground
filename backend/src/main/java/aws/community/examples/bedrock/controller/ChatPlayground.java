package aws.community.examples.bedrock.controller;

import aws.community.examples.bedrock.aimodels.Claude;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;

import static aws.community.examples.bedrock.aimodels.LLM.Request;
import static aws.community.examples.bedrock.aimodels.LLM.Response;

@RestController
public class ChatPlayground {

    private final BedrockRuntimeClient client;

    @Autowired
    public ChatPlayground(final BedrockRuntimeClient client) {
        this.client = client;
    }

    @PostMapping("/foundation-aimodels/model/chat/anthropic.claude-v2/invoke")
    public Response invoke(@RequestBody Request body) {
        String  systemPrompt =
                """
                Take the role of a friendly chat bot. Your responses are brief.
                You sometimes use emojis where appropriate, but you don't overdo it.
                You engage human in a dialog by regularly asking questions,
                except when Human indicates that the conversation is over.
                """;

        String prompt = systemPrompt + "\n\n" + body.prompt();

        return new Response(Claude.invoke(client, prompt, 0.8, 300));
    }
}
