package org.lviv.crime.controller.user;

import lombok.AllArgsConstructor;
import org.lviv.crime.dto.user.UserCreateDto;
import org.lviv.crime.dto.user.UserDto;
import org.lviv.crime.dto.user.UserItemDto;
import org.lviv.crime.dto.user.UserUpdateDto;
import org.lviv.crime.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
@RequestMapping("users")
public class UserController {

    private final UserService userService;

    @PostMapping("sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UserDto> create(@RequestBody UserCreateDto userCreateDto) {
        return userService.create(userCreateDto);
    }

    @GetMapping
    public Flux<UserItemDto> findAll() {
        return userService.findAllItems();
    }

    @GetMapping("{userId}")
    public Mono<UserDto> find(@PathVariable Long userId) {
        return userService.findById(userId);
    }

    @GetMapping("current")
    public Mono<UserDto> getCurrentUser() {
        return userService.getCurrentUser();
    }

    @PutMapping("{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> update(@PathVariable Long userId, @RequestBody UserUpdateDto userUpdateDto) {
        return userService.update(userId, userUpdateDto);
    }

    @DeleteMapping("{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> remove(@PathVariable Long userId) {
        return userService.remove(userId);
    }
}
