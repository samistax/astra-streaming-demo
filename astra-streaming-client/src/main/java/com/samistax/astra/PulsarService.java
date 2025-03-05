package com.samistax.astra;

import com.samistax.dto.PriceUpdateEvent;
import org.springframework.pulsar.annotation.PulsarListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Service
public class PulsarService {

    private final Sinks.Many<PriceUpdateEvent> sampleEventSink = Sinks.many().multicast().directBestEffort();
    private final Flux<PriceUpdateEvent> sampleEvents = sampleEventSink.asFlux();

    @PulsarListener
    private void sampleEventReceived(PriceUpdateEvent sampleEvent) {
        sampleEventSink.tryEmitNext(sampleEvent);
    }
    public Flux<PriceUpdateEvent> getSampleEvents() {
        return sampleEvents;
    }
}
