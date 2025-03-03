package com.samistax.function;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.samistax.dto.PriceUpdateEvent;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;

import java.util.Iterator;
import java.util.Map;
import java.util.logging.LogRecord;
import java.util.logging.Logger;


public class VectorizeFunction implements Function<String, String> {

    private final Logger logger = Logger.getLogger(VectorizeFunction.class.getName());
    @Override
    public void initialize(Context context) throws Exception {
        Function.super.initialize(context);
    }

    @Override
    public String process(String input, Context context) throws Exception {
        // Create an ObjectMapper instance for parsing JSON
        ObjectMapper mapper = new ObjectMapper();

        // Parse the input JSON string to JsonNode
        JsonNode inputJson = mapper.readTree(input);

        // Create an empty ObjectNode for the result
        ObjectNode resultJson = mapper.createObjectNode();

        // Iterate over the fields of inputJson
        Iterator<Map.Entry<String, JsonNode>> fields = inputJson.fields();
        int count = 0;
        String embeddedContent = "";
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            String fieldName = field.getKey();   // Field name
            JsonNode fieldValue = field.getValue();  // Field value

            // Copy field from inputJson into resultJson
            resultJson.set(fieldName, fieldValue);

            // Build context that will be embedded
            if ( count < 3) {
                embeddedContent = embeddedContent.concat(" " + fieldValue); // Append the value to the string
                count++; // Increment counter
            }

        }
        // Add new "vectorize" property for demonstration of content that needs to be embedded
        resultJson.put("$vectorize", embeddedContent);

        ObjectNode metadata = resultJson.withObjectProperty("metadata");
        metadata.put("source", context.getFunctionName());
        metadata.put("message_id", ""+resultJson.get("itemId"));

        return resultJson.toString();
    }
}