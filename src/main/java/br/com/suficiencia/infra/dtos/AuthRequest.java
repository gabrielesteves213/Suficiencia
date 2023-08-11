package br.com.suficiencia.infra.dtos;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthRequest {

    private String login;
    private String password;

    @Builder.Default
    private Boolean rememberMe = false;

}
