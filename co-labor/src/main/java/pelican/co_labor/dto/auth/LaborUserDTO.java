package pelican.co_labor.dto.auth;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LaborUserDTO {
    private String username;
    private String password;
    private String passwordConfirm;
    private String email;
    private String name;
}
