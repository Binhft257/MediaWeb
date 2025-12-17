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
        userRepository.findById(userId).orElseThrow(() -> new UnauthorizedException("You must be authenticated to upload a media item."));

        // Create entity
        MediaItemEntity entity = new MediaItemEntity();
        mapMediaItemToEntity(mediaCreateRequest, entity);   
        entity.setCreatedAt(new Date());
        entity.setUpdatedAt(new Date());
        entity.setUploadedBy(userId);

        // Save the media entity in the db
        mediaItemRepository.save(entity);

        // Return the response
        return mapToSearchResponse(entity);
    }

    @Override
    public void deleteMediaItem(Integer mediaItemId, Integer userId) {
        // Verify the user is an admin
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new UnauthorizedException("You must be authenticated to delete a media item."));
        
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
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new UnauthorizedException("You must be authenticated to update a media item."));

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
        List<UserCommentEntity> reviewsEntities = userCommentRepository.findByMediaItem_MediaItemId(mediaItemId);

        // Map to response
        List<UserCommentResponse> reviewsResponses =
            reviewsEntities.stream()
                .map(this::mapToUserCommentResponse)
                .toList();

        return reviewsResponses;
    }

    @Override
    public UserCommentResponse createMediaReview(Integer mediaItemId, UserCommentRequest request, Integer userId) {
        // Check if the user is connected
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new UnauthorizedException("You must be authenticated to create a review."));

        // Verify that the media item exists
        MediaItemEntity mediaItem = mediaItemRepository.findById(mediaItemId).orElseThrow(() -> new RuntimeException("Media Item not found"));

        // Check if the user has already commented on this media item
        boolean alreadyCommented = userCommentRepository.existsByUserAndMediaItem(user, mediaItem);
        if (alreadyCommented) {
            throw new IllegalStateException("User has already commented on this media item.");
        }

        // Create the comment entity
        UserCommentEntity entity = new UserCommentEntity();
        entity.setStatus(request.getStatus());
        entity.setCreatedAt(new Date());
        entity.setUser(user);
        entity.setMediaItem(mediaItem);

        // Save the comment in the database
        userCommentRepository.save(entity);

        // Return the response
        return mapToUserCommentResponse(entity);
    }

    @Override
    public UserCommentResponse updateMediaReview(Integer mediaItemId, UserCommentRequest request, Integer userId) {
        // Check the user is connected
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new UnauthorizedException("You must be authenticated to update a review."));

        // Verify that the media item exists
        MediaItemEntity mediaItem = mediaItemRepository.findById(mediaItemId).orElseThrow(() -> new RuntimeException("Media Item not found"));

        // Find the review by user and media item
        UserCommentEntity entity = userCommentRepository.findByUserAndMediaItem(user, mediaItem).orElseThrow(() -> new RuntimeException("Review not found."));

        // Update the review
        entity.setStatus(request.getStatus());
        entity.setUpdatedAt(new Date());

        // Save the updated review in the database
        userCommentRepository.save(entity);

        // Return the response
        return mapToUserCommentResponse(entity);
    }


    @Override
    public void deleteMediaReview(Integer mediaItemId, Integer userId) {
        // Check if a user is connected
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new UnauthorizedException("You must be authenticated to delete a review."));

        // Verify that the media item exists
        MediaItemEntity mediaItem = mediaItemRepository.findById(mediaItemId).orElseThrow(() -> new RuntimeException("Media Item not found"));

        // Find the review by user and media item
        UserCommentEntity entity = userCommentRepository.findByUserAndMediaItem(user, mediaItem).orElseThrow(() -> new RuntimeException("Review not found."));

        // Check if the user is the one that wrote the review
        if (userId != entity.getUser().getId()) {
            throw new UnauthorizedException("You can't delete a review you didn't wrote.");
        }
        
        // Delete the review
        userCommentRepository.delete(entity);
    }
    
    @Override
    public List<UserRatingResponse> getMediaRatings(Integer mediaItemId) {
        List<UserRatingEntity> ratingsEntities = userRatingRepository.findByMediaItem_MediaItemId(mediaItemId);

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
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new UnauthorizedException("You must be authenticated to rate."));

        // Find media item
        MediaItemEntity mediaItem = mediaItemRepository.findById(mediaItemId).orElseThrow(() -> new RuntimeException("Media Item not found"));

        // Check if user has already rated
        boolean alreadyRated = userRatingRepository.existsByUserAndMediaItem(user, mediaItem);
        if (alreadyRated) {
            throw new IllegalStateException("User has already rated this media item.");
        }

        // Create entity
        UserRatingEntity entity = new UserRatingEntity();
        entity.setRatingValue(request.getRatingValue());
        entity.setRatedAt(new Date());
        entity.setUser(user);
        entity.setMediaItem(mediaItem);

        // Save in the db
        userRatingRepository.save(entity);

        return mapToUserRatingResponse(entity);
    }

    @Override
    public UserRatingResponse updateMediaRating(Integer mediaItemId, UserRatingRequest request, Integer userId) {
        // Check if the user is connected
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new UnauthorizedException("You must be authenticated to update a rating."));

        // Verify that the media item exists
        MediaItemEntity mediaItem = mediaItemRepository.findById(mediaItemId).orElseThrow(() -> new RuntimeException("Media Item not found"));

        // Find the rating by user and media item
        UserRatingEntity entity = userRatingRepository.findByUserAndMediaItem(user, mediaItem).orElseThrow(() -> new RuntimeException("Rating not found."));

        // Update the rating
        entity.setRatingValue(request.getRatingValue());
        entity.setRatedAt(new Date());

        // Save the updated rating in the database
        userRatingRepository.save(entity);

        // Return the response
        return mapToUserRatingResponse(entity);      
    }

    @Override
    public void deleteMediaRating(Integer mediaItemId, Integer userId) {
        // Check if the user is connected
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new UnauthorizedException("You must be authenticated to delete a rating."));

        // Verify that the media item exists
        MediaItemEntity mediaItem = mediaItemRepository.findById(mediaItemId).orElseThrow(() -> new RuntimeException("Media Item not found"));

        // Find the rating by user and media item
        UserRatingEntity entity = userRatingRepository.findByUserAndMediaItem(user, mediaItem).orElseThrow(() -> new RuntimeException("Rating not found."));

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
        String type = entity.getMediaType().getTypeName();

        MediaSearchResponse dto;

        switch (type) {
            case "Movie":
                dto = modelMapper.map(entity.getMovie(), MoviesResponse.class);
                break;
            case "Music":
                dto = modelMapper.map(entity.getMusic(), MusicResponse.class);
                break;
            case "Book":
                dto = modelMapper.map(entity.getBook(), BooksResponse.class);
                break;
            case "TV Series":
                dto = modelMapper.map(entity.getTvSeries(), TVSeriesResponse.class);
                break;
            case "Video Game":
                dto = modelMapper.map(entity.getVideoGame(), VideoGamesResponse.class);
                break;
            default:
                throw new IllegalArgumentException("Unknown media type: " + type);
        }

        // Champs communs
        dto.setMediaItemId(entity.getMediaItemId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setLanguage(entity.getLanguage());
        dto.setCountry(entity.getCountry());
        dto.setContentRating(entity.getContentRating());
        dto.setReleaseDate(entity.getReleaseDate());
        dto.setUrlItem(entity.getUrlItem());
        dto.setTypeName(type);

        if (entity.getGenres() != null) {
            List<String> genreNames = entity.getGenres().stream()
                    .map(GenreEntity::getGenreName)
                    .toList();
            dto.setGenres(genreNames);
        }

        return dto;
    }
    
    // Map a media item to entity
    private void mapMediaItemToEntity(MediaCreateRequest request, MediaItemEntity entity) {

        entity.setTitle(request.getTitle());
        entity.setDescription(request.getDescription());
        entity.setLanguage(request.getLanguage());
        entity.setCountry(request.getCountry());
        entity.setContentRating(request.getContentRating());
        entity.setReleaseDate(request.getReleaseDate());
        entity.setUrlItem(request.getUrlItem());

        MediaTypeEntity mediaType = mediaTypeRepository
                .findByTypeName(request.getTypeName())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Media type not found: " + request.getTypeName()
                ));
        entity.setMediaType(mediaType);

        if (request.getGenres() != null) {
            entity.setGenres(
                genreRepository.findByGenreNameIn(request.getGenres())
            );
        }

        if (request instanceof MoviesRequest movieReq) {
            MoviesEntity movie = entity.getMovie();
            if (movie == null) {
                movie = new MoviesEntity();
                movie.setMediaItem(entity);
                entity.setMovie(movie);
            }
            modelMapper.map(movieReq, movie);

        } else if (request instanceof BooksRequest bookReq) {
            BooksEntity book = entity.getBook();
            if (book == null) {
                book = new BooksEntity();
                book.setMediaItem(entity);
                entity.setBook(book);
            }
            modelMapper.map(bookReq, book);

        } else if (request instanceof MusicRequest musicReq) {
            MusicEntity music = entity.getMusic();
            if (music == null) {
                music = new MusicEntity();
                music.setMediaItem(entity);
                entity.setMusic(music);
            }
            modelMapper.map(musicReq, music);

        } else if (request instanceof TVSeriesRequest tvReq) {
            TVSeriesEntity tv = entity.getTvSeries();
            if (tv == null) {
                tv = new TVSeriesEntity();
                tv.setMediaItem(entity);
                entity.setTvSeries(tv);
            }
            modelMapper.map(tvReq, tv);

        } else if (request instanceof VideoGamesRequest gameReq) {
            VideoGamesEntity game = entity.getVideoGame();
            if (game == null) {
                game = new VideoGamesEntity();
                game.setMediaItem(entity);
                entity.setVideoGame(game);
            }
            modelMapper.map(gameReq, game);

        } else {
            throw new IllegalArgumentException("Unsupported media create request");
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
