package org.ibtissam.induspress.dto.auth;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {

    private String firstname;

    private String lastname;

    private String email;

    private String password;
}
