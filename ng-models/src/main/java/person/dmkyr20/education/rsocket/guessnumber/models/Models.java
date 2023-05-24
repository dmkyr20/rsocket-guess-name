package person.dmkyr20.education.rsocket.guessnumber.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.rsocket.Payload;
import io.rsocket.util.DefaultPayload;

import java.io.IOException;

public class Models {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static GuessNumberRequest toRequest(Payload payload) {
        try {
            byte[] data = payload.getData().array();
            return MAPPER.readValue(data, GuessNumberRequest.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static GuessNumberResponse toResponse(Payload payload) {
        try {
            byte[] data = payload.getData().array();
            return MAPPER.readValue(data, GuessNumberResponse.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Payload toPayload(Object response) {
        try {
            byte[] data = MAPPER.writeValueAsBytes(response);
            return DefaultPayload.create(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
