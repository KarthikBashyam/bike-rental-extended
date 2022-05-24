package io.axoniq.demo.bikerental.rental;

import com.thoughtworks.xstream.XStream;
import io.axoniq.demo.bikerental.coreapi.payment.PaymentStatus;
import io.axoniq.demo.bikerental.coreapi.rental.BikeStatus;
import org.axonframework.common.caching.Cache;
import org.axonframework.common.caching.WeakReferenceCache;
import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.config.Configuration;
import org.axonframework.config.ConfigurationScopeAwareProvider;
import org.axonframework.config.EventProcessingConfigurer;
import org.axonframework.deadline.DeadlineManager;
import org.axonframework.deadline.SimpleDeadlineManager;
import org.axonframework.eventhandling.tokenstore.jpa.TokenEntry;
import org.axonframework.messaging.StreamableMessageSource;
import org.axonframework.modelling.saga.repository.jpa.SagaEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@EntityScan(basePackageClasses = {BikeStatus.class, SagaEntry.class, TokenEntry.class, PaymentStatus.class})
@SpringBootApplication
public class RentalApplication {

    public static void main(String[] args) {
        SpringApplication.run(RentalApplication.class, args);
    }

    @Bean
    public DeadlineManager deadlineManager(TransactionManager transactionManager,
                                           Configuration config) {
        return SimpleDeadlineManager.builder()
                                    .transactionManager(transactionManager)
                                    .scopeAwareProvider(new ConfigurationScopeAwareProvider(config))
                                    .build();
    }

    @Bean(destroyMethod = "shutdown")
    public ScheduledExecutorService workerExecutorService() {
        return Executors.newScheduledThreadPool(4);
    }

    @Autowired
    public void configureXStreamSecurity(XStream xStream) {
        xStream.allowTypesByWildcard(new String[]{"io.axoniq.demo.bikerental.coreapi.**"});
    }

    @Autowired
    public void configure(EventProcessingConfigurer eventProcessing) {
        eventProcessing.registerPooledStreamingEventProcessor(
                "PaymentSagaProcessor",
                Configuration::eventStore,
                (c, b) -> b.workerExecutor(workerExecutorService())
                           .batchSize(100)
                           .initialToken(StreamableMessageSource::createHeadToken)
        );
        eventProcessing.registerPooledStreamingEventProcessor(
                "io.axoniq.demo.bikerental.rental.query",
                Configuration::eventStore,
                (c, b) -> b.workerExecutor(workerExecutorService())
                           .batchSize(100)

        );
        eventProcessing.registerPooledStreamingEventProcessor(
                "io.axoniq.demo.bikerental.rental.payment",
                Configuration::eventStore,
                (c, b) -> b.workerExecutor(workerExecutorService())
                           .batchSize(100)
        );
    }

    @Bean
    public Cache bikeCache() {
        return new WeakReferenceCache();
    }

    @Bean
    public HttpTraceRepository htttpTraceRepository() {
        return new InMemoryHttpTraceRepository();
    };

}
