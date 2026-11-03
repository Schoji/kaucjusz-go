package com.kaucjuszGo.domain.machines;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.client.RestClient;

import com.kaucjuszGo.domain.machines.dto.ExternalMachineDto;
import com.kaucjuszGo.util.LocationUtils;

import jakarta.transaction.Transactional;

@Service
public class MachineImportService {
    private final MachineRepository machineRepository;

    public MachineImportService(MachineRepository machineRepository) {
        this.machineRepository = machineRepository;
    }

    @Transactional
    public void importMachinesFromExternalSource() {
        RestClient restClient = RestClient.create();

        List<ExternalMachineDto> externalMachines = restClient.get()
                .uri("https://mapakaucji.pl/data/kaucjomaty_polska.json")
                .retrieve()
                .body(new ParameterizedTypeReference<List<ExternalMachineDto>>() {});
        
        Set<String> existingExternalIds = machineRepository.findAllExternalIds();

        List<MachineEntity> machinesToSave = new ArrayList<>();

        for (ExternalMachineDto externalMachine : externalMachines) {

            if (externalMachine.getLatitude() == null || externalMachine.getLongitude() == null) {
                continue;
            }

            // Maybe if there is a record update data that is different, but for now just skip if the machine already exists
            String externalMachineId = DigestUtils.md5DigestAsHex((externalMachine.getBrand() + "_" + externalMachine.getCity() + "_" + externalMachine.getAddress() + "_" + externalMachine.getLatitude().toString() + "_" + externalMachine.getLongitude().toString()).getBytes(StandardCharsets.UTF_8));
            
            if (existingExternalIds.contains(externalMachineId)) {
                continue; // Skip if the machine already exists in the database
            }

            MachineEntity machineEntity = new MachineEntity();
            machineEntity.setExternalId(externalMachineId);
            machineEntity.setName(externalMachine.getBrand());
            machineEntity.setGeolocation(LocationUtils.createPoint(externalMachine.getLatitude(), externalMachine.getLongitude()));
            machineEntity.setBrand(externalMachine.getBrand());
            machineEntity.setCity(externalMachine.getCity());
            machineEntity.setAddress(externalMachine.getAddress());
            machineEntity.setPostalCode(externalMachine.getPostalCode());
            
            machinesToSave.add(machineEntity);
            existingExternalIds.add(externalMachineId);
        }
        machineRepository.saveAll(machinesToSave);
    }
}