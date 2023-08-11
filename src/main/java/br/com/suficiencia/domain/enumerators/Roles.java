package br.com.suficiencia.domain.enumerators;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum Roles {

    ADMIN("ADMIN"), USER("USER");

    private String description;

    Roles(String description) {
        this.description = description;
    }

    public static List<Roles> getRolesAsList() {return Arrays.asList(values());}
}
