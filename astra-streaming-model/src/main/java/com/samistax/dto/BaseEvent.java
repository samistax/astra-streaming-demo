package com.samistax.dto;

import lombok.Data;

// Secure Event sample event
public @Data class BaseEvent {
    private String itemId;

    public String getItemId() {
        return itemId;
    }
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
}
