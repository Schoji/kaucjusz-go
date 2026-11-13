package com.kaucjuszGo.domain.machines.reviews;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.kaucjuszGo.domain.machines.MachineEntity;
import com.kaucjuszGo.domain.machines.MachineRepository;
import com.kaucjuszGo.domain.machines.MachineService;
import com.kaucjuszGo.domain.machines.dto.PageResponseDto;
import com.kaucjuszGo.domain.user.UserEntity;
import com.kaucjuszGo.domain.user.UserService;

import jakarta.transaction.Transactional;

@Service
public class MachineReviewService {
    private final MachineRepository machineRepository;
    private UserService userService;
    private MachineReviewRepository machineReviewRepository;
    private MachineService machineService;

    public MachineReviewService(MachineReviewRepository machineReviewRepository, UserService userService, MachineService machineService, MachineRepository machineRepository) {
        this.machineReviewRepository = machineReviewRepository;
        this.userService = userService;
        this.machineService = machineService;
        this.machineRepository = machineRepository;
    }

    @Transactional 
    public void addOrUpdateReview(UUID machineId, Integer rating, String comment) {
        UserEntity currentUser = userService.getCurrentUser();
        MachineEntity machine = machineService.getMachineById(machineId);

        Optional<MachineReviewEntity> existingReview = machineReviewRepository.findByUser_FirebaseUidAndMachine_Id(currentUser.getFirebaseId(), machineId);

        boolean isNew = existingReview.isEmpty();
        MachineReviewEntity review = existingReview.orElseGet(MachineReviewEntity::new);

        if (isNew) {
            review.setUser(currentUser);
            review.setMachine(machine);
            machine.setReviewsCount(machine.getReviewsCount() + 1);
        }
        review.setRating(rating);
        review.setComment(comment);
        machineReviewRepository.save(review);

        Double avg = machineReviewRepository.calculateAverageRatingByMachine_Id(machineId);
        if (avg != null) {
            machine.setAverageRating(BigDecimal.valueOf(avg).setScale(2, RoundingMode.HALF_UP));
        }

        machineRepository.save(machine);
    }

    public PageResponseDto<ReviewDto> getReviewsForMachine(UUID machineId, Pageable pageable){
        // Checks if machine exists
        machineService.getMachineById(machineId);

        Page<MachineReviewEntity> reviews = (machineReviewRepository.findByMachine_Id(machineId, pageable));


        Page<ReviewDto> reviewDtos = (reviews.map(review -> {
            ReviewDto reviewDto = new ReviewDto();
            reviewDto.setId(review.getId());
            reviewDto.setRating(review.getRating());
            reviewDto.setComment(review.getComment());
            reviewDto.setUser(review.getUser() != null ? review.getUser().getNickname() : null);
            reviewDto.setCreatedAt(review.getCreatedAt());
            return reviewDto;
        }));

        return PageResponseDto.from(reviewDtos);
    }
}
