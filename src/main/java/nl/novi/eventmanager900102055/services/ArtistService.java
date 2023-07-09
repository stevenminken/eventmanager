package nl.novi.eventmanager900102055.services;

import nl.novi.eventmanager900102055.dtos.ArtistDto;
import nl.novi.eventmanager900102055.exceptions.NameDuplicateException;
import nl.novi.eventmanager900102055.exceptions.ResourceNotFoundException;
import nl.novi.eventmanager900102055.models.Artist;
import nl.novi.eventmanager900102055.models.Event;
import nl.novi.eventmanager900102055.models.User;
import nl.novi.eventmanager900102055.repositories.ArtistRepository;
import nl.novi.eventmanager900102055.repositories.EventRepository;
import nl.novi.eventmanager900102055.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArtistService {
    private final ArtistRepository artistRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public ArtistService(ArtistRepository artistRepository, UserRepository userRepository, EventRepository eventRepository) {
        this.artistRepository = artistRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }
    public List<ArtistDto> findAllArtists() {
        List<Artist> artistList = artistRepository.findAll();
        return transferArtistListToArtistDtoList(artistList);
    }

    public ArtistDto createArtist(ArtistDto artistDto, long userId) throws NameDuplicateException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Iterable<Artist> Artists = artistRepository.findAll();
        for (Artist l : Artists) {
            if (l.getName().equals(artistDto.getName())) {
                throw new NameDuplicateException("This Artist already exists");
            }
        }
        Artist artist = transferArtistDtoToArtist(artistDto);

//        List<Event> events = eventRepository.findAllById(eventIds);
//        artist.setEvents(events);
//        for (Event event : events) {
//            event.getArtists().add(artist);
//        }

        artist.setUser(user);
        user.getArtistList().add(artist);

        userRepository.save(user);
        artist = artistRepository.save(artist);
        return transferArtistToArtistDto(artist);
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

        for (Artist a : artistList) {
            ArtistDto artistDto = new ArtistDto();
            artistDto.setId(a.getId());
            artistDto.setName(a.getName());
            artistDto.setGenre(a.getGenre());
            artistDtoList.add(artistDto);
        }
        return artistDtoList;
    }

    public Artist transferArtistDtoToArtist(ArtistDto artistDto) {
        Artist artist = new Artist();
        artist.setId(artistDto.getId());
        artist.setName(artistDto.getName());
        artist.setGenre(artistDto.getGenre());

        return artist;
    }

    public ArtistDto transferArtistToArtistDto(Artist artist) {
        ArtistDto artistDto = new ArtistDto();

        artistDto.setId(artist.getId());
        artistDto.setName(artist.getName());
        artistDto.setGenre(artist.getGenre());

        return artistDto;
    }
}
