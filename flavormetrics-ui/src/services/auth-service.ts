import { BadCredentialsError, ConflictError } from "@/errors";
import type { UUID } from "crypto";

const API_BASE_URL = "http://localhost:8080/api/v1";

export interface LoginResponse {
  email: string;
  id: UUID;
  isAccountNonExpired: boolean;
  isAccountNonLocked: boolean;
  isCredentialsNonExpired: boolean;
  isEnabled: boolean;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export const fetchLogin = async (
  body: LoginRequest
): Promise<LoginResponse> => {
  const response = await fetch(`${API_BASE_URL}/auth/login`, {
    method: "post",
    body: JSON.stringify(body),
    headers: {
      "Content-Type": "application/json",
    },
  });

  if (response.status === 401) {
    throw new BadCredentialsError("Bad credentials");
  }

  if (!response.ok) {
    throw new Error(`HTTP error! Status: ${response.status}`);
  }

  const data: LoginResponse = await response.json();
  return data;
};

export declare enum RoleType {
  ROLE_ADMIN,
  ROLE_USER,
}

export interface RegisterRequest {
  email: string;
  firstName: string;
  lastName: string;
  password: string;
}

export interface RegisterResponse {
  id: UUID;
  firstName: string;
  lastName: string;
  password: string;
  role: RoleType;
}

export const register = async (
  body: RegisterRequest
): Promise<RegisterResponse> => {
  const res = await fetch(`${API_BASE_URL}/auth/signup`, {
    method: "POST",
    body: JSON.stringify(body),
    headers: {
      "Content-Type": "application/json",
    },
  });

  if (res.status === 409) {
    throw new ConflictError()
  }

  if (!res.ok) {
    throw new Error(`HTTP error! Status: ${res.status}`);
  }

  const data: RegisterResponse = await res.json();

  return data;
};
