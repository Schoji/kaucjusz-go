package com.kaucjuszGo.domain.machines;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.kaucjuszGo.domain.machines.dto.MachineDetailsResponseDto;
import com.kaucjuszGo.domain.machines.dto.MachineListResponseDto;
import com.kaucjuszGo.domain.machines.dto.MachineWithDistanceProjection;
import com.kaucjuszGo.domain.machines.dto.PageResponseDto;
import com.kaucjuszGo.domain.machines.reviews.MachineReviewEntity;
import com.kaucjuszGo.domain.machines.reviews.MachineReviewRepository;
import com.kaucjuszGo.domain.machines.reviews.ReviewDto;
import com.kaucjuszGo.util.LocationUtils;

@Service
public class MachineService {
    private final MachineRepository machineRepository;
    private final MachineReviewRepository machineReviewRepository;

    public MachineService(MachineRepository machineRepository, MachineReviewRepository machineReviewRepository) {
        this.machineRepository = machineRepository;
        this.machineReviewRepository = machineReviewRepository;
    }

    public MachineEntity getMachineById(UUID machineId){
        return machineRepository.findById(machineId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Machine not found"));    }

    public MachineDetailsResponseDto getMachineDetails(UUID id, Pageable pageable) {
        MachineEntity machine = machineRepository.findById(id).orElse(null);

        if (machine == null) {
            return null;
        }

        Page<MachineReviewEntity> reviews = machineReviewRepository.findByMachine_Id(id, pageable);

        Page<ReviewDto> reviewDtos = (reviews.map(review -> {
            ReviewDto reviewDto = new ReviewDto();
            reviewDto.setId(review.getId());
            reviewDto.setRating(review.getRating());
            reviewDto.setComment(review.getComment());
            reviewDto.setUser(review.getUser() != null ? review.getUser().getNickname() : null);
            reviewDto.setCreatedAt(review.getCreatedAt());
            return reviewDto;
        }));

        MachineDetailsResponseDto response = new MachineDetailsResponseDto();
        response.setId(machine.getId());
        response.setName(machine.getName());
        response.setAddress(machine.getAddress());
        response.setPhotoUrl(machine.getPhotoUrl());
        response.setOpeningHours(machine.getOpeningHours());
        response.setAverageRating(machine.getAverageRating());
        response.setReviewsCount(machine.getReviewsCount());
        response.setStatus(machine.getStatus());
        response.setReviews(PageResponseDto.from(reviewDtos));

        return response;
    }

    public List<MachineListResponseDto> getMachines(Double latitude, Double longitude, Double radius,
            String query,
            Boolean isOpen, Boolean isTopRated) {

        Point position = LocationUtils.createPoint(latitude, longitude);
        String queryPattern = (query != null && !query.isBlank()) ? "%" + query.toLowerCase().trim() + "%" : null;

        List<MachineWithDistanceProjection> machineProjections = machineRepository.findMachinesWithFilters(position,
                radius, queryPattern, isOpen, isTopRated);

        return machineProjections.stream().map(machine -> {
            MachineListResponseDto dto = new MachineListResponseDto();
            dto.setId(machine.getId());
            dto.setName(machine.getName());
            dto.setAddress(machine.getAddress());
            dto.setPhotoUrl(machine.getPhotoUrl());
            dto.setLatitude(machine.getLatitude());
            dto.setLongitude(machine.getLongitude());
            dto.setAverageRating(machine.getAverageRating());
            dto.setDistance(machine.getDistance());
            return dto;

        }).collect(Collectors.toList());
    }
}
