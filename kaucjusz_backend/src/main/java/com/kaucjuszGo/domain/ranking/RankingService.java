package com.kaucjuszGo.domain.ranking;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.kaucjuszGo.domain.machines.dto.PageResponseDto;
import com.kaucjuszGo.domain.pet.PetEntity;
import com.kaucjuszGo.domain.pet.PetRepository;
import com.kaucjuszGo.domain.ranking.dto.RankingEntryDto;
import com.kaucjuszGo.domain.ranking.dto.RankingResponseDto;
import com.kaucjuszGo.domain.user.UserEntity;
import com.kaucjuszGo.domain.user.UserService;

@Service
public class RankingService {
    private PetRepository petRepository;
    private UserService userService;

    RankingService(PetRepository petRepository, UserService userService){
        this.petRepository = petRepository; 
        this.userService = userService;
    }

    private RankingEntryDto mapToEntryDto(PetEntity pet, long rank){
            RankingEntryDto rankingEntryDto = new RankingEntryDto();
            rankingEntryDto.setExperience(pet.getExperience());
            rankingEntryDto.setLevel(pet.getLevel());
            rankingEntryDto.setNickname(pet.getUser().getNickname());
            rankingEntryDto.setRank(rank);
            rankingEntryDto.setRegion(pet.getUser().getRegion());
            rankingEntryDto.setUserId(pet.getUser().getId());
            return rankingEntryDto;
    }

    public RankingResponseDto getPetRanking(Pageable pageable, boolean isLocal){
        Sort rankingSort = Sort.by(
            Sort.Order.desc(PetEntity::getLevel),
            Sort.Order.desc(PetEntity::getExperience),
            Sort.Order.asc(PetEntity::getCreatedAt)
        );
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), rankingSort);

        UserEntity currentUser = userService.getCurrentUser();
        PetEntity currentUserPet = currentUser.getPet();
        Page<PetEntity> petEntities;
        Long currentUserRanking;

        if (isLocal){
            String region = currentUser.getRegion();

            if (region == null || region.isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User region is not set");
            }

            petEntities = petRepository.findByUser_DeletedAtIsNullAndUser_RegionIgnoreCase(region, sortedPageable);
            currentUserRanking = petRepository.countPlayersAheadInRegion(region, currentUserPet.getLevel(), currentUserPet.getXp(), currentUserPet.getCreatedAt()) + 1;
        }
        else{
            petEntities = petRepository.findByUser_DeletedAtIsNull(sortedPageable);
            currentUserRanking = petRepository.countPlayersAhead(currentUserPet.getLevel(), currentUserPet.getXp(), currentUserPet.getCreatedAt()) + 1;
        }

        AtomicLong rankCounter = new AtomicLong((long) sortedPageable.getPageNumber() * sortedPageable.getPageSize());
        Page<RankingEntryDto> mappedleaderboard = (petEntities.map(pet -> {
            return mapToEntryDto(pet, rankCounter.incrementAndGet());
        }));

        PageResponseDto<RankingEntryDto> leaderboardEntryDto = PageResponseDto.from(mappedleaderboard);

        RankingEntryDto CurrentUserRankEntryDto = mapToEntryDto(currentUserPet, currentUserRanking);

        RankingResponseDto rankingResponseDto = new RankingResponseDto();
        rankingResponseDto.setCurrentUser(CurrentUserRankEntryDto);
        rankingResponseDto.setLeaderboard(leaderboardEntryDto);  
        
        return rankingResponseDto;
    }
}
