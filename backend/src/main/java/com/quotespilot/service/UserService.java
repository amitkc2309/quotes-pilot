package com.quotespilot.service;

import com.quotespilot.dto.UserDTO;
import com.quotespilot.entity.User;

public interface UserService {
    User get(Long id);
    void save(User user);
    User findByName(String name);
    Long createUser(UserDTO user) throws Exception;
    public String login(UserDTO dto);
    User checkCredentials(String inputUsername, String inputPassword);
}
