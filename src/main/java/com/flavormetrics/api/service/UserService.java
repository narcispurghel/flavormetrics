package com.flavormetrics.api.service;

import com.flavormetrics.api.model.Data;
import com.flavormetrics.api.model.UserDto;
import com.flavormetrics.api.model.request.RegisterRequest;

public interface UserService {

    Data<UserDto> registerUser(RegisterRequest data);

}
