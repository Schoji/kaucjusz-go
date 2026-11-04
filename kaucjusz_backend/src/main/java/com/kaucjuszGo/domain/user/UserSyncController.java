package com.kaucjuszGo.domain.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserSyncController {
    private final UserSyncService userSyncService;
    private final FirebaseUserService firebaseUserService;

    public UserSyncController(UserSyncService userSyncService, FirebaseUserService firebaseUserService) {
        this.userSyncService = userSyncService;
        this.firebaseUserService = firebaseUserService;
    }

    @PostMapping("/sync")
    public ResponseEntity<UserSyncResponseDto> syncUser(
            @RequestParam String nickname,
            @RequestParam String region) {

        String firebaseUserId = firebaseUserService.getCurrentUserId();
        String email = firebaseUserService.getCurrentUserEmail();

        UserSyncResponseDto response = userSyncService.syncUser(firebaseUserId, nickname, email, region);

        if (response.getIsNewUser()) {
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

}
