package com.ncedu.user_manager.controller.v1;

import com.ncedu.user_manager.model.dto.NewUserDTO;
import com.ncedu.user_manager.model.dto.UserDTO;
import com.ncedu.user_manager.service.UserService;
import com.ncedu.user_manager.util.OffsetBasedPageRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Api(tags = {"User"})
@RestController
@RequestMapping("${api.prefix.v1}/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @ApiOperation(httpMethod = "GET", value = "Find users")
    @GetMapping
    @PreAuthorize("hasAuthority('USER_ADMIN')")
    public List<UserDTO> find() {
        return userService.find(new OffsetBasedPageRequest(null, null));
    }

    @ApiOperation(httpMethod = "POST", value = "Create user")
    @PostMapping
    @PreAuthorize("hasAuthority('USER_ADMIN')")
    public UserDTO create(@RequestBody NewUserDTO newUserDTO) {
        return userService.create(newUserDTO);
    }

    @ApiOperation(httpMethod = "PUT", value = "Edit user")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_ADMIN')")
    public UserDTO update(@PathVariable UUID id, @RequestBody UserDTO userDTO) {
        return userService.update(id, userDTO);
    }

    @ApiOperation(httpMethod = "DELETE", value = "Delete user")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_ADMIN')")
    public void delete(@PathVariable UUID id) {
        userService.delete(id);
    }

    @ApiOperation(httpMethod = "POST", value = "Add roles to user")
    @PostMapping("/{id}/addRoles")
    @PreAuthorize("hasAuthority('USER_ADMIN')")
    public UserDTO addRoles(@PathVariable UUID id, @RequestBody List<String> roleCodes) {
        return userService.addRoles(id,roleCodes);
    }

    @ApiOperation(httpMethod = "POST", value = "Remove roles from user")
    @PostMapping("/{id}/removeRoles")
    @PreAuthorize("hasAuthority('USER_ADMIN')")
    public UserDTO removeRoles(@PathVariable UUID id, @RequestBody List<String> roleCodes) {
        return userService.removeRoles(id, roleCodes);
    }

}
