package com.samistax.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.samistax.dto.SampleEvent;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.pulsar.core.PulsarTemplate;
import org.springframework.pulsar.core.PulsarTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.*;
import java.util.logging.Logger;

@Controller
@Configuration
@EnableScheduling
public class EventController {

    private static int lastActivityId = 0;

    protected Logger logger = Logger.getLogger(EventController.class.getName());
    private List<SampleEvent> events = new ArrayList<>();


    //private final PulsarTemplate<SampleEvent> pulsarTemplate;
    private final PulsarTemplate<String> pulsarTemplate;

    public EventController(PulsarTemplate<String> pulsarTemplate) {
        this.pulsarTemplate = pulsarTemplate;
    }

    @Scheduled(fixedRateString = "${scheduler.pollingInterval:10000}") // delay in millisecond for generating new event
    public void simulatedEventStream() {
        // Simulate event generation background task
        SampleEvent event = generateEvents(1).get(0);
        this.events.add(event);

        System.out.println("Event: #" + lastActivityId +" produced.");
        JsonMapper mapper = new JsonMapper();
        // Convert the POJO to a JSON string
        try {
            /*try {

            } catch (JsonProcessingException jpe) {
                throw new RuntimeException(jpe);
            }
             */
            String jsonPayload = mapper.writeValueAsString(event);
            pulsarTemplate.sendAsync(jsonPayload);

            //pulsarTemplate.sendAsync(event);
        } catch (PulsarClientException pce) {
            throw new RuntimeException(pce);
        } catch (JsonProcessingException jpe) {
            throw new RuntimeException(jpe);
        }

    }

    @PostMapping("/trigger")
    public ResponseEntity<String> triggerEvent(@RequestBody String jsonPayload) {
        try {
            pulsarTemplate.sendAsync(jsonPayload);

            //JsonMapper mapper = new JsonMapper();
            //pulsarTemplate.sendAsync(mapper.readValue(jsonPayload, SampleEvent.class));

        } catch (PulsarClientException e) {
            return new ResponseEntity<>("Event triggering failed (PulsarClientException).", HttpStatus.INTERNAL_SERVER_ERROR);
        }// catch (JsonProcessingException e) {
        //   return new ResponseEntity<>("Event triggering failed (JsonProcessingException).", HttpStatus.INTERNAL_SERVER_ERROR);
        //}
        return new ResponseEntity<>("Event triggered successfully.", HttpStatus.OK);
    }


    @RequestMapping("/generateEvents/{eventCount}")
    public List<SampleEvent> generate(@PathVariable("eventCount") String eventCount) {
        logger.info("events-service byTypeId() invoked. TypeId = " + eventCount);
        int count = 0;
        try {
            count = Integer.parseInt(eventCount);
        } catch (NumberFormatException nfe) {}

        List<SampleEvent> generatedEvents = generateEvents(count);
        // Add events to the in memoory events list ofr other methods to be retrieved.
        events.addAll(generatedEvents );
        logger.info("events-service generated: " + generatedEvents.size() + " events" );
        return generatedEvents;

    }
    @RequestMapping("/events/eventCount")
    public ResponseEntity<Integer> eventCount() {
        System.out.println("Event count -> " + events.size());
        logger.info("events-service eventCount() invoked. Events = " + events.size());
        return new ResponseEntity<Integer>(events.size(), HttpStatus.OK);

    }
    @RequestMapping("/events/clearEvents")
    public ResponseEntity<Integer> clearEvents() {
        logger.info("events-service clearEvents() invoked. Removing " + events.size() +" events.");
        events.clear();
        return new ResponseEntity<Integer>(events.size(), HttpStatus.OK);

    }
    @RequestMapping("/events/all")
    public List<SampleEvent> getAllEvents() {
        logger.info("events-service getAllEvents() invoked. Events = " + events);
        return events;

    }
    public static List<SampleEvent> generateEvents(int eventCount) {
        List<SampleEvent> events = new ArrayList<>();
        for (int i = 0; i < eventCount; i++) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                events.add(mapper.readValue(EventController.generateSampleEvent(), SampleEvent.class));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return events;
    }

    private static String generateSampleEvent() {

        Random random = new Random();
        String[] environments = {"development", "testing", "production"};
        String[] functions = {"login", "logout", "signup"};
        String[] statuses = {"success", "error", "pending"};

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode eventNode = mapper.createObjectNode();
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
            lastActivityId += 1;
        } catch (JsonProcessingException e) {}
        return jsonPayload;
    }
}