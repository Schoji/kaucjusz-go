package com.kaucjuszGo.domain.machines.reviews;

import java.math.RoundingMode;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kaucjuszGo.domain.machines.MachineEntity;
import com.kaucjuszGo.domain.machines.MachineRepository;
import com.kaucjuszGo.domain.machines.MachineService;
import com.kaucjuszGo.domain.machines.dto.PageResponseDto;
import com.kaucjuszGo.domain.user.UserEntity;
import com.kaucjuszGo.domain.user.UserService;

@Service
public class MachineReviewService {
    private final MachineRepository machineRepository;
    private UserService userService;
    private MachineReviewRepository machineReviewRepository;
    private MachineService machineService;

    public MachineReviewService(MachineReviewRepository machineReviewRepository, UserService userService,
            MachineService machineService, MachineRepository machineRepository) {
        this.machineReviewRepository = machineReviewRepository;
        this.userService = userService;
        this.machineService = machineService;
        this.machineRepository = machineRepository;
    }

    @Transactional
    public void addReview(UUID machineId, Integer rating, String comment) {
        UserEntity currentUser = userService.getCurrentUser();
        MachineEntity machine = machineService.getMachineByIdForUpdate(machineId);

        MachineReviewEntity review = new MachineReviewEntity();

        String normalizedComment = (comment != null && !comment.isBlank()) ? comment.trim() : null;

        review.setUser(currentUser);
        review.setMachine(machine);
        review.setComment(normalizedComment);
        review.setRating(rating);

        machineReviewRepository.saveAndFlush(review);

        MachineRatingStatsProjection stats = machineReviewRepository.calculateRatingStatsByMachineId(machineId);
        if (stats != null) {
            machine.setAverageRating(stats.getAverageRating().setScale(2, RoundingMode.HALF_UP));
            machine.setReviewsCount(stats.getReviewsCount().intValue());
        }

        machineRepository.save(machine);
    }

    @Transactional(readOnly = true)
    public PageResponseDto<ReviewDto> getReviewsForMachine(UUID machineId, Pageable pageable) {
        machineService.getMachineById(machineId);
        UserEntity currentUser = userService.getCurrentUser();

        Pageable unsortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        Page<ReviewProjection> reviews = machineReviewRepository.findPrioritizedReviewsByMachineId(
                machineId, currentUser.getId(), unsortedPageable);

        return PageResponseDto.from(reviews.map(this::toReviewDto));
    }

    private ReviewDto toReviewDto(ReviewProjection review) {
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setId(review.getId());
        reviewDto.setRating(review.getRating());
        reviewDto.setComment(review.getComment());
        reviewDto.setUser(review.getUserNickname());
        reviewDto.setCreatedAt(review.getCreatedAt());
        reviewDto.setIsOwn(review.getIsOwn());
        return reviewDto;
    }
}
