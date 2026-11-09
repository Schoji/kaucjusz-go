package com.kaucjuszGo.domain.user;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.kaucjuszGo.domain.pet.PetEntity;

import jakarta.transaction.Transactional;

@Service
public class UserService {
    private final FirebaseUserService firebaseUserService;
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository, FirebaseUserService firebaseUserService) {
        this.userRepository = userRepository;
        this.firebaseUserService = firebaseUserService;
    }

    public UserEntity getCurrentUser() {
        String firebaseUid = firebaseUserService.getCurrentUserId();

        UserEntity user = userRepository.findUserByFirebaseUid(firebaseUid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found in database"));

        return user;
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

            PetEntity petEntity = new PetEntity();
            petEntity.setUser(userEntity);
            userEntity.setPet(petEntity);

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
