package person.dmkyr20.eduaction.rsocket.guessnumber.client;

import person.dmkyr20.education.rsocket.guessnumber.models.GuessNumberRequest;
import person.dmkyr20.education.rsocket.guessnumber.models.GuessNumberResponse;
import person.dmkyr20.education.rsocket.guessnumber.models.ValidationModel;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

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
                            String next = scanner.next();
                            if("exit".equals(next)) {
                                System.out.println("Bye!!!");
                                s.complete();
                                return;
                            } else if (isInteger(next)) {
                                s.next(Integer.parseInt(next));
                            } else {
                                System.out.println("Oops! The value is not an integer - " + next);
                            }
                        }
                        s.complete();
                    })
                    .map(GuessNumberRequest::new);

            AtomicInteger score = new AtomicInteger(0);
            client.chanel(requestFlux)
                    .map(GuessNumberResponse::getMessage)
                    .doOnNext(System.out::println)
                    .doOnNext(n -> {
                        if(n != null && n.startsWith("YES")) {
                            System.out.println("You won, your score is: " + score.get());
                            System.exit(0);
                        }
                    })
                    .doOnNext(n -> score.incrementAndGet())
                    .subscribe();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isInteger(String s) {
        return isInteger(s,10);
    }

    public static boolean isInteger(String s, int radix) {
        if(s.isEmpty()) return false;
        for(int i = 0; i < s.length(); i++) {
            if(i == 0 && s.charAt(i) == '-') {
                if(s.length() == 1) return false;
                else continue;
            }
            if(Character.digit(s.charAt(i),radix) < 0) return false;
        }
        return true;
    }
}
