package person.dmkyr20.education.rsocket.guessnumber.server;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GameService {
    private final int from;
    private final int to;
    private int number;

    private static int randomNumber(int from, int to) {
        return (int) (Math.random() * to) + from;
    }

    public GameService(int from, int to) {
        this.from = from;
        this.to = to;
        this.number = randomNumber(from, to);
    }

    public void regenerate() {
        number = randomNumber(from, to);
    }

    public String guess(int value) {
        log.info("guessed = " + number + " | input = " + value);
        int comparator = Integer.compare(value, number);
        if(comparator == 0) {
            return "YES, the value is " + number;
        } else if(comparator < 0) {
            return "NO, the value is more than " + value;
        } else {
            return "NO, the value is less than " + value;
        }
    }
}
