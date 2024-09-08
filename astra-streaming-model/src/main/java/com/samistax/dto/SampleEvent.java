package com.samistax.dto;

import lombok.Data;


// Secure Event sample event
public @Data class SampleEvent {
    private String transaction_id;
    private long event_time;
    private long cas_timestamp;
    private String client_version;
    private String environment;
    private String event_log;
    private String function;
    private String identity;
    private String level;
    private String partner_transaction_id;
    private float process_time;
    private String product_id;
    private String request;
    private String request_id;
    private String response_id;
    private int response_size;
    private String serial_number;
    private String server_name;
    private String server_version;
    private String service;
    private String site_code;
    private String source;
    private String status;
    private int status_code;
    private String status_message;
    private long tsd_timestamp;
    private String type;
    private String url;
    private String user_company;
    private String user_name;
}
