package kz.adem.tasklist.service;

import kz.adem.tasklist.web.dto.auth.JwtRequest;
import kz.adem.tasklist.web.dto.auth.JwtResponse;

public interface AuthService {

    JwtResponse login(JwtRequest loginRequest);

    JwtResponse refresh(String refreshToken);
}
