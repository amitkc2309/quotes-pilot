package com.quotespilot.restcontroller;


import com.quotespilot.dto.UserDTO;
import com.quotespilot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class UserRestController {
    
    @Autowired
    private UserService userService;
    
    //http://localhost:8080/register/
    @PostMapping("/register")
    public ResponseEntity registerUser(@RequestBody UserDTO userDTO) throws Exception {
        Long id =userService.createUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("user created with id-"+id);
    }

    //http://localhost:8080/login/
    @PostMapping("login")
    public ResponseEntity login(@RequestBody UserDTO userDTO) throws Exception {
        String jwt=userService.login(userDTO);
        return ResponseEntity.ok(jwt);
    }

    //http://localhost:8080/logout/
    @GetMapping("/logout")
    public ResponseEntity logOut(HttpServletRequest request, HttpServletResponse response)  {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("User "+auth.getName()+" logged-out");
    }
    
}
