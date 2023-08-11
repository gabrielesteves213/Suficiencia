package br.com.suficiencia.infra.auth.service;

import br.com.suficiencia.infra.dtos.AuthRequest;
import br.com.suficiencia.infra.dtos.AuthResponse;

public interface AuthService {

    AuthResponse loginAndReturnToken(AuthRequest authRequest);

}
