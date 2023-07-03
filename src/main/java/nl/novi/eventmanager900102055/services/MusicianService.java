package nl.novi.eventmanager900102055.services;

import nl.novi.eventmanager900102055.dtos.MusicianDto;
import nl.novi.eventmanager900102055.exceptions.NameDuplicateException;
import nl.novi.eventmanager900102055.exceptions.ResourceNotFoundException;
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

    public List<MusicianDto> getMusicianByLastName(String lastname) throws ResourceNotFoundException {
        Iterable<Musician> iterableMusicians = musicianRepository.findByLastName(lastname);
        if(iterableMusicians == null) {
            throw new ResourceNotFoundException("Can't find this musician");
        }
        ArrayList<Musician> musicianList = new ArrayList<>();

        for (Musician musician : iterableMusicians) {
            musicianList.add(musician);
        }

        return transferMusicianListToMusicianDtoList(musicianList);
    }

    public MusicianDto addMusician(MusicianDto musicianDto) throws NameDuplicateException {
        Iterable<Musician> allMusicians = musicianRepository.findAll();
        for (Musician m: allMusicians) {
            if (m.getLastName().equals(musicianDto.lastName)) {
                throw new NameDuplicateException("This Musician already exists");
            }
        }
        Musician musician = transferToMusician(musicianDto);
        musicianRepository.save(musician);

        return transferToMusicianDto(musician);
    }

    public boolean deleteMusician(Long id) {
        if(musicianRepository.existsById(id)) {
            musicianRepository.deleteById(id);
            return true;
        }
        return false;
//        if(repos == null){throw new RecourceNotFoundException("...")}
//
//

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
