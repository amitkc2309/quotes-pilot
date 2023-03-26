package com.quotespilot.service;

import com.quotespilot.dto.Constants;
import com.quotespilot.dto.UserDTO;
import com.quotespilot.entity.User;
import com.quotespilot.repository.UserRepository;
import com.quotespilot.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class UserServiceImpl implements UserService{

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public User get(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public User findByName(String name) {
        return userRepository.findByName(name);
    }

    @Override
    public Long createUser(UserDTO dto) throws Exception {
        if(this.findByName(dto.getName()) !=null) throw new Exception("User already exist");
        String salt=BCrypt.gensalt();
        String hashedPassword = passwordEncoder.encode(dto.getPassword()+salt);
        User user =new User();
        user.setName(dto.getName());
        user.setHashedPassword(hashedPassword);
        user.setSalt(salt);
        user.setRole(Constants.ROLE_USER);
        return userRepository.save(user).getId();
    }
    
    public String login(UserDTO dto){
        UsernamePasswordAuthenticationToken token =new UsernamePasswordAuthenticationToken(dto.getName(),dto.getPassword());
        //UserAuthenticationProvider's authenticate() will be called
        authenticationManager.authenticate(token);
        // if there is no exception thrown from authentication manager,
        // we can generate a JWT token and give it to user.
        User savedUser=this.findByName(dto.getName());
        String jwt = jwtUtil.generate(savedUser);
        return jwt;
    }

    @Override
    public User checkCredentials(String inputUsername, String inputPassword){
        User savedUser=this.findByName(inputUsername);
        if(savedUser!=null){
            String savedHash = savedUser.getHashedPassword();
            String savedSalt = savedUser.getSalt();
            logger.info("inputPassword="+inputPassword+"-salt="+savedSalt+"-");
             /*Note that passwordEncoder.encode always returns different result even with same input 
             so equals() will always return false and below 2 lines will not work:-*/
            //String generatedHash= passwordEncoder.encode(inputPassword+savedSalt);
            //if(generatedHash.equals(savedHash)) return true;
             /*To fix this Use matches() instead:-*/
            if(passwordEncoder.matches(inputPassword+savedSalt,savedHash)) return savedUser;
        }
        else{
            logger.warn("*****************User does not exist");
        }
        logger.warn("**************************Credentials Check failed");
        return null;
    }
}
