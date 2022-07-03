package com.gilog.service;

import com.gilog.dto.UserDto;
import com.gilog.entity.User;
import com.gilog.repository.UserRepository;
import com.gilog.repository.UserRepositoryInt;
import com.gilog.vo.UserFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<UserDto> getUserList(UserFilter userFilter) {
        List<User> users = userRepository.getUserList(userFilter);
        List<UserDto> usersDto = new ArrayList<>();

        for (User user : users) {
            usersDto.add(
                    UserDto.builder()
                            .id(user.getId())
                            .nickname(user.getNickname())
                            .username(user.getUsername())
                            .age(user.getAge())
                            .gender(user.getGender())
                            .regDatetime(user.getRegDatetime())
                            .build()
            );
        }
        return usersDto;
    }
}
