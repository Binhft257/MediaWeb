package com.javaweb.service.impl;

import com.javaweb.entity.BooksEntity;
import com.javaweb.entity.GenreEntity;
import com.javaweb.entity.MediaItemEntity;
import com.javaweb.entity.MediaTypeEntity;
import com.javaweb.entity.MoviesEntity;
import com.javaweb.entity.MusicEntity;
import com.javaweb.entity.TVSeriesEntity;
import com.javaweb.entity.UserEntity;
import com.javaweb.entity.VideoGamesEntity;
import com.javaweb.model.request.BooksRequest;
import com.javaweb.model.request.MediaCreateRequest;
import com.javaweb.model.request.MediaSearchRequest;
import com.javaweb.model.request.MoviesRequest;
import com.javaweb.model.request.MusicRequest;
import com.javaweb.model.request.TVSeriesRequest;
import com.javaweb.model.request.VideoGamesRequest;
import com.javaweb.model.response.BooksResponse;
import com.javaweb.model.response.MediaSearchResponse;
import com.javaweb.model.response.MoviesResponse;
import com.javaweb.model.response.MusicResponse;
import com.javaweb.model.response.TVSeriesResponse;
import com.javaweb.model.response.VideoGamesResponse;
import com.javaweb.repository.GenreRepository;
import com.javaweb.repository.MediaItemRepository;
import com.javaweb.repository.MediaItemRepositoryCustom;
import com.javaweb.repository.MediaTypeRepository;
import com.javaweb.repository.UserRepository;
import com.javaweb.service.MediaService;
import com.javaweb.exceptions.BadRequestException;
import com.javaweb.exceptions.UnauthorizedException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Date;

