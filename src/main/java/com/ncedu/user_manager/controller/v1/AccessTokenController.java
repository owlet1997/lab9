package com.ncedu.user_manager.controller.v1;

import com.ncedu.user_manager.model.dto.TokenDTO;
import com.ncedu.user_manager.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"Token"})
@RestController
@RequestMapping("${api.prefix.v1}/accessToken")
@RequiredArgsConstructor
public class AccessTokenController {

    private final UserService userService;

    @ApiOperation(httpMethod = "GET", value = "Get access token")
    @GetMapping
    @PreAuthorize("hasAuthority('REFRESH_ACCESS')")
    public TokenDTO getAccessToken() {
        return userService.getOrRefreshAccessToken();
    }

}
