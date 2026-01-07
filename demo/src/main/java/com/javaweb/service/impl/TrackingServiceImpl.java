package com.javaweb.service.impl;

import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.javaweb.entity.GenreEntity;
import com.javaweb.entity.MediaItemEntity;
import com.javaweb.entity.UserMediaLogEntity;
import com.javaweb.exceptions.AlreadyTrackedException;
import com.javaweb.exceptions.UnauthorizedException;
import com.javaweb.model.request.TrackMediaRequest;
import com.javaweb.model.request.UpdateTrackingRequest;
import com.javaweb.model.response.BooksResponse;
import com.javaweb.model.response.MediaSearchResponse;
import com.javaweb.model.response.MoviesResponse;
import com.javaweb.model.response.MusicResponse;
import com.javaweb.model.response.TVSeriesResponse;
import com.javaweb.model.response.TrackingResponse;
import com.javaweb.model.response.VideoGamesResponse;
import com.javaweb.repository.MediaItemRepository;
import com.javaweb.repository.UserMediaLogRepository;
import com.javaweb.repository.UserRepository;
import com.javaweb.service.TrackingService;

@Service
public class TrackingServiceImpl implements TrackingService {

    private static final String DEFAULT_STATUS = "PLAN_TO_WATCH";

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserMediaLogRepository userMediaLogRepository;

    @Autowired
    private MediaItemRepository mediaItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Page<TrackingResponse> getTrackedMediaItems(Integer userId, String type, Pageable pageable) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UnauthorizedException("You must be authenticated to see your tracking space."));

        Page<UserMediaLogEntity> logsPage = (type == null || type.isBlank())
                ? userMediaLogRepository.findByUser_Id(userId, pageable)
                : userMediaLogRepository.findByUser_IdAndMediaItem_MediaType_TypeName(userId, type, pageable);

        return logsPage.map(this::mapToTrackingResponse);
    }


    @Override
    public TrackingResponse trackMediaItem(TrackMediaRequest request, Integer userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UnauthorizedException("You must be authenticated to track a media item."));

        Integer mediaItemId = request.getMediaItemId();
        if (mediaItemId == null) {
            throw new IllegalArgumentException("mediaItemId is required.");
        }

        // ✅ Nếu đã track rồi -> 409
        if (userMediaLogRepository.existsByUser_IdAndMediaItem_MediaItemId(userId, mediaItemId)) {
            throw new AlreadyTrackedException("You already tracked this media item.");
        }

        MediaItemEntity mediaItemEntity = mediaItemRepository.findById(mediaItemId)
                .orElseThrow(() -> new RuntimeException("Media item not found."));

        UserMediaLogEntity entity = new UserMediaLogEntity();
        entity.setCreatedAt(new Date());
        entity.setMediaItem(mediaItemEntity);
        entity.setUser(userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found.")));

        // ✅ set status/comment/rating
        String status = normalizeStatus(request.getStatus());
        entity.setStatus(status);
        entity.setComment(request.getComment());
        entity.setRating(request.getRating());

        UserMediaLogEntity saved = userMediaLogRepository.save(entity);

        return mapToTrackingResponse(saved);
    }

    @Override
    public void deleteMediaItemTracked(Integer userMediaLogId, Integer userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UnauthorizedException("You must be authenticated to delete a media track."));

        UserMediaLogEntity userMediaLogEntity = userMediaLogRepository.findById(userMediaLogId)
                .orElseThrow(() -> new RuntimeException("User media log not found."));

        // ✅ FIX: Integer compare
        if (!userId.equals(userMediaLogEntity.getUser().getId())) {
            throw new UnauthorizedException("You can't delete a media item tracked you didn't track.");
        }

        userMediaLogRepository.delete(userMediaLogEntity);
    }
    @Override
    public TrackingResponse updateTrackedMediaItem(Integer logId, UpdateTrackingRequest request, Integer userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UnauthorizedException("You must be authenticated to update a media track."));

        UserMediaLogEntity log = userMediaLogRepository.findById(logId)
                .orElseThrow(() -> new RuntimeException("User media log not found."));

        // ownership
        if (!userId.equals(log.getUser().getId())) {
            throw new UnauthorizedException("You can't update a media track you don't own.");
        }

        // update fields (chỉ update cái nào gửi lên)
        if (request.getStatus() != null && !request.getStatus().isBlank()) {
            log.setStatus(normalizeStatus(request.getStatus()));
        }
        if (request.getComment() != null) {
            log.setComment(request.getComment());
        }
        if (request.getRating() != null) {
            log.setRating(request.getRating());
        }

        UserMediaLogEntity saved = userMediaLogRepository.save(log);
        return mapToTrackingResponse(saved);
    }
    // ===== helper: validate status =====
    private String normalizeStatus(String status) {
        if (status == null || status.isBlank()) return DEFAULT_STATUS;

        String s = status.trim().toUpperCase();

        // only allow these 3 values
        if (!s.equals("PLAN_TO_WATCH") && !s.equals("WATCHING") && !s.equals("COMPLETED")) {
            throw new IllegalArgumentException("Invalid status. Use PLAN_TO_WATCH / WATCHING / COMPLETED.");
        }
        return s;
    }

    // ===== mapping: log -> TrackingResponse =====
    private TrackingResponse mapToTrackingResponse(UserMediaLogEntity log) {
        TrackingResponse res = new TrackingResponse();
        res.setLogId(log.getLogId());
        res.setStatus(log.getStatus());
        res.setComment(log.getComment());
        res.setRating(log.getRating());
        res.setCreatedAt(log.getCreatedAt());

        res.setMedia(mapToSearchResponse(log.getMediaItem()));
        return res;
    }

    // ===== mapping: media item -> MediaSearchResponse (giữ nguyên code bạn) =====
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
}
