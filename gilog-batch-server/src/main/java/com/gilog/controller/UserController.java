package com.gilog.controller;

import com.gilog.dto.UserDto;
import com.gilog.service.UserService;
import com.gilog.vo.UserFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/user")
    public String userListPage() {
        return "user/userList";
    }

    @ResponseBody
    @GetMapping("/users")
    public List<UserDto> getUserData(@RequestParam(required = false) String searchKey, @RequestParam(required = false) String searchValue) {
        UserFilter filter = UserFilter.builder()
                .searchKey(searchKey)
                .searchValue(searchValue)
                .build();
        return userService.getUserList(filter);
    }

}
