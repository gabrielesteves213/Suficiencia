package br.com.suficiencia.domain.repositories;

import br.com.suficiencia.domain.entities.Role;
import br.com.suficiencia.domain.enumerators.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Boolean existsByRoleName(Roles roleName);

}
