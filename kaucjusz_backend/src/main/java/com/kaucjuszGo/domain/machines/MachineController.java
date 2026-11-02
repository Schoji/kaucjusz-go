package com.kaucjuszGo.domain.machines;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/vending-machines")
public class MachineController {
    private final MachineService machineService;

    public MachineController(MachineService machineService) {
        this.machineService = machineService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<MachineDetailsResponseDto> getMachine(@PathVariable UUID id, @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        MachineDetailsResponseDto machineDetails = machineService.getMachine(id, pageable);
        if (machineDetails == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(machineDetails);
    }
    
}
