package hexlet.code.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@Setter

public class AuthResponseDto implements Serializable {

    private final String token;

}
