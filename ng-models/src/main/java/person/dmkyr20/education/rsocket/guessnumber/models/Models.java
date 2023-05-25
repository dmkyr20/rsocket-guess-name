package person.dmkyr20.education.rsocket.guessnumber.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.rsocket.Payload;

import java.io.IOException;

public class Models {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static GuessNumberRequest toRequest(Payload payload) {
        return toRequest(payload.getData().array());
    }

    public static GuessNumberRequest toRequest(byte[] data) {
        try {
            return MAPPER.readValue(data, GuessNumberRequest.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static GuessNumberResponse toResponse(Payload payload) {
        return toResponse(payload.getData().array());
    }

    public static GuessNumberResponse toResponse(byte[] data) {
        try {
            return MAPPER.readValue(data, GuessNumberResponse.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ValidationModel toValidation(Payload payload) {
        return toValidation(payload.getData().array());
    }

    public static ValidationModel toValidation(byte[] data) {
        try {
            return MAPPER.readValue(data, ValidationModel.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] toBytes(Object response) {
        try {
            return MAPPER.writeValueAsBytes(response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
