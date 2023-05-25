package person.dmkyr20.education.rsocket.guessnumber.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.MessageMapping;
import person.dmkyr20.education.rsocket.guessnumber.api.Routs;
import person.dmkyr20.education.rsocket.guessnumber.models.GuessNumberResponse;
import person.dmkyr20.education.rsocket.guessnumber.models.GuessNumberRequest;
import org.springframework.stereotype.Controller;
import person.dmkyr20.education.rsocket.guessnumber.models.Models;
import person.dmkyr20.education.rsocket.guessnumber.models.ValidationModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
public class GameController {
    private final int from;
    private final int to;

    public GameController(@Value("${game.number.min}") int from,
                          @Value("${game.number.max}") int to) {
        this.from = from;
        this.to = to;
    }

    @MessageMapping(Routs.GUESS_NUMBER)
    public Flux<byte[]> guess(Flux<byte[]> requests) {
        GameService service = new GameService(from, to);
        return requests
                .map(Models::toRequest)
                .map(GuessNumberRequest::getNumber)
                .map(service::guess)
                .map(GuessNumberResponse::new)
                .map(Models::toBytes);
    }

    @MessageMapping(Routs.VALIDATION)
    public Mono<byte[]> testConnection(Mono<byte[]> request) {
        return request
                .map(Models::toValidation)
                .map(ValidationModel::getCode)
                .doOnNext(code -> log.info("Connection successful " + code))
                .map(ValidationModel::new)
                .map(Models::toBytes);
    }
}
