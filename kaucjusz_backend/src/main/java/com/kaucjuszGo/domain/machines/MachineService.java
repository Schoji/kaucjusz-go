package com.kaucjuszGo.domain.machines;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.locationtech.jts.geom.Point;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.kaucjuszGo.domain.machines.dto.MachineDetailsResponseDto;
import com.kaucjuszGo.domain.machines.dto.MachineListResponseDto;
import com.kaucjuszGo.domain.machines.dto.MachineWithDistanceProjection;
import com.kaucjuszGo.domain.machines.reviews.MachineReviewRepository;
import com.kaucjuszGo.domain.user.UserEntity;
import com.kaucjuszGo.domain.user.UserService;
import com.kaucjuszGo.util.LocationUtils;

@Service
public class MachineService {
    private final MachineReviewRepository machineReviewRepository;
    private final MachineRepository machineRepository;
    private final UserService userService;

    public MachineService(MachineRepository machineRepository, UserService userService,
            MachineReviewRepository machineReviewRepository) {
        this.machineRepository = machineRepository;
        this.userService = userService;
        this.machineReviewRepository = machineReviewRepository;
    }

    public MachineEntity getMachineById(UUID machineId) {
        return machineRepository.findById(machineId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Machine not found"));
    }

    public MachineDetailsResponseDto getMachineDetails(UUID id) {
        MachineEntity machine = machineRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Machine not found"));

        UserEntity user = userService.getCurrentUser();

        Integer userMachineRating = machineReviewRepository.findNewestMachineRatingById(id, user.getId());

        MachineDetailsResponseDto response = new MachineDetailsResponseDto();
        response.setId(machine.getId());
        response.setName(machine.getName());
        response.setAddress(machine.getAddress());
        response.setPhotoUrl(machine.getPhotoUrl());
        response.setOpeningHours(machine.getOpeningHours());
        response.setAverageRating(machine.getAverageRating());
        response.setReviewsCount(machine.getReviewsCount());
        response.setStatus(machine.getStatus());
        response.setUserRating(userMachineRating);

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
            dto.setReviewsCount(machine.getReviewsCount());
            dto.setStatus(machine.getStatus());
            return dto;

        }).collect(Collectors.toList());
    }

    public MachineEntity getMachineByIdForUpdate(UUID machineId) {
        return machineRepository.findByIdForUpdate(machineId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Machine not found"));
    }
}
