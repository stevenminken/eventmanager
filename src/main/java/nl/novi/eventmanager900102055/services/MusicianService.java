package nl.novi.eventmanager900102055.services;

import nl.novi.eventmanager900102055.dtos.MusicianDto;
import nl.novi.eventmanager900102055.models.Musician;
import nl.novi.eventmanager900102055.repositories.MusicianRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MusicianService {

    private final MusicianRepository musicianRepository;

    public MusicianService(MusicianRepository musicianRepository) {
        this.musicianRepository = musicianRepository;
    }

    public List<MusicianDto> getAllMusicians() {
        List<MusicianDto> musicianDtoList = new ArrayList<>();
        Iterable<Musician> musicianList = musicianRepository.findAll();

        for(Musician m : musicianList) {
            MusicianDto dto = transferToDto(m);
            musicianDtoList.add(dto);
        }
        return musicianDtoList;
    }

    public MusicianDto transferToDto(Musician musician){
        var dto = new MusicianDto();

        dto.setId(musician.getId());
        dto.setFirstName(musician.getFirstName());
        dto.setLastName(musician.getLastName());

        return dto;
    }

}
