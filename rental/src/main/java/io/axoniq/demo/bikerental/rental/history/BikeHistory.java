package io.axoniq.demo.bikerental.rental.history;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class BikeHistory {

    @Id
    @GeneratedValue
    private Long id;

    private String bikeId;

    private String description;

    private LocalDateTime timestamp;

    public BikeHistory() {
    }

    public BikeHistory(String bikeId, String description, LocalDateTime timestamp) {
        this.bikeId = bikeId;
        this.description = description;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getBikeId() {
        return bikeId;
    }

    public void setBikeId(String bikeId) {
        this.bikeId = bikeId;
    }
}
