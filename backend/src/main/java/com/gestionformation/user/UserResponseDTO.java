package com.gestionformation.user;

import lombok.*;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class UserResponseDTO {
    private Long    id;
    private String  username;
    private String  email;
    private String  role;
    private boolean enabled;
}
