package com.javaweb.service.impl;

import com.javaweb.entity.*;
import com.javaweb.exceptions.UnauthorizedException;
import com.javaweb.model.request.*;
import com.javaweb.model.response.*;
import com.javaweb.repository.*;
import com.javaweb.service.MediaService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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

    // ===================== APIs =====================

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

        userRepository.findById(userId)
                .orElseThrow(() -> new UnauthorizedException("You must be authenticated to upload a media item."));

        MediaItemEntity entity = new MediaItemEntity();

        // ✅ CREATE mapper (cho phép set type + create subtype)
        mapMediaItemToEntityForCreate(mediaCreateRequest, entity);

        entity.setCreatedAt(new Date());
        entity.setUpdatedAt(new Date());
        entity.setUploadedBy(userId);

        mediaItemRepository.save(entity);

        return mapToSearchResponse(entity);
    }

    @Override
    public void deleteMediaItem(Integer mediaItemId, Integer userId) {

        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UnauthorizedException("You must be authenticated."));

        MediaItemEntity entity = mediaItemRepository.findById(mediaItemId)
                .orElseThrow(() -> new RuntimeException("Media item not found."));

        boolean isAdmin = isAdmin(userEntity);
        boolean isOwner = userId.equals(entity.getUploadedBy());

        // ✅ Admin xóa được tất cả, user chỉ xóa của mình
        if (!isAdmin && !isOwner) {
            throw new UnauthorizedException("You are not allowed to delete this media item.");
        }

        mediaItemRepository.delete(entity);
    }

    @Override
    public MediaSearchResponse updateMediaItem(
            Integer mediaItemId,
            MediaCreateRequest mediaCreateRequest,
            Integer userId) {

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UnauthorizedException("You must be authenticated"));

        MediaItemEntity entity = mediaItemRepository.findById(mediaItemId)
                .orElseThrow(() -> new RuntimeException("Media item not found"));

        boolean isAdmin = isAdmin(user);
        boolean isOwner = userId.equals(entity.getUploadedBy());

        // ✅ Admin update được tất cả, user chỉ update của mình
        if (!isAdmin && !isOwner) {
            throw new UnauthorizedException("You are not allowed to update this media item.");
        }

        // ✅ UPDATE mapper (cấm đổi type + cấm map sai subtype)
        mapMediaItemToEntityForUpdate(mediaCreateRequest, entity);
        entity.setUpdatedAt(new Date());

        mediaItemRepository.save(entity);

        return mapToSearchResponse(entity);
    }

    // ===================== REVIEWS =====================


    @Override
    public List<UserCommentResponse> getMediaReviews(Integer mediaItemId) {
        return userCommentRepository
                .findByMediaItem_MediaItemId(mediaItemId, Sort.by(Sort.Direction.DESC, "commentId"))
                .stream()
                .map(this::mapToUserCommentResponse)
                .toList();
    }


    @Override
    public UserCommentResponse createMediaReview(Integer mediaItemId, UserCommentRequest request, Integer userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UnauthorizedException("You must be authenticated to create a review."));

        MediaItemEntity mediaItem = mediaItemRepository.findById(mediaItemId)
                .orElseThrow(() -> new RuntimeException("Media Item not found"));

        // ✅ Cho phép 1 user review nhiều lần => mỗi lần tạo 1 row mới
        UserCommentEntity entity = new UserCommentEntity();
        entity.setUser(user);
        entity.setMediaItem(mediaItem);
        entity.setContent(request.getContent());
        entity.setCreatedAt(new Date());
        entity.setUpdatedAt(new Date());

        userCommentRepository.save(entity);
        return mapToUserCommentResponse(entity);
    }

    @Override
    public UserCommentResponse updateMediaReview(Integer mediaItemId, Integer reviewId, UserCommentRequest request, Integer userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UnauthorizedException("You must be authenticated to update a review."));

        UserCommentEntity entity = userCommentRepository
                .findByCommentIdAndMediaItem_MediaItemId(reviewId, mediaItemId)
                .orElseThrow(() -> new RuntimeException("Review not found."));

        // ✅ Giữ nguyên logic: chỉ owner được sửa (admin không cần thêm quyền theo yêu cầu)
        if (!userId.equals(entity.getUser().getId())) {
            throw new UnauthorizedException("You can't update a review you don't own.");
        }

        entity.setContent(request.getContent());
        entity.setUpdatedAt(new Date());
        userCommentRepository.save(entity);

        return mapToUserCommentResponse(entity);
    }

    @Override
    public void deleteMediaReview(Integer mediaItemId, Integer reviewId, Integer userId) {

        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UnauthorizedException("You must be authenticated to delete a review."));

        UserCommentEntity entity = userCommentRepository
                .findByCommentIdAndMediaItem_MediaItemId(reviewId, mediaItemId)
                .orElseThrow(() -> new RuntimeException("Review not found."));

        boolean admin = isAdmin(userEntity);
        boolean owner = userId.equals(entity.getUser().getId());

        // ✅ Admin xóa được tất cả, user chỉ xóa của mình
        if (!admin && !owner) {
            throw new UnauthorizedException("You can't delete a review you don't own.");
        }

        userCommentRepository.delete(entity);
    }

    // ===================== RATINGS =====================

    @Override
    public List<UserRatingResponse> getMediaRatings(Integer mediaItemId) {
        return userRatingRepository.findByMediaItem_MediaItemId(mediaItemId)
                .stream()
                .map(this::mapToUserRatingResponse)
                .toList();
    }

    @Override
    public UserRatingResponse createMediaRating(Integer mediaItemId, UserRatingRequest request, Integer userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UnauthorizedException("You must be authenticated to rate."));

        MediaItemEntity mediaItem = mediaItemRepository.findById(mediaItemId)
                .orElseThrow(() -> new RuntimeException("Media Item not found"));

        boolean alreadyRated = userRatingRepository.existsByUserAndMediaItem(user, mediaItem);
        if (alreadyRated) {
            throw new IllegalStateException("User has already rated this media item.");
        }

        UserRatingEntity entity = new UserRatingEntity();
        entity.setRatingValue(request.getRatingValue());
        entity.setRatedAt(new Date());
        entity.setUser(user);
        entity.setMediaItem(mediaItem);

        userRatingRepository.save(entity);

        return mapToUserRatingResponse(entity);
    }

    @Override
    public UserRatingResponse updateMediaRating(Integer mediaItemId, UserRatingRequest request, Integer userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UnauthorizedException("You must be authenticated to update a rating."));

        MediaItemEntity mediaItem = mediaItemRepository.findById(mediaItemId)
                .orElseThrow(() -> new RuntimeException("Media Item not found"));

        UserRatingEntity entity = userRatingRepository.findByUserAndMediaItem(user, mediaItem)
                .orElseThrow(() -> new RuntimeException("Rating not found."));

        entity.setRatingValue(request.getRatingValue());
        entity.setRatedAt(new Date());

        userRatingRepository.save(entity);

        return mapToUserRatingResponse(entity);
    }

    @Override
    public void deleteMediaRating(Integer mediaItemId, Integer userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UnauthorizedException("You must be authenticated to delete a rating."));

        MediaItemEntity mediaItem = mediaItemRepository.findById(mediaItemId)
                .orElseThrow(() -> new RuntimeException("Media Item not found"));

        UserRatingEntity entity = userRatingRepository.findByUserAndMediaItem(user, mediaItem)
                .orElseThrow(() -> new RuntimeException("Rating not found."));

        // ✅ fix Integer compare
        if (!userId.equals(entity.getUser().getId())) {
            throw new UnauthorizedException("You can't delete a rating you don't own.");
        }

        userRatingRepository.delete(entity);
    }

    // ===================== MAPPING FUNCTIONS =====================

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

    // ===== CREATE mapper =====
    private void mapMediaItemToEntityForCreate(MediaCreateRequest request, MediaItemEntity entity) {

        String expectedTypeFromSubclass = resolveTypeNameFromRequest(request);

        // Create: bắt buộc có typeName và phải khớp subtype
        if (request.getTypeName() == null) {
            throw new IllegalArgumentException("typeName is required for creating a media item");
        }
        if (!request.getTypeName().equals(expectedTypeFromSubclass)) {
            throw new IllegalArgumentException(
                    "typeName does not match request subtype. Expected: " + expectedTypeFromSubclass +
                            ", but got: " + request.getTypeName()
            );
        }

        applyCommonMediaFields(request, entity);

        // set type
        setMediaTypeFromTypeName(request.getTypeName(), entity);

        // genres
        applyGenres(request, entity);

        // subtype (create allowed)
        mapSubtypeForCreate(request, entity);
    }

    // ===== UPDATE mapper =====
    private void mapMediaItemToEntityForUpdate(MediaCreateRequest request, MediaItemEntity entity) {

        String currentType = entity.getMediaType().getTypeName();
        String expectedTypeFromSubclass = resolveTypeNameFromRequest(request);

        // Update: subtype request bắt buộc match type hiện tại
        if (!currentType.equals(expectedTypeFromSubclass)) {
            throw new IllegalArgumentException(
                    "This media item is type '" + currentType + "' but request is '" + expectedTypeFromSubclass + "'"
            );
        }

        // Update: nếu client gửi typeName thì cũng phải đúng (và coi như cấm đổi)
        if (request.getTypeName() != null && !currentType.equals(request.getTypeName())) {
            throw new IllegalArgumentException(
                    "Cannot change media type from " + currentType + " to " + request.getTypeName()
            );
        }

        applyCommonMediaFields(request, entity);
        applyGenres(request, entity);

        // subtype: update ONLY (không tạo mới)
        mapSubtypeForUpdate(request, entity);
    }

    // ===== shared helpers =====

    private void applyCommonMediaFields(MediaCreateRequest request, MediaItemEntity entity) {
        if (request.getTitle() != null) entity.setTitle(request.getTitle());
        if (request.getDescription() != null) entity.setDescription(request.getDescription());
        if (request.getLanguage() != null) entity.setLanguage(request.getLanguage());
        if (request.getCountry() != null) entity.setCountry(request.getCountry());
        if (request.getContentRating() != null) entity.setContentRating(request.getContentRating());
        if (request.getReleaseDate() != null) entity.setReleaseDate(request.getReleaseDate());
        if (request.getUrlItem() != null) entity.setUrlItem(request.getUrlItem());
    }

    private void applyGenres(MediaCreateRequest request, MediaItemEntity entity) {
        if (request.getGenres() != null) {
            entity.setGenres(genreRepository.findByGenreNameIn(request.getGenres()));
        }
    }

    private void setMediaTypeFromTypeName(String typeName, MediaItemEntity entity) {
        MediaTypeEntity mediaType = mediaTypeRepository
                .findByTypeName(typeName)
                .orElseThrow(() -> new IllegalArgumentException("Media type not found: " + typeName));
        entity.setMediaType(mediaType);
    }

    private String resolveTypeNameFromRequest(MediaCreateRequest request) {
        if (request instanceof MoviesRequest) return "Movie";
        if (request instanceof MusicRequest) return "Music";
        if (request instanceof BooksRequest) return "Book";
        if (request instanceof TVSeriesRequest) return "TV Series";
        if (request instanceof VideoGamesRequest) return "Video Game";
        throw new IllegalArgumentException("Unsupported media request subtype: " + request.getClass().getSimpleName());
    }

    private void mapSubtypeForCreate(MediaCreateRequest request, MediaItemEntity entity) {

        if (request instanceof MoviesRequest movieReq) {
            MoviesEntity movie = entity.getMovie();
            if (movie == null) {
                movie = new MoviesEntity();
                movie.setMediaItem(entity);
                entity.setMovie(movie);
            }
            modelMapper.map(movieReq, movie);
            return;
        }

        if (request instanceof BooksRequest bookReq) {
            BooksEntity book = entity.getBook();
            if (book == null) {
                book = new BooksEntity();
                book.setMediaItem(entity);
                entity.setBook(book);
            }
            modelMapper.map(bookReq, book);
            return;
        }

        if (request instanceof MusicRequest musicReq) {
            MusicEntity music = entity.getMusic();
            if (music == null) {
                music = new MusicEntity();
                music.setMediaItem(entity);
                entity.setMusic(music);
            }
            modelMapper.map(musicReq, music);
            return;
        }

        if (request instanceof TVSeriesRequest tvReq) {
            TVSeriesEntity tv = entity.getTvSeries();
            if (tv == null) {
                tv = new TVSeriesEntity();
                tv.setMediaItem(entity);
                entity.setTvSeries(tv);
            }
            modelMapper.map(tvReq, tv);
            return;
        }

        if (request instanceof VideoGamesRequest gameReq) {
            VideoGamesEntity game = entity.getVideoGame();
            if (game == null) {
                game = new VideoGamesEntity();
                game.setMediaItem(entity);
                entity.setVideoGame(game);
            }
            modelMapper.map(gameReq, game);
            return;
        }

        throw new IllegalArgumentException("Unsupported media create request");
    }

    private void mapSubtypeForUpdate(MediaCreateRequest request, MediaItemEntity entity) {

        if (request instanceof MoviesRequest movieReq) {
            if (entity.getMovie() == null) throw new IllegalStateException("Movie entity missing for this media item");
            modelMapper.map(movieReq, entity.getMovie());
            return;
        }

        if (request instanceof BooksRequest bookReq) {
            if (entity.getBook() == null) throw new IllegalStateException("Book entity missing for this media item");
            modelMapper.map(bookReq, entity.getBook());
            return;
        }

        if (request instanceof MusicRequest musicReq) {
            if (entity.getMusic() == null) throw new IllegalStateException("Music entity missing for this media item");
            modelMapper.map(musicReq, entity.getMusic());
            return;
        }

        if (request instanceof TVSeriesRequest tvReq) {
            if (entity.getTvSeries() == null) throw new IllegalStateException("TV Series entity missing for this media item");
            modelMapper.map(tvReq, entity.getTvSeries());
            return;
        }

        if (request instanceof VideoGamesRequest gameReq) {
            if (entity.getVideoGame() == null) throw new IllegalStateException("Video Game entity missing for this media item");
            modelMapper.map(gameReq, entity.getVideoGame());
            return;
        }

        throw new IllegalArgumentException("Unsupported media update request");
    }

    // ✅ improved: accept ADMIN / Admin / ROLE_ADMIN
    private boolean isAdmin(UserEntity user) {
        if (user == null || user.getRole() == null || user.getRole().getName() == null) return false;
        String roleName = user.getRole().getName().trim();
        return roleName.equalsIgnoreCase("ADMIN") || roleName.equalsIgnoreCase("ROLE_ADMIN");
    }

    private UserCommentResponse mapToUserCommentResponse(UserCommentEntity entity) {
        UserCommentResponse dto = new UserCommentResponse();

        dto.setReviewId(entity.getCommentId());
        dto.setContent(entity.getContent());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        dto.setUserId(entity.getUser().getId());
        dto.setUserName(entity.getUser().getName());
        dto.setUserAvatar(entity.getUser().getAvatar());

        return dto;
    }

    private UserRatingResponse mapToUserRatingResponse(UserRatingEntity entity) {
        UserRatingResponse dto = new UserRatingResponse();

        modelMapper.map(entity, dto);

        dto.setUserId(entity.getUser().getId());
        dto.setUserName(entity.getUser().getName());
        dto.setUserAvatar(entity.getUser().getAvatar());

        return dto;
    }
}