@Service
public class MediaServiceImpl implements MediaService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MediaItemRepositoryCustom mediaItemRepositoryCustom;

    @Autowired
    private MediaItemRepository mediaItemRepository;

    @Autowired
    private MediaTypeRepository mediaTypeRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Page<MediaSearchResponse> getMedias(Pageable pageable, MediaSearchRequest mediaSearchRequest) {

        List<MediaItemEntity> medias =
                mediaItemRepositoryCustom.getMediasWithCondition(pageable, mediaSearchRequest);

        List<MediaSearchResponse> resultList = medias.stream()
                .map(this::mapToSearchResponse)
                .toList();

        long total = mediaItemRepositoryCustom
                .getMediasWithCondition(null, mediaSearchRequest)
                .size();

        return new PageImpl<>(resultList, pageable, total);
    }

    @Override
    public MediaSearchResponse getMediasDetail(Integer id) {

        MediaItemEntity mediaItemEntity = mediaItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Media item not found."));

        return mapToSearchResponse(mediaItemEntity);
    }

    @Override
    public MediaSearchResponse uploadMediaItem(MediaCreateRequest mediaCreateRequest, Integer userId) {
        // Check if a user is connected
        if (userId == null) {
            throw new UnauthorizedException("You must be authenticated to upload a media item.");
        }

        // Create entity
        MediaItemEntity entity = new MediaItemEntity();
        mapToEntity(mediaCreateRequest, entity);       

        // Save the media entity in the db
        mediaItemRepository.save(entity);

        // Return the response
        return mapToSearchResponse(entity);
    }

    @Override
    public void deleteMediaItem(Integer mediaItemId, Integer userId) {
        // Verify the user is an admin
        UserEntity userEntity = userRepository.findById(userId)
            .orElseThrow(() -> new UnauthorizedException("You must be authenticated to delete a media item."));

        if (userEntity.getRole().getName() != "Admin") {
            new UnauthorizedException("You must be an admin to delete a media item.");
        }

        // Get the media item entity
        MediaItemEntity entity = mediaItemRepository.findById(mediaItemId)
                .orElseThrow(() -> new RuntimeException("Media item not found."));
        
        mediaItemRepository.delete(entity);
    }

    @Override
    public MediaSearchResponse updateMediaItem(Integer mediaItemId, MediaCreateRequest mediaCreateRequest, Integer userId) {
        // Verify the user is an admin
        UserEntity userEntity = userRepository.findById(userId)
            .orElseThrow(() -> new UnauthorizedException("You must be authenticated to delete a media item."));

        if (userEntity.getRole().getName() != "Admin") {
            new UnauthorizedException("You must be an admin to delete a media item.");
        }

        // Get the media item entity
        MediaItemEntity entity = mediaItemRepository.findById(mediaItemId)
                .orElseThrow(() -> new RuntimeException("Media item not found."));

        mapToEntity(mediaCreateRequest, entity);

        // Save the media entity in the db
        mediaItemRepository.save(entity);

        // Return the response
        return mapToSearchResponse(entity);
    }

    // Map a media item entity into a media search response
    private MediaSearchResponse mapToSearchResponse(MediaItemEntity entity) {

        MediaSearchResponse dto = new MediaSearchResponse();

        // Map common attributes
        modelMapper.map(entity, dto);

        // Map genres
        if (entity.getGenres() != null) {
            List<String> genreNames = entity.getGenres()
                    .stream()
                    .map(GenreEntity::getGenreName)
                    .toList();
            dto.setGenre(genreNames);
        }

        // Map specific details
        String type = entity.getMediaType().getTypeName();
        dto.setTypeName(type);

        Object details = switch (type) {
            case "Movie" -> modelMapper.map(entity.getMovie(), MoviesResponse.class);
            case "Music" -> modelMapper.map(entity.getMusic(), MusicResponse.class);
            case "Book" -> modelMapper.map(entity.getBook(), BooksResponse.class);
            case "TV Series" -> modelMapper.map(entity.getTvSeries(), TVSeriesResponse.class);
            case "Video Game" -> modelMapper.map(entity.getVideoGame(), VideoGamesResponse.class);
            default -> null;
        };
        dto.setDetails(details);

        return dto;
    }

    // Edit a media item entity according to a media edit request
    private void mapToEntity(MediaCreateRequest request, MediaItemEntity entity) {
        // Find media type entity
        String typeName = request.getTypeName();
        MediaTypeEntity mediaTypeEntity = mediaTypeRepository.findByTypeName(typeName)
            .orElseThrow(() -> new IllegalArgumentException("Media type not found: " + typeName));

        // Find genre
        List<String> genres = request.getGenres();
        List<GenreEntity> genreEntities = genreRepository.findAllByGenreName(genres)
            .orElseThrow(() -> new IllegalArgumentException("One of several genres not found in: " + genres));

        // Map entity attributes except details
        modelMapper.map(request, entity);
        entity.setMediaType(mediaTypeEntity);
        entity.setCreatedAt(new Date()); // Actual date
        entity.setGenres(genreEntities);

        // Find and map details
        Object details = request.getDetails();

        switch (typeName) {

            case "Book" -> {
                if (!(details instanceof BooksRequest dto)) {
                    throw new BadRequestException("Book details must be provided.");
                }
                BooksEntity book = modelMapper.map(dto, BooksEntity.class);
                book.setMediaItem(entity);
                entity.setBook(book);
            }

            case "Movie" -> {
                if (!(details instanceof MoviesRequest dto)) {
                    throw new BadRequestException("Movie details must be provided.");
                }
                MoviesEntity movie = modelMapper.map(dto, MoviesEntity.class);
                movie.setMediaItem(entity);
                entity.setMovie(movie);
            }

            case "Music" -> {
                if (!(details instanceof MusicRequest dto)) {
                    throw new BadRequestException("Music details must be provided.");
                }
                MusicEntity music = modelMapper.map(dto, MusicEntity.class);
                music.setMediaItem(entity);
                entity.setMusic(music);
            }

            case "Video Game" -> {
                if (!(details instanceof VideoGamesRequest dto)) {
                    throw new BadRequestException("Video game details must be provided.");
                }
                VideoGamesEntity game = modelMapper.map(dto, VideoGamesEntity.class);
                game.setMediaItem(entity);
                entity.setVideoGame(game);
            }

            case "TV Series" -> {
                if (!(details instanceof TVSeriesRequest dto)) {
                    throw new BadRequestException("TV series details must be provided.");
                }
                TVSeriesEntity serie = modelMapper.map(dto, TVSeriesEntity.class);
                serie.setMediaItem(entity);
                entity.setTvSeries(serie);
            }

            default -> throw new IllegalArgumentException("Unknown media type: " + typeName);
        }
    }
}
