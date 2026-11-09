package com.kaucjuszGo.domain.machines;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kaucjuszGo.domain.machines.dto.MachineDetailsResponseDto;
import com.kaucjuszGo.domain.machines.dto.MachineListResponseDto;
import com.kaucjuszGo.domain.machines.dto.ReviewRequestDto;
import com.kaucjuszGo.domain.machines.reviews.MachineReviewService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/vending-machines")
public class MachineController {
    private final MachineService machineService;
    private final MachineImportService machineImportService;
    private final MachineReviewService machineReviewService;

    public MachineController(MachineService machineService, MachineImportService machineImportService,
            MachineReviewService machineReviewService) {
        this.machineService = machineService;
        this.machineImportService = machineImportService;
        this.machineReviewService = machineReviewService;
    }

    // Get machine details by ID - KGO-40
    @GetMapping("/{id}")
    public ResponseEntity<MachineDetailsResponseDto> getMachine(@PathVariable UUID id,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        MachineDetailsResponseDto machineDetails = machineService.getMachineDetails(id, pageable);
        if (machineDetails == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(machineDetails);
    }

    // Get all machines - KGO-39
    @GetMapping("/search")
    public ResponseEntity<List<MachineListResponseDto>> getMachines(@RequestParam Double longitude,
            @RequestParam Double latitude, @RequestParam Double radius, @RequestParam(required = false) String query,
            @RequestParam(required = false) Boolean isOpen, @RequestParam(required = false) Boolean isTopRated,
            @RequestParam(required = false) Boolean isNearby) {
        List<MachineListResponseDto> machines = machineService.getMachines(longitude, latitude, radius, query, isOpen,
                isTopRated, isNearby);
        return ResponseEntity.ok(machines);
    }

    // Import machines from external source - KGO-38
    @PostMapping("/import")
    public ResponseEntity<Void> importMachines() {
        machineImportService.importMachinesFromExternalSource();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/reviews")
    public ResponseEntity<Void> addReview(@PathVariable("id") UUID machineId,
            @RequestBody @Valid ReviewRequestDto request) {
        machineReviewService.addOrUpdateReview(machineId, request.getRating(), request.getComment());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
