package person.dmkyr20.eduaction.rsocket.guessnumber.client;

import person.dmkyr20.education.rsocket.guessnumber.models.GuessNumberRequest;
import person.dmkyr20.education.rsocket.guessnumber.models.GuessNumberResponse;
import person.dmkyr20.education.rsocket.guessnumber.models.ValidationModel;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Scanner;
import java.util.UUID;

public class Main {

    public static void main(String[] args) {
        try(Client client = new Client(7000); Scanner scanner = new Scanner(System.in)) {
            System.out.println("WELOCOME!!!");
            System.out.println("The rules of the game are simple - " +
                    "we are guessing number and you have to guess it. " +
                    "We will say if our value more or less than yours!");

            UUID original = UUID.randomUUID();
            ValidationModel validationModel = new ValidationModel(original);
            UUID actual = client.testConnection(validationModel)
                    .blockOptional(Duration.ofSeconds(2))
                    .orElseThrow(() -> new RuntimeException("Connection isn't valid"))
                    .getCode();
            if( ! actual.equals(original)) {
                throw new RuntimeException("Connection isn't valid");
            }


            Flux<GuessNumberRequest> requestFlux = Flux.<Integer>create(
                    s -> {
                        while (!s.isCancelled()) {
                            System.out.println("Your number?");
                            s.next(scanner.nextInt());
                        }
                        s.complete();
                    })
                    .map(GuessNumberRequest::new);

            client.chanel(requestFlux)
                    .map(GuessNumberResponse::getMessage)
                    .doOnNext(System.out::println)
                    .subscribe();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
