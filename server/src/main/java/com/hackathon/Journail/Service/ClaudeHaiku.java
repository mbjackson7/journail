package com.hackathon.Journail.Service;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.ContentBlock;
import software.amazon.awssdk.services.bedrockruntime.model.ConversationRole;
import software.amazon.awssdk.services.bedrockruntime.model.ConverseResponse;
import software.amazon.awssdk.services.bedrockruntime.model.Message;

public class ClaudeHaiku {
    private final static Region region = Region.US_EAST_1;
    final private StaticCredentialsProvider staticCredentialsProvider;

    public ClaudeHaiku() {
        String AwsAccessKeyId = System.getenv("AWS_ACCESS_KEY_ID");
        String AwsSecretAccessKey = System.getenv("AWS_SECRET_ACCESS_KEY");

        AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(AwsAccessKeyId, AwsSecretAccessKey);
        this.staticCredentialsProvider = StaticCredentialsProvider.create(awsBasicCredentials);
    }

    public String converse(String inputText) {
        BedrockRuntimeClient bedrockRuntimeClient = BedrockRuntimeClient.builder().credentialsProvider(this.staticCredentialsProvider).region(region).build();

        String modelId = "anthropic.claude-3-haiku-20240307-v1:0";

        Message message = Message.builder().content(ContentBlock.fromText(inputText)).role(ConversationRole.USER).build();

        try {
            ConverseResponse response = bedrockRuntimeClient.converse(request -> request
                    .modelId(modelId)
                    .messages(message)
                    .inferenceConfig(config -> config
                            .maxTokens(512)
                            .temperature(0.5F)
                            .topP(0.9F)));

            return response.output().message().content().get(0).text();

        } catch (SdkClientException e) {
            System.err.printf("ERROR: Can't invoke '%s'. Reason: %s", modelId, e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
