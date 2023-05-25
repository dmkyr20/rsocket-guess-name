package person.dmkyr20.education.rsocket.guessnumber.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ValidationModel {
    private UUID code;
}
