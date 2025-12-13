package com.javaweb.service.impl;

import com.javaweb.entity.BooksEntity;
import com.javaweb.entity.GenreEntity;
import com.javaweb.entity.MediaItemEntity;
import com.javaweb.entity.MediaTypeEntity;
import com.javaweb.entity.MoviesEntity;
import com.javaweb.entity.MusicEntity;
import com.javaweb.entity.TVSeriesEntity;
import com.javaweb.entity.UserCommentEntity;
import com.javaweb.entity.UserEntity;
import com.javaweb.entity.UserRatingEntity;
import com.javaweb.entity.VideoGamesEntity;
import com.javaweb.model.request.BooksRequest;
import com.javaweb.model.request.MediaCreateRequest;
import com.javaweb.model.request.MediaSearchRequest;
import com.javaweb.model.request.MoviesRequest;
import com.javaweb.model.request.MusicRequest;
import com.javaweb.model.request.TVSeriesRequest;
import com.javaweb.model.request.UserCommentRequest;
import com.javaweb.model.request.UserRatingRequest;
import com.javaweb.model.request.VideoGamesRequest;
import com.javaweb.model.response.BooksResponse;
import com.javaweb.model.response.MediaSearchResponse;
import com.javaweb.model.response.MoviesResponse;
import com.javaweb.model.response.MusicResponse;
import com.javaweb.model.response.TVSeriesResponse;
import com.javaweb.model.response.UserCommentResponse;
import com.javaweb.model.response.UserRatingResponse;
import com.javaweb.model.response.VideoGamesResponse;
import com.javaweb.repository.GenreRepository;
import com.javaweb.repository.MediaItemRepository;
import com.javaweb.repository.MediaItemRepositoryCustom;
import com.javaweb.repository.MediaTypeRepository;
import com.javaweb.repository.UserCommentRepository;
import com.javaweb.repository.UserRatingRepository;
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

    @Autowired
    private UserCommentRepository userCommentRepository;

    @Autowired
    private UserRatingRepository userRatingRepository;

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
        mapMediaItemToEntity(mediaCreateRequest, entity);   
        entity.setCreatedAt(new Date());    

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

        // Update entity
        mapMediaItemToEntity(mediaCreateRequest, entity);
        entity.setUpdatedAt(new Date());

        // Save the media entity in the db
        mediaItemRepository.save(entity);

        // Return the response
        return mapToSearchResponse(entity);
    }

    @Override
    public List<UserCommentResponse> getMediaReviews(Integer mediaItemId) {
        List<UserCommentEntity> reviewsEntities = userCommentRepository.findByMediaItemId(mediaItemId);

        // Map to response
        List<UserCommentResponse> reviewsResponses =
            reviewsEntities.stream()
                .map(this::mapToUserCommentResponse)
                .toList();

        return reviewsResponses;
    }

    @Override
    public UserCommentResponse createMediaReview(Integer mediaItemId, UserCommentRequest request, Integer userId) {
        // Check if a user is connected
        if (userId == null) {
            throw new UnauthorizedException("You must be authenticated to create a review.");
        }

        // Create entity
        UserCommentEntity entity = new UserCommentEntity();
        
        // Map request to entity
        entity.setStatus(request.getStatus());
        entity.setCreatedAt(new Date());
        entity.setUser(userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found")));
        entity.setMediaItem(mediaItemRepository.findById(mediaItemId).orElseThrow(() -> new RuntimeException("Media Item not found")));

        // Save the user comment entity in the db
        userCommentRepository.save(entity);

        // Return the response
        return mapToUserCommentResponse(entity);
    }

    @Override
    public UserCommentResponse updateMediaReview(Integer mediaItemId, Integer reviewId, UserCommentRequest request, Integer userId) {
        // Check if a user is connected
        if (userId == null) {
            throw new UnauthorizedException("You must be authenticated to update a review.");
        }

        // Get the review entity
        UserCommentEntity entity = userCommentRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found."));

        // Check if the user is the one that wrote the review
        if (userId != entity.getUser().getId()) {
            throw new UnauthorizedException("You can't modify a review you didn't wrote.");
        }

        // Update entity
        entity.setStatus(request.getStatus());
        entity.setUpdatedAt(new Date());
    
        // Save the media entity in the db
        userCommentRepository.save(entity);

        // Return the response
        return mapToUserCommentResponse(entity);        
    }

    @Override
    public void deleteMediaReview(Integer reviewId, Integer userId) {
        // Check if a user is connected
        if (userId == null) {
            throw new UnauthorizedException("You must be authenticated to delete a review.");
        }

        // Get the review entity
        UserCommentEntity entity = userCommentRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found."));

        // Check if the user is the one that wrote the review
        if (userId != entity.getUser().getId()) {
            throw new UnauthorizedException("You can't delete a review you didn't wrote.");
        }
        
        // Delete the review
        userCommentRepository.delete(entity);
    }
    
    @Override
    public List<UserRatingResponse> getMediaRatings(Integer mediaItemId) {
        List<UserRatingEntity> ratingsEntities = userRatingRepository.findByMediaItemId(mediaItemId);

        // Map to response
        List<UserRatingResponse> ratingsResponses =
            ratingsEntities.stream()
                .map(this::mapToUserRatingResponse)
                .toList();

        return ratingsResponses;
    }

    @Override
    public UserRatingResponse createMediaRating(Integer mediaItemId, UserRatingRequest request, Integer userId) {
        // Check if a user is connected
        if (userId == null) {
            throw new UnauthorizedException("You must be authenticated to rate.");
        }

        // Create entity
        UserRatingEntity entity = new UserRatingEntity();
        
        // Map request to entity
        entity.setRatingValue(request.getRatingValue());
        entity.setRatedAt(new Date());
        entity.setUser(userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found")));
        entity.setMediaItem(mediaItemRepository.findById(mediaItemId).orElseThrow(() -> new RuntimeException("Media Item not found")));

        // Save the user comment entity in the db
        userRatingRepository.save(entity);

        // Return the response
        return mapToUserRatingResponse(entity);
    }

    @Override
    public UserRatingResponse updateMediaRating(Integer mediaItemId, Integer reviewId, UserRatingRequest request, Integer userId) {
        // Check if a user is connected
        if (userId == null) {
            throw new UnauthorizedException("You must be authenticated to update a rating.");
        }

        // Get the rating entity
        UserRatingEntity entity = userRatingRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Rating not found."));

        // Check if the user is the one that wrote the review
        if (userId != entity.getUser().getId()) {
            throw new UnauthorizedException("You can't modify a rating you don't own.");
        }

        // Update entity
        entity.setRatingValue(request.getRatingValue());
    
        // Save the media entity in the db
        userRatingRepository.save(entity);

        // Return the response
        return mapToUserRatingResponse(entity);        
    }

    @Override
    public void deleteMediaRating(Integer ratingId, Integer userId) {
        // Check if a user is connected
        if (userId == null) {
            throw new UnauthorizedException("You must be authenticated to delete a rating.");
        }

        // Get the rating entity
        UserRatingEntity entity = userRatingRepository.findById(ratingId)
                .orElseThrow(() -> new RuntimeException("Rating not found."));

        // Check if the user is the one that wrote the review
        if (userId != entity.getUser().getId()) {
            throw new UnauthorizedException("You can't delete a rating you don't own.");
        }
        
        // Delete the review
        userRatingRepository.delete(entity);
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

    // Edit a media item entity according to a media edit request
    private void mapMediaItemToEntity(MediaCreateRequest request, MediaItemEntity entity) {
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

    // Map a User Comment Entity into a User Comment Response
    private UserCommentResponse mapToUserCommentResponse(UserCommentEntity entity) {

        UserCommentResponse dto = new UserCommentResponse();

        // Map common attributes
        modelMapper.map(entity, dto);

        // Map user attributes
        dto.setUserId(entity.getUser().getId());
        dto.setUserName(entity.getUser().getName());
        dto.setUserAvatar(entity.getUser().getAvatar());

        return dto;
    }

    // Map a User Rating Entity into a User Rating Response
    private UserRatingResponse mapToUserRatingResponse(UserRatingEntity entity) {

        UserRatingResponse dto = new UserRatingResponse();

        // Map common attributes
        modelMapper.map(entity, dto);

        // Map user attributes
        dto.setUserId(entity.getUser().getId());
        dto.setUserName(entity.getUser().getName());
        dto.setUserAvatar(entity.getUser().getAvatar());

        return dto;
    }
}
