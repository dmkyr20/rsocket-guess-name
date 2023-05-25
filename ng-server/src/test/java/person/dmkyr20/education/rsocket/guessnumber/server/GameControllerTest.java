package person.dmkyr20.education.rsocket.guessnumber.server;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.messaging.rsocket.RSocketRequester;
import person.dmkyr20.education.rsocket.guessnumber.api.Routs;
import person.dmkyr20.education.rsocket.guessnumber.models.ValidationModel;

import java.util.UUID;

@SpringBootTest
class GameControllerTest {
//    application.cbor

    @Autowired
    RSocketRequester.Builder builder;

    @Test
    public void shouldGetSameValue() {
        RSocketRequester requester1 = RSocketRequester.builder()
                .dataMimeType(MediaType.APPLICATION_CBOR)
//                .rsocketStrategies(RSocketStrategies.builder()
//
//                        .build())
                .tcp("localhost", 7000);

        RSocketRequester requester2 = builder
                .tcp("localhost", 7000);

        RSocketRequester requester = requester2;

        ValidationModel result = requester.route(Routs.VALIDATION)
                .data(new ValidationModel(UUID.randomUUID()))
                .retrieveMono(ValidationModel.class)
                .blockOptional()
                .orElseThrow();
        System.out.println(result.getCode());
    }

}