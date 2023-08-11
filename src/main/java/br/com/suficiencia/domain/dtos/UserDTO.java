package br.com.suficiencia.domain.dtos;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;

    @NotNull
    private String login;

    private String password;

    private Set<RoleDTO> roles;

}
