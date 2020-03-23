package com.ncedu.user_manager.controller.v1;

import com.ncedu.user_manager.model.dto.LoginPasswordDTO;
import com.ncedu.user_manager.model.dto.TokenDTO;
import com.ncedu.user_manager.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"Logout"})
@RestController
@RequestMapping("${api.prefix.v1}/logout")
@RequiredArgsConstructor
public class LogoutController {

    private final UserService userService;

    @ApiOperation(httpMethod = "POST", value = "Logout")
    @PostMapping
    public void logout() {
        userService.logout();
    }

}
