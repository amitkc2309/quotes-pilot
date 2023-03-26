package com.quotespilot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    
    @InjectMocks
    BCryptPasswordEncoder passwordEncoder;
    
    @Test
    public void testPasswordHashing(){
        String hashedPassword=passwordEncoder.encode("admin"+"salt123");
        boolean isMatched=passwordEncoder.matches("admin"+"salt123","$2a$10$CQkYdHqrPBnGHDaLBGWI5O.6nDkyBrhuKP0WdB0YL2bQdzHlXLIOi");
        assertEquals(true,isMatched);
        boolean isMatched2=passwordEncoder.matches("wrongPasswordEntered"+"salt123","$2a$10$CQkYdHqrPBnGHDaLBGWI5O.6nDkyBrhuKP0WdB0YL2bQdzHlXLIOi");
        assertEquals(false,isMatched2);
    }
}
