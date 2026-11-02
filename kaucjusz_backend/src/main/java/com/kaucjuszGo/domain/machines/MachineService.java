package com.kaucjuszGo.domain.machines;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.kaucjuszGo.domain.machines.reviews.MachineReviewEntity;
import com.kaucjuszGo.domain.machines.reviews.MachineReviewRepository;
import com.kaucjuszGo.domain.machines.reviews.ReviewDto;

@Service
public class MachineService {
    private final MachineRepository machineRepository;
    private final MachineReviewRepository machineReviewRepository;

    public MachineService(MachineRepository machineRepository, MachineReviewRepository machineReviewRepository) {
        this.machineRepository = machineRepository;
        this.machineReviewRepository = machineReviewRepository;
    }

    public MachineDetailsResponseDto getMachine(UUID id, Pageable pageable) {
        MachineEntity machine = machineRepository.findById(id).orElse(null);
        
        if (machine == null) {
            return null;
        }

        Page<MachineReviewEntity> reviews = machineReviewRepository.findByMachine_Id(id, pageable);

        MachineDetailsResponseDto response = new MachineDetailsResponseDto();
        response.setId(machine.getId());
        response.setName(machine.getName());
        response.setAddress(machine.getAddress());
        response.setPhotoUrl(machine.getPhotoUrl());
        response.setOpeningHours(machine.getOpeningHours());
        response.setAverageRating(machine.getAverageRating());
        response.setReviewsCount(machine.getReviewsCount());
        response.setStatus(machine.getStatus());
        response.setReviews(reviews.map(review -> {
            ReviewDto reviewDto = new ReviewDto();
            reviewDto.setId(review.getId());
            reviewDto.setRating(review.getRating());
            reviewDto.setComment(review.getComment());
            reviewDto.setUser(review.getUser() != null ? review.getUser().getNickname() : null);
            reviewDto.setCreatedAt(review.getCreatedAt());
            return reviewDto;
        }));
        
        return response;
    }
}
