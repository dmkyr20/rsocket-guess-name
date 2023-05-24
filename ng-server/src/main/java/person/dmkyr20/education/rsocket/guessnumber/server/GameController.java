package person.dmkyr20.education.rsocket.guessnumber.server;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.MessageMapping;
import person.dmkyr20.education.rsocket.guessnumber.models.GuessNumberResponse;
import person.dmkyr20.education.rsocket.guessnumber.models.GuessNumberRequest;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

@Controller
public class GameController {
    private final int from;
    private final int to;

    public GameController(@Value("${game.number.min}") int from,
                          @Value("${game.number.max}") int to) {
        this.from = from;
        this.to = to;
    }

    @MessageMapping
    public Flux<GuessNumberResponse> guess(Flux<GuessNumberRequest> requests) {
        GameService service = new GameService(from, to);
        return requests
                .map(GuessNumberRequest::getNumber)
                .map(service::guess)
                .map(GuessNumberResponse::new);
    }
}
