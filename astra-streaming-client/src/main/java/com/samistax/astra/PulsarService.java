package com.samistax.astra;

import org.springframework.pulsar.annotation.PulsarListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import com.samistax.dto.SampleEvent;

@Service
public class PulsarService {

    private final Sinks.Many<SampleEvent> sampleEventSink = Sinks.many().multicast().directBestEffort();
    private final Flux<SampleEvent> sampleEvents = sampleEventSink.asFlux();

    @PulsarListener
    private void sampleEventReceived(SampleEvent sampleEvent) {
        sampleEventSink.tryEmitNext(sampleEvent);
    }
    public Flux<SampleEvent> getSampleEvents() {
        return sampleEvents;
    }
}
