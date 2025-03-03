package com.samistax.service;


import com.datastax.astra.client.model.InsertOneResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.samistax.dto.BaseEvent;
import com.samistax.dto.PriceUpdateEvent;
import com.samistax.generator.PricingDataGenerator;
import com.samistax.generator.SampleEventGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.pulsar.core.PulsarTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.logging.Logger;

import org.apache.pulsar.client.api.PulsarClientException;

import com.samistax.dto.SampleEvent;
import com.datastax.astra.client.Collection;
import com.datastax.astra.client.model.Document;
import com.datastax.astra.client.DataAPIClient;
import com.datastax.astra.client.Database;

@Controller
@Configuration
@EnableScheduling
public class EventController {

    private static int lastActivityId = 0;
    protected Logger logger = Logger.getLogger(EventController.class.getName());
    private List<BaseEvent> events = new ArrayList<>();

    @Value("${data_api.astra-token}")
    private String ASTRA_DB_TOKEN;
    @Value("${data_api.astra-endpoint}")
    private String ASTRA_ENDPOINT;
    @Value("${data_api.astra-collection}")
    private String ASTRA_COLLECTION;
    private Collection<Document> collection;

    private final boolean PUBLISH_MODE_PULSAR = true;
    private final boolean PUBLISH_MODE_DATA_API = true;

    @Autowired
    private final PulsarTemplate<String> pulsarTemplate;
    public EventController(PulsarTemplate<String> pulsarTemplate) {
        this.pulsarTemplate = pulsarTemplate;
    }

    // STEP 1: Establish connection to Astra DB and get a handle to collection
    @Bean
    public Database getDataApiClient() {
        // Initialize the client
        DataAPIClient client = new DataAPIClient(ASTRA_DB_TOKEN);
        Database db = client.getDatabase(ASTRA_ENDPOINT);
        System.out.println("Connected to AstraDB " + db.listCollectionNames());
        //collection = db.createCollection( "price_updates") ;
        collection = db.getCollection( "price_updates") ;
        return db;
    }
    // STEP 2: Generate sample data and write to Astra DB
    @Scheduled(fixedRateString = "${scheduler.pollingInterval:50}") // delay in millisecond for generating new event
    public void simulateEventStream() {
        // Simulate event generation background task
        //SampleEvent event = generateEvents(1).get(0);
        PriceUpdateEvent event = PricingDataGenerator.generateData();
        this.events.add(event);
        lastActivityId += 1;

        System.out.println("Event: #" + lastActivityId +" produced.");
        JsonMapper mapper = new JsonMapper();
        String jsonPayload = "";
        // Convert the POJO to a JSON string
        try {
            jsonPayload = mapper.writeValueAsString(event);
        } catch (JsonProcessingException jpe) {
            throw new RuntimeException(jpe);
        }

        // Write new events directly to Astra DB
        if ( PUBLISH_MODE_DATA_API ) { //
            String contentToVectorize = event.getItemName() + " " + event.getItemBrand() + " "+ event.getPrice();
            Document doc = Document.parse(jsonPayload).vectorize(contentToVectorize);

            InsertOneResult result = collection.insertOne(doc);
        }
        if ( PUBLISH_MODE_PULSAR ) {
            // Push event to Astra Streaming for payload transformation and persisting msg to Astra DB using sink)
            try {
                pulsarTemplate.sendAsync(jsonPayload);
            } catch (PulsarClientException pce) {
                throw new RuntimeException(pce);
            }
        }
    }

    @PostMapping("/trigger")
    public ResponseEntity<String> triggerEvent(@RequestBody String jsonPayload) {
        try {
            pulsarTemplate.sendAsync(jsonPayload);
            return new ResponseEntity<>("Event triggered successfully.", HttpStatus.OK);
        } catch (PulsarClientException e) {}
        return new ResponseEntity<>("Event triggering failed.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @RequestMapping("/generateEvents/{eventCount}")
    public List<BaseEvent> generate(@PathVariable("eventCount") String eventCount) {
        logger.info("events-service byTypeId() invoked. TypeId = " + eventCount);
        int count = 0;
        try {
            count = Integer.parseInt(eventCount);
        } catch (NumberFormatException nfe) {}

        List<BaseEvent> generatedEvents = generateEvents(count);
        // Add events to the in memory events list ofr other methods to be retrieved.
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
    public List<BaseEvent> getAllEvents() {
        logger.info("events-service getAllEvents() invoked. Events = " + events);
        return events;

    }
    public static List<BaseEvent> generateEvents(int eventCount) {
        List<BaseEvent> events = new ArrayList<>();
        for (int i = 0; i < eventCount; i++) {
            events.add(SampleEventGenerator.generateData());
        }
        return events;
    }


}