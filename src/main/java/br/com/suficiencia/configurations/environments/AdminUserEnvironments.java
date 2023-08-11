package br.com.suficiencia.configurations.environments;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@NoArgsConstructor
@ConfigurationProperties(prefix = "admin.user")
public class AdminUserEnvironments {

    private String login;
    private String password;
}
