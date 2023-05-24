package person.dmkyr20.eduaction.rsocket.guessnumber.client;

import io.rsocket.Payload;
import person.dmkyr20.education.rsocket.guessnumber.models.GuessNumberRequest;
import person.dmkyr20.education.rsocket.guessnumber.models.Models;
import reactor.core.publisher.Flux;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try(Client client = new Client(); Scanner scanner = new Scanner(System.in)) {
            System.out.println("WELOCOME!!!");
            System.out.println("The rules of the game are simple - we are guessing number and you have to guess it. We will say if our value more or less than yours!");

            Flux<Payload> requestFlux = Flux.<Integer>create(
                    s -> {
                        while (!s.isCancelled()) {
                            System.out.println("Your number?");
                            s.next(scanner.nextInt());
                        }
                        s.complete();
                    })
                    .map(GuessNumberRequest::new)
                    .map(Models::toPayload);

            client.chanel(requestFlux)
                    .map(Models::toResponse)
                    .doOnNext(
                            response -> {
                                System.out.println(response.getMessage());
                            })
                    .subscribe();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
