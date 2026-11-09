package com.kaucjuszGo.domain.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSyncRequestDto {
    private String nickname;
    private String region;
}
