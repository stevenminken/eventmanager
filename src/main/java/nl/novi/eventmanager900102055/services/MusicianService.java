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
        List<Musician> musicianList = musicianRepository.findAll();
        return transferMusicianListToMusicianDtoList(musicianList);
    }

    public List<MusicianDto> getMusicianByLastName(String lastname) {
        Iterable<Musician> iterableMusicians = musicianRepository.findByLastName(lastname);
        ArrayList<Musician> musicianList = new ArrayList<>();

        for (Musician musician : iterableMusicians) {
            musicianList.add(musician);
        }

        return transferMusicianListToMusicianDtoList(musicianList);
    }

    public MusicianDto addMusician(MusicianDto musicianDto) {
        Musician musician = transferToMusician(musicianDto);
        musicianRepository.save(musician);

        return transferToMusicianDto(musician);
    }

    public List<MusicianDto> transferMusicianListToMusicianDtoList(List<Musician> musicianList){
        List<MusicianDto> musicianDtoList = new ArrayList<>();

        for(Musician m : musicianList) {
            MusicianDto musicianDto = new MusicianDto();
            musicianDto.id = m.getId();
            musicianDto.firstName = m.getFirstName();
            musicianDto.lastName = m.getLastName();
            musicianDtoList.add(musicianDto);
        }
        return musicianDtoList;
    }

    public Musician transferToMusician(MusicianDto musicianDto){
        Musician musician = new Musician();
        musician.setId(musicianDto.id);
        musician.setFirstName(musicianDto.firstName);
        musician.setLastName(musicianDto.lastName);

        return musician;
    }

    public MusicianDto transferToMusicianDto(Musician musician){
        MusicianDto musicianDto = new MusicianDto();

        musicianDto.id = musician.getId();
        musicianDto.firstName = musician.getFirstName();
        musicianDto.lastName = musician.getLastName();

        return musicianDto;
    }

}
