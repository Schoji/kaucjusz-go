package com.kaucjuszGo.domain.user;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSyncResponseDto {
    private UUID id;
    private String firebaseUid;
    private String email;
    private String nickname;
    private String region;
    private Integer currentPoints;
    private Integer lifetimePoints;
    private Integer currentStreak;
    private Boolean isNewUser;
}
