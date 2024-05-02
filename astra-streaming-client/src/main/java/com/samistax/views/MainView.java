package com.samistax.views;

import com.samistax.astra.PulsarService;
import com.samistax.dto.SampleEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import reactor.core.publisher.Flux;

/**
 * A sample Vaadin view class.
 * <p>
 * To implement a Vaadin view just extend any Vaadin component and use @Route
 * annotation to announce it in a URL as a Spring managed bean.
 * <p>
 * A new instance of this class is created for every new user and every browser
 * tab/window.
 * <p>
 * The main view contains a text field for getting the user name and a button
 * that shows a greeting message in a notification.
 */
@Route
public class MainView extends VerticalLayout {

    /**
     * Construct a new Vaadin view.
     * <p>
     * Build the initial UI state for the user accessing the application.
     *
     * @param service
     *            The message service. Automatically injected Spring managed
     *            bean.
     */
    public MainView(@Autowired PulsarService service){

        // Create a grid bound to the list
        Grid<SampleEvent> grid = new Grid<>(SampleEvent.class);
        grid.setColumns("event_time", "transaction_id", "environment");
        grid.setSizeFull();

        Flux<SampleEvent> eventStream = service.getSampleEvents();
        eventStream.subscribe(
                event -> {
                    System.out.println("Received event: " + event);
                    this.getUI().ifPresent(ui -> ui.access(() -> {
                        grid.getListDataView().addItem(event);
                        grid.getDataProvider().refreshAll();
                    }));
                    },
                error -> System.err.println("Error occurred: " + error),
                () -> System.out.println("Stream completed.")
        );

        add(new Span("Listening to identified anomalies"), grid);
        setSizeFull();
    }

}