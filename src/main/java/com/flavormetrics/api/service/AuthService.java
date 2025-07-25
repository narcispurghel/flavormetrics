package com.flavormetrics.api.service;

import com.flavormetrics.api.model.UserDetailsImpl;
import com.flavormetrics.api.model.request.LoginRequest;
import com.flavormetrics.api.model.request.RegisterRequest;
import com.flavormetrics.api.model.response.RegisterResponse;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
  RegisterResponse signup(RegisterRequest request);

  UserDetailsImpl authenticate(LoginRequest req, HttpServletResponse res);

  String logout(HttpServletResponse response);
}
