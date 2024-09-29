package com.hackathon.Journail.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.ContentBlock;
import software.amazon.awssdk.services.bedrockruntime.model.ConversationRole;
import software.amazon.awssdk.services.bedrockruntime.model.ConverseResponse;
import software.amazon.awssdk.services.bedrockruntime.model.Message;

@Component
public class ClaudeHaiku {
    private final Region region = Region.US_EAST_1;

    @Value("${access.key.id}")
    private String AwsAccessKeyId;

    @Value("${secret.access.key}")
    private String AwsSecretAccessKey;

    public String converse(String inputText) {
        AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(AwsAccessKeyId, AwsSecretAccessKey);
        StaticCredentialsProvider staticCredentialsProvider = StaticCredentialsProvider.create(awsBasicCredentials);
        BedrockRuntimeClient bedrockRuntimeClient = BedrockRuntimeClient.builder().credentialsProvider(staticCredentialsProvider).region(region).build();

        String modelId = "anthropic.claude-3-sonnet-20240229-v1:0";

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