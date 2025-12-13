package com.javaweb.service.impl;

import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javaweb.entity.GenreEntity;
import com.javaweb.entity.MediaItemEntity;
import com.javaweb.entity.UserMediaLogEntity;
import com.javaweb.exceptions.UnauthorizedException;
import com.javaweb.model.request.TrackMediaRequest;
import com.javaweb.model.response.BooksResponse;
import com.javaweb.model.response.MediaSearchResponse;
import com.javaweb.model.response.MoviesResponse;
import com.javaweb.model.response.MusicResponse;
import com.javaweb.model.response.TVSeriesResponse;
import com.javaweb.model.response.VideoGamesResponse;
import com.javaweb.repository.MediaItemRepository;
import com.javaweb.repository.UserMediaLogRepository;
import com.javaweb.repository.UserRepository;
import com.javaweb.service.TrackingService;

@Service
public class TrackingServiceImpl implements TrackingService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserMediaLogRepository userMediaLogRepository;

    @Autowired
    private MediaItemRepository mediaItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<MediaSearchResponse> getTrackedMediaItems(Integer userId) {
        // Check if a user is connected
        if (userId == null) {
            throw new UnauthorizedException("You must be authenticated to rate.");
        }

        // Get the logs of the connected user
        List<UserMediaLogEntity> userMediaLogEntities = userMediaLogRepository.findByUserId(userId);

        // Get each media item
        List<MediaItemEntity> mediaItemsEntities = userMediaLogEntities
            .stream()
            .map(UserMediaLogEntity::getMediaItem)
            .toList();

        // Map to response
        List<MediaSearchResponse> mediaSearchResponses =
            mediaItemsEntities.stream()
                .map(this::mapToSearchResponse)
                .toList();

        return mediaSearchResponses;
    }

    @Override
    public MediaSearchResponse trackMediaItem(TrackMediaRequest request, Integer userId) {
        // Check if a user is connected
        if (userId == null) {
            throw new UnauthorizedException("You must be authenticated to track a media item.");
        }

        // Get the media item
        MediaItemEntity mediaItemEntity = mediaItemRepository.findById(request.getMediaItemId()).orElseThrow(() -> new RuntimeException("Media item not found."));

        // Create entity
        UserMediaLogEntity entity = new UserMediaLogEntity();
        entity.setCreatedAt(new Date());
        entity.setMediaItem(mediaItemEntity);
        entity.setUser(userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found.")));

        // Save the entity in the db
        userMediaLogRepository.save(entity);

        // Return the response
        return mapToSearchResponse(mediaItemEntity);
    }

    @Override
    public void deleteMediaItemTracked(Integer userMediaLogId, Integer userId) {
        // Check if a user is connected
        if (userId == null) {
            throw new UnauthorizedException("You must be authenticated to delete a media item tracked.");
        }

        // Get the user media log
        UserMediaLogEntity userMediaLogEntity = userMediaLogRepository.findById(userMediaLogId).orElseThrow(() -> new RuntimeException("User media log not found."));

        // Check if the user is the one that wrote the review
        if (userId != userMediaLogEntity.getUser().getId()) {
            throw new UnauthorizedException("You can't delete a media item tracked you didn't tracked.");
        }
        
        // Delete the media log
        userMediaLogRepository.delete(userMediaLogEntity);
    }


    ///// MAPPING FUNCTIONS /////
    
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
}
