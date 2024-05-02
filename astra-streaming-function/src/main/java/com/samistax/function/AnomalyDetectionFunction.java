package com.samistax.function;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.samistax.dto.SampleEvent;
import org.apache.pulsar.client.api.Schema;
import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;

public class AnomalyDetectionFunction implements Function<String, SampleEvent> {

    @Override
    public SampleEvent process(String input, Context context) throws Exception {

        // Create an ObjectMapper instance for parsing JSON
        ObjectMapper mapper = new ObjectMapper();
        // Parse the input JSON string to JsonNode
        JsonNode jsonNode = mapper.readTree(input);
        // Example how to map JSON to a POJO
        SampleEvent se = mapper.readValue(input, SampleEvent.class);

        int status_code = jsonNode.get("status_code").asInt();
        String status = jsonNode.get("status").asText();
        // Using status code as a dummy trigger for anomaly detected
        // In real case predictive ML models and/or domain specific algorithms to be used to detect anomaly
        if ( status_code > 200 ) {
            // Create new output topic name
            String outputTopicName = context.getInputTopics().stream().findFirst().get() + "-anomaly";
            // Publish new output message to the anomaly topic
            context.newOutputMessage(outputTopicName, Schema.JSON(SampleEvent.class)).value(se).sendAsync();
        }
        // Processed object will be sent to the topic configured as output when setting up the function in Astra Streaming
        return se;
    }
}

