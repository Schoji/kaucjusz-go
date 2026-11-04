package com.kaucjuszGo.domain.machines;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kaucjuszGo.domain.machines.dto.MachineDetailsResponseDto;
import com.kaucjuszGo.domain.machines.dto.MachineListResponseDto;

@RestController
@RequestMapping("/api/vending-machines")
public class MachineController {
    private final MachineService machineService;
    private final MachineImportService machineImportService;

    public MachineController(MachineService machineService, MachineImportService machineImportService) {
        this.machineService = machineService;
        this.machineImportService = machineImportService;
    }

    // Get machine details by ID - KGO-40
    @GetMapping("/{id}")
    public ResponseEntity<MachineDetailsResponseDto> getMachine(@PathVariable UUID id,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        MachineDetailsResponseDto machineDetails = machineService.getMachine(id, pageable);
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
    public ResponseEntity<String> importMachines() {
        machineImportService.importMachinesFromExternalSource();
        return ResponseEntity.ok("Machines imported successfully.");
    }
}
