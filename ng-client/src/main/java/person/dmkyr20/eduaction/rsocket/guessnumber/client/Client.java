package person.dmkyr20.eduaction.rsocket.guessnumber.client;

import io.rsocket.core.RSocketClient;
import io.rsocket.core.RSocketConnector;
import io.rsocket.metadata.WellKnownMimeType;
import io.rsocket.transport.ClientTransport;
import io.rsocket.transport.netty.client.TcpClientTransport;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import person.dmkyr20.education.rsocket.guessnumber.models.GuessNumberRequest;
import person.dmkyr20.education.rsocket.guessnumber.models.GuessNumberResponse;
import person.dmkyr20.education.rsocket.guessnumber.models.Models;
import person.dmkyr20.education.rsocket.guessnumber.models.ValidationModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@AllArgsConstructor
public class Client implements AutoCloseable {
    private final RSocketClient rSocket;

    public Client(int port) {
        ClientTransport clientTransport = TcpClientTransport.create("localhost",port);
        this.rSocket = RSocketClient
                .from(RSocketConnector
                        .create()
                        .metadataMimeType(String.valueOf(WellKnownMimeType.MESSAGE_RSOCKET_COMPOSITE_METADATA))
                        .connect(clientTransport));
    }

    public Flux<GuessNumberResponse> chanel(Publisher<GuessNumberRequest> requestPublisher) {
        return rSocket.requestChannel(
                Flux.from(requestPublisher)
                        .map(Models::toBytes)
                        .map(PayloadUtils::createGuessNumberPayload))
                .map(Models::toResponse);
    }

    public Mono<ValidationModel> testConnection(ValidationModel validation) {
        return rSocket.requestResponse(
                Mono.just(validation)
                        .map(Models::toBytes)
                        .map(PayloadUtils::createValidationPayload))
                .map(Models::toValidation);
    }

    @Override
    public void close() throws Exception {
        rSocket.dispose();
    }
}
