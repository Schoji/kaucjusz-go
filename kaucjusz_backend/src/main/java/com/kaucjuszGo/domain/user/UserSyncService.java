package com.kaucjuszGo.domain.user;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserSyncService {
    private final UserRepository userRepository;

    public UserSyncService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserSyncResponseDto syncUser(String firebaseUserId, String nickname, String email, String region) {
        UserEntity userEntity = userRepository.findUserByFirebaseUid(firebaseUserId).orElse(null);

        UserSyncResponseDto userDto = new UserSyncResponseDto();

        if (userEntity == null) {
            if (userRepository.existsByNickname(nickname)) {
                throw new org.springframework.web.server.ResponseStatusException(
                        HttpStatus.CONFLICT, "Nickname is already taken");
            }
            userEntity = new UserEntity();
            userEntity.setFirebaseId(firebaseUserId);
            userEntity.setNickname(nickname);
            userEntity.setEmail(email);
            userEntity.setRegion(region);

            userRepository.save(userEntity);

            userDto.setIsNewUser(true);

        } else {
            userDto.setIsNewUser(false);
        }

        userDto.setId(userEntity.getId());
        userDto.setFirebaseUid(userEntity.getFirebaseId());
        userDto.setEmail(userEntity.getEmail());
        userDto.setNickname(userEntity.getNickname());
        userDto.setRegion(userEntity.getRegion());
        userDto.setCurrentPoints(userEntity.getCurrentPoints());
        userDto.setLifetimePoints(userEntity.getLifetimePoints());
        userDto.setCurrentStreak(userEntity.getCurrentStreak());

        return userDto;
    }
}
