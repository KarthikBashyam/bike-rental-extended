package io.axoniq.demo.bikerental.rental.history;

import io.axoniq.demo.bikerental.coreapi.rental.BikeRegisteredEvent;
import io.axoniq.demo.bikerental.coreapi.rental.BikeRequestedEvent;
import org.axonframework.eventhandling.EventHandler;

import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class BikeHistoryProjection {

    private BikeHistoryRepository repository;

    public BikeHistoryProjection(BikeHistoryRepository repository) {
        this.repository = repository;
    }
    @EventHandler
    public void handle(BikeRegisteredEvent event) {
        repository.save(new BikeHistory(event.getBikeId(), "Bike Registered at Location:"+event.getLocation(), LocalDateTime.now()));
    }

    @EventHandler
    public void handle(BikeRequestedEvent event) {
        repository.save(new BikeHistory(event.getBikeId(),"Bike requested by :"+event.getRenter(), LocalDateTime.now()));
    }

    @QueryHandler(queryName = "findAllByBikeId")
    public Iterable<BikeHistory> findAllByBikeId(String bikeiD) {
        return repository.findAllByBikeId(bikeiD);
    }
}
