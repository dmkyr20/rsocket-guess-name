package person.dmkyr20.eduaction.rsocket.guessnumber.client;

import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.core.RSocketConnector;
import io.rsocket.transport.ClientTransport;
import io.rsocket.transport.netty.client.TcpClientTransport;
import lombok.AllArgsConstructor;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

@AllArgsConstructor
public class Client implements AutoCloseable {
    private final RSocket rSocket;

    public Client() {
        ClientTransport clientTransport = TcpClientTransport.create(8080);
        this.rSocket = RSocketConnector.connectWith(clientTransport).block();
    }

    public Flux<Payload> chanel(Publisher<Payload> requestPublisher) {
        return rSocket.requestChannel(requestPublisher);
    }

    @Override
    public void close() throws Exception {
        rSocket.dispose();
    }
}
