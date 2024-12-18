package com.validationlogin.services;

import com.validationlogin.entities.User;
import com.validationlogin.repository.UserRepo;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class AuthService {



public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

        private final UserRepo userRepository;
        private final PasswordEncoder passwordEncoder;

        //private String secretKey;
        private  final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

          public SecretKey getSecretKey() {
           return secretKey;
           }


        @Autowired
        public AuthService(UserRepo userRepository) {
              this.userRepository = userRepository;
              this.passwordEncoder=new BCryptPasswordEncoder();
        }


   // Method to encode password
    public String encodePassword(String password) {
            return passwordEncoder.encode(password);
    }

        // Authenticate user and generate JWT token
        public String authenticateUser(String username, String password) {
            User user = userRepository.findByUsername(username);


/*
            if (user != null && passwordEncoder.matches(password, user.getPassword())) {
*/

            if (user != null && user.getPassword().equals(password)) {
                return generateToken(user);
            }
            else {
                return null; // Authentication failed
            }
        }

        // Generate JWT token
        private String generateToken(User user) {

      /*      Map<String, Object> header = new HashMap<>();
            header.put("username", username1);
            header.put("password",password1);*/


            return Jwts.builder()
                    .setSubject(user.getUsername())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // Token expires in 1 hour
/*
                    .setHeader(header)
*/
                    .signWith(SignatureAlgorithm.HS256, secretKey)
                    .compact();


        }
    }



