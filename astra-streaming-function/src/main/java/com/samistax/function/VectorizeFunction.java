package com.samistax.function;

import com.datastax.astra.client.Collection;
import com.datastax.astra.client.DataAPIClient;
import com.datastax.astra.client.Database;
import com.datastax.astra.client.model.CollectionOptions;
import com.datastax.astra.client.model.Document;
import com.datastax.astra.client.model.InsertOneResult;
import com.datastax.astra.client.model.SimilarityMetric;
import com.fasterxml.jackson.databind.node.ObjectNode; // Unshaded Jackson for Pulsar
import org.apache.pulsar.client.api.schema.GenericObject;
import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class VectorizeFunction implements Function<GenericObject, Void> {

    private static final Logger logger = LoggerFactory.getLogger(Fi.class);
    private Collection<Document> collection;
    private com.fasterxml.jackson.databind.ObjectMapper pulsarObjectMapper = new com.fasterxml.jackson.databind.ObjectMapper(); // Unshaded Jackson ObjectMapper for Pulsar
    private String astraApiEndpoint;
    private String astraToken;
    private String keyspace;
    private String collectionName;
    private String vectorService;
    private String vectorModel;
    private String searchString;
    private int vectorDimension;
    private String vectorServiceSecretKey;

    @Override
    public void initialize(Context context) {
        try {
            // Retrieve Astra DB configuration from user-defined properties
            astraApiEndpoint = context.getUserConfigValue("ASTRA_DB_API_ENDPOINT")
                    .map(Object::toString)
                    .orElseThrow(() -> new IllegalArgumentException("ASTRA_DB_API_ENDPOINT is required"));

            astraToken = context.getUserConfigValue("ASTRA_DB_APPLICATION_TOKEN")
                    .map(Object::toString)
                    .orElseThrow(() -> new IllegalArgumentException("ASTRA_DB_APPLICATION_TOKEN is required"));

            keyspace = context.getUserConfigValue("ASTRA_DB_KEYSPACE")
                    .map(Object::toString)
                    .orElseThrow(() -> new IllegalArgumentException("ASTRA_DB_KEYSPACE is required"));

            collectionName = context.getUserConfigValue("ASTRA_DB_COLLECTION")
                    .map(Object::toString)
                    .orElseThrow(() -> new IllegalArgumentException("ASTRA_DB_COLLECTION is required"));

            // Retrieve vector service and model from user-defined properties with defaults
            vectorService = context.getUserConfigValue("ASTRA_DB_VECTORIZE_SERVICE")
                    .map(Object::toString)
                    .orElse("nvidia"); // Default to "nvidia" if not provided

            vectorModel = context.getUserConfigValue("ASTRA_DB_VECTORIZE_MODEL")
                    .map(Object::toString)
                    .orElse("NV-Embed-QA"); // Default to "NV-Embed-QA" if not provided

            // Retrieve the vector dimension from user-defined properties, defaulting to 1024
            vectorDimension = context.getUserConfigValue("ASTRA_DB_VECTOR_DIMENSION")
                    .map(Object::toString)
                    .map(Integer::parseInt)  // Convert to integer
                    .orElse(1024);  // Default dimension if not provided

            // Retrieve the vector embedding service secret key, needed with openai based embedding service
            vectorServiceSecretKey = context.getUserConfigValue("ASTRA_DB_VECTORIZE_SECRET_KEY")
                    .map(Object::toString).orElse("");
            System.out.println("SecretKey " + vectorServiceSecretKey);



            // Retrieve the search field for $vectorize
            searchString = context.getUserConfigValue("ASTRA_DB_VECTORIZE_FIELD")
                    .map(Object::toString)
                    .orElseThrow(() -> new IllegalArgumentException("ASTRA_DB_VECTORIZE_FIELD is required"));

            // Initialize Astra DB client and create collection
            DataAPIClient client = new DataAPIClient(astraToken);
            Database db = client.getDatabase(astraApiEndpoint);

            // Create a collection with vectorization enabled
            collection = db.createCollection(
                    collectionName,
                    CollectionOptions.builder()
                            .vectorDimension(vectorDimension)  // Use configurable vector dimension
                            .vectorSimilarity(SimilarityMetric.COSINE)  // Use cosine similarity metric
                            .vectorize(vectorService, vectorModel,vectorServiceSecretKey)  // Vector service and model
                            .build());

            logger.info("Initialized Astra DB with Endpoint: {}, Keyspace: {}, Collection: {}, Vector Dimension: {}", astraApiEndpoint, keyspace, collectionName, vectorDimension);

        } catch (Exception e) {
            logger.error("Error during initialization: ", e);
            throw new RuntimeException("Failed to initialize VectorizeFunction", e);  // Propagate the error if initialization fails
        }
    }

    @Override
    public Void process(GenericObject input, Context context) {
        try {
            logger.info("Raw message received: {}", input.getNativeObject());

            // Process the input using the unshaded Jackson ObjectMapper for Pulsar
            ObjectNode pulsarData = pulsarObjectMapper.convertValue(input.getNativeObject(), ObjectNode.class);

            // Convert the ObjectNode into a Map<String, Object> for easier manipulation
            Map<String, Object> dataMap = pulsarObjectMapper.convertValue(pulsarData, Map.class);
            logger.info("Data map content: {}", dataMap);  // Log the full map content for debugging

            // Create a new document for insertion into Astra DB
            Document doc = new Document(dataMap);

            // Check if the search field exists in the document, and add it to $vectorize
            dataMap.forEach((key, value) -> {
                if (key.equals(searchString) && value instanceof String) {
                    logger.info("Found matching field: {}, adding to $vectorize", key);
                    doc.append("$vectorize", value);  // Append the field value to $vectorize
                }
            });

            // Insert the document into Astra DB
            InsertOneResult result = collection.insertOne(doc);
            logger.info("Document successfully inserted with ID: {}", result.getInsertedId());

        } catch (Exception e) {
            logger.error("Error processing message: ", e);  // Log the error with stack trace for troubleshooting
        }

        return null;  // Return null as required by the Pulsar Function interface
    }
}