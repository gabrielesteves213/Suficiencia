package br.com.suficiencia.infra.dtos;

import br.com.suficiencia.domain.enumerators.Roles;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private String token;
    private Long id;
    private String username;
    private Roles role;
}
