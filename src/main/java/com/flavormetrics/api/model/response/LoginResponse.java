package com.flavormetrics.api.model.response;

import java.util.List;

public record LoginResponse(String username, List<String> roles, String token) {
}
