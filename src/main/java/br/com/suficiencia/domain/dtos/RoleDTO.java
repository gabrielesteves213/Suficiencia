package br.com.suficiencia.domain.dtos;

import br.com.suficiencia.domain.enumerators.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {

    private Roles roleName;

}
