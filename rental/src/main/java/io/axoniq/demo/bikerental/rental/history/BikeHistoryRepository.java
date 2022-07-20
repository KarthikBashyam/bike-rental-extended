package io.axoniq.demo.bikerental.rental.history;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BikeHistoryRepository extends JpaRepository<BikeHistory, Long> {

    public Iterable<BikeHistory> findAllByBikeId(String bikeId);
}
