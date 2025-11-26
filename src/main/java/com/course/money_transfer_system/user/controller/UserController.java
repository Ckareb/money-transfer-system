package com.course.money_transfer_system.user.controller;

import com.course.money_transfer_system.transfer.model.ResponseInfo;
import com.course.money_transfer_system.user.dto.UserAccessDto;
import com.course.money_transfer_system.user.dto.UserDataDto;
import com.course.money_transfer_system.user.dto.UserDto;
import com.course.money_transfer_system.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Операции с пользователями")
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Список истории транзакций")
    @GetMapping("/pages")
    public Page<UserDto> getUsers(@PageableDefault(size = 20) Pageable pageable) {
        return userService.getUsers(pageable);
    }

    @Operation(summary = "Просмотр данных счета")
    @GetMapping(path = "/{id}")
    public UserDto getUserDto(@PathVariable Long id) {
        return  userService.getUserDto(id);
    }

    @Operation(summary = "Создание счета")
    @PostMapping(path = "/create")
    public ResponseEntity<ResponseInfo> createUser(@RequestBody UserDto dto) {
        return userService.createUser(dto);
    }

    @Operation(summary = "Изменение данных счета")
    @PutMapping(path = "/change")
    public ResponseEntity<ResponseInfo> changeUserData(@RequestBody UserDataDto dto) {
        return userService.changeUserData(dto);
    }

    @Operation(summary = "Закрытие аккаунта")
    @PutMapping(path = "/close/{id}")
    public ResponseEntity<ResponseInfo> closeUser(@PathVariable Long id) {
        return userService.closeUser(id);
    }

    @Operation(summary = "Доступ к операциям без id")
    @GetMapping(path = "/access")
    public UserAccessDto getAccess() {
        return userService.getAccess();
    }
}
