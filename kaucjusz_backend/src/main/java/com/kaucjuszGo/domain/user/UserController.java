package com.kaucjuszGo.domain.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final FirebaseUserService firebaseUserService;

    public UserController(UserService userService, FirebaseUserService firebaseUserService) {
        this.userService = userService;
        this.firebaseUserService = firebaseUserService;
    }

    @PostMapping("/sync")
    public ResponseEntity<UserSyncResponseDto> syncUser(
            @RequestBody UserSyncRequestDto userRequest) {

        String firebaseUserId = firebaseUserService.getCurrentUserId();
        String email = firebaseUserService.getCurrentUserEmail();

        UserSyncResponseDto response = userService.syncUser(firebaseUserId, userRequest.getNickname(), email,
                userRequest.getRegion());

        if (response.getIsNewUser()) {
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

}
