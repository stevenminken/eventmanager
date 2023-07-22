package nl.novi.eventmanager900102055.services;

import nl.novi.eventmanager900102055.dtos.ArtistDto;
import nl.novi.eventmanager900102055.exceptions.NameDuplicateException;
import nl.novi.eventmanager900102055.exceptions.ResourceNotFoundException;
import nl.novi.eventmanager900102055.models.Artist;
import nl.novi.eventmanager900102055.models.User;
import nl.novi.eventmanager900102055.repositories.ArtistRepository;
import nl.novi.eventmanager900102055.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArtistService {
    private final ArtistRepository artistRepository;

     public ArtistService(ArtistRepository artistRepository) {
            this.artistRepository = artistRepository;
    }
    public ArtistDto createArtist(ArtistDto artistDto) throws NameDuplicateException{

        Iterable<Artist> Artists = artistRepository.findAll();
        for (Artist artist : Artists) {
            if (artist.getName().equals(artistDto.getName())) {
                throw new NameDuplicateException("This artist already exists");
            }
        }
        Artist artist = transferArtistDtoToArtist(artistDto);
        artist = artistRepository.save(artist);
        return transferArtistToArtistDto(artist);
    }
    public List<ArtistDto> findAllArtists() {
        List<Artist> artistList = artistRepository.findAll();
        return transferArtistListToArtistDtoList(artistList);
    }

    public ArtistDto findArtistById(Long id) {
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Artist not found"));
        return transferArtistToArtistDto(artist);
    }

    public ArtistDto findArtistByName(String name) throws ResourceNotFoundException {
        Artist artist  = artistRepository.findByName(name);
        if (artist == null) {
            throw new ResourceNotFoundException("Artist not found");
        }
        return transferArtistToArtistDto(artist);
    }

    public ArtistDto updateArtist(Long id, ArtistDto artistDto) {
        Artist artist = artistRepository.findById(id).orElse(null);
        if (artist == null) {
            return null;
        }
        artist.setName(artistDto.getName());
        artist.setGenre(artistDto.getGenre());

        return transferArtistToArtistDto(artistRepository.save(artist));
    }

    public boolean deleteArtist(Long id) {
        if (artistRepository.existsById(id)) {
            artistRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<ArtistDto> transferArtistListToArtistDtoList(List<Artist> artistList) {
        List<ArtistDto> artistDtoList = new ArrayList<>();

        for (Artist artist : artistList) {
            artistDtoList.add(transferArtistToArtistDto(artist));
        }
        return artistDtoList;
    }

    public Artist transferArtistDtoToArtist(ArtistDto artistDto) {
        Artist artist = new Artist();
        artist.setId(artistDto.getId());
        artist.setName(artistDto.getName());
        artist.setGenre(artistDto.getGenre());
        artist.setEventList(artistDto.getEventList());

        return artist;
    }

    public ArtistDto transferArtistToArtistDto(Artist artist) {
        ArtistDto artistDto = new ArtistDto();

        artistDto.setId(artist.getId());
        artistDto.setName(artist.getName());
        artistDto.setGenre(artist.getGenre());
        artistDto.setEventList(artist.getEventList());

        return artistDto;
    }
}
