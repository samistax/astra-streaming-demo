package com.samistax.generator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.samistax.dto.SampleEvent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.time.Instant;
import java.util.Random;
import java.util.UUID;

public class SampleEventGenerator {
    public static SampleEvent generateData() {
        Random random = new Random();
        SampleEvent demoEvent = new SampleEvent();
        // Example how to map JSON to a POJO
        try {
            ObjectMapper mapper = new ObjectMapper();
            demoEvent = mapper.readValue(generateSampleEventJSON(), SampleEvent.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return demoEvent;
    }
    private static String generateSampleEventJSON() {

        Random random = new Random();
        String[] environments = {"development", "testing", "production"};
        String[] functions = {"login", "logout", "signup"};
        String[] statuses = {"success", "error", "pending"};

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode eventNode = mapper.createObjectNode();
        eventNode.put("id", UUID.randomUUID().toString());
        eventNode.put("transaction_id", UUID.randomUUID().toString());
        eventNode.put("event_time", Instant.now().toEpochMilli());
        eventNode.put("cas_timestamp", Instant.now().plusSeconds(random.nextInt(100)).toEpochMilli());
        eventNode.put("client_version", "1.0." + random.nextInt(10));
        eventNode.put("environment", environments[random.nextInt(environments.length)]);
        eventNode.put("event_log", "Event log example");
        eventNode.put("function", functions[random.nextInt(functions.length)]);
        eventNode.put("identity", "user" + random.nextInt(1000));
        eventNode.put("level", "INFO");
        eventNode.put("partner_transaction_id", UUID.randomUUID().toString());
        eventNode.put("process_time", random.nextFloat() * 100);
        eventNode.put("product_id", "prod-" + random.nextInt(100));
        eventNode.put("request", "{\"action\":\"" + functions[random.nextInt(functions.length)] + "\"}");
        eventNode.put("request_id", UUID.randomUUID().toString());
        eventNode.put("response_id", UUID.randomUUID().toString());
        eventNode.put("response_size", random.nextInt(5000));
        eventNode.put("serial_number", "SN-" + UUID.randomUUID().toString());
        eventNode.put("server_name", "server" + random.nextInt(10) + ".example.com");
        eventNode.put("server_version", "v" + random.nextInt(5) + ".0");
        eventNode.put("service", "service" + random.nextInt(5));
        eventNode.put("site_code", "SC" + random.nextInt(999));
        eventNode.put("source", "web");
        eventNode.put("status", statuses[random.nextInt(statuses.length)]);
        eventNode.put("status_code", random.nextInt(500));
        eventNode.put("status_message", "Status message example");
        eventNode.put("tsd_timestamp", Instant.now().plusSeconds(random.nextInt(1000)).toEpochMilli());
        eventNode.put("type", "type" + random.nextInt(5));
        eventNode.put("url", "https://example.com/api/" + functions[random.nextInt(functions.length)]);
        eventNode.put("user_company", "ExampleCorp");
        eventNode.put("user_name", "User" + random.nextInt(1000));

        String jsonPayload = "";
        try {
            jsonPayload = mapper.writeValueAsString(eventNode);
        } catch (JsonProcessingException e) {}
        return jsonPayload;
    }
}