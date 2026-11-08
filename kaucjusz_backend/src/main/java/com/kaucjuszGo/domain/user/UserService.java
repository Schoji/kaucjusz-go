package com.kaucjuszGo.domain.user;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service 
public class UserService {
    private final FirebaseUserService firebaseUserService;
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository, FirebaseUserService firebaseUserService){
        this.userRepository = userRepository;
        this.firebaseUserService = firebaseUserService;
    }

    public UserEntity getCurrentUser(){
        String firebaseUid = firebaseUserService.getCurrentUserId();

        UserEntity user = userRepository.findUserByFirebaseUid(firebaseUid).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found in database"));
    
        return user;
    }
}
