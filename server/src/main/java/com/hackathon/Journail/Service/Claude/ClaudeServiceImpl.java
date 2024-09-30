package com.hackathon.Journail.Service.Claude;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.ContentBlock;
import software.amazon.awssdk.services.bedrockruntime.model.ConversationRole;
import software.amazon.awssdk.services.bedrockruntime.model.ConverseResponse;
import software.amazon.awssdk.services.bedrockruntime.model.Message;

@Service
public class ClaudeServiceImpl implements ClaudeService {
    private final Region REGION = Region.US_EAST_1;
    private final String MODEL_ID = "anthropic.claude-3-sonnet-20240229-v1:0";
    @Value("${access.key.id}")
    private String AwsAccessKeyId;
    @Value("${secret.access.key}")
    private String AwsSecretAccessKey;

    @Override
    public String converse(String inputText) {
        AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(AwsAccessKeyId, AwsSecretAccessKey);
        StaticCredentialsProvider staticCredentialsProvider = StaticCredentialsProvider.create(awsBasicCredentials);
        try {
            BedrockRuntimeClient bedrockRuntimeClient = BedrockRuntimeClient.builder().
                    credentialsProvider(staticCredentialsProvider)
                    .region(REGION).build();
            Message message = Message.builder()
                    .content(ContentBlock.fromText(inputText))
                    .role(ConversationRole.USER)
                    .build();
            ConverseResponse response = bedrockRuntimeClient.converse(request -> request
                    .modelId(MODEL_ID)
                    .messages(message)
                    .inferenceConfig(config -> config
                            .maxTokens(512)
                            .temperature(0.5F)
                            .topP(0.9F)));

            return response.output().message().content().get(0).text();
        } catch (SdkClientException e) {
            System.err.printf("ERROR: Can't invoke '%s'. Reason: %s", MODEL_ID, e.getMessage());
            throw new RuntimeException(e);
        }
    }
}