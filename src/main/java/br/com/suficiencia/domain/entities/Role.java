package br.com.suficiencia.domain.entities;

import br.com.suficiencia.domain.enumerators.Roles;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@Table(name = "Roles", schema = "public")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Roles roleName;

    public Role(Long id, Roles roleName) {
        this.id = id;
        this.roleName = roleName;
    }

    public Role() {}

    public Role(Roles roleName) {
        this.roleName = roleName;
    }

    @Override
    public String getAuthority() {
        return null;
    }
}
