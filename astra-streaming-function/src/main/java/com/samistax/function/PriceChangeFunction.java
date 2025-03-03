package com.samistax.function;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.samistax.dto.PriceUpdateEvent;
import org.apache.pulsar.client.api.Schema;
import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;

public class PriceChangeFunction implements Function<String, PriceUpdateEvent> {

    @Override
    public PriceUpdateEvent process(String input, Context context) throws Exception {

        // Create an ObjectMapper instance for parsing JSON
        ObjectMapper mapper = new ObjectMapper();
        // Parse the input JSON string to JsonNode
        JsonNode jsonNode = mapper.readTree(input);
        // Example how to map JSON to a POJO
        PriceUpdateEvent se = mapper.readValue(input, PriceUpdateEvent.class);
        int quantity = jsonNode.get("quantity").asInt();
        // Using quantity as a dummy trigger for sinkign subset of input messages
        if ( quantity > 10 ) {
            // Create new output topic name
            String outputTopicName = context.getInputTopics().stream().findFirst().get() + "-processed";
            // Publish new output message to the anomaly topic
            context.newOutputMessage(outputTopicName, Schema.JSON(PriceUpdateEvent.class)).value(se).sendAsync();
        }
        // Processed object will be sent to the topic configured as output when setting up the function in Astra Streaming
        return se;
    }
}

