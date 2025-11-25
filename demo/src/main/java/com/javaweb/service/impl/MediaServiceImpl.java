package com.javaweb.service.impl;

import com.javaweb.entity.MediaItemEntity;
import com.javaweb.model.request.MediaSearchRequest;
import com.javaweb.model.response.*;
import com.javaweb.repository.MediaItemRepository;
import com.javaweb.repository.MediaItemRepositoryCustom;
import com.javaweb.service.MediaService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class MediaServiceImpl implements MediaService {
    @Autowired
    private MediaItemRepositoryCustom mediaItemRepositoryCustom;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Page<MediaSearchResponse> getMedias(Pageable pageable, MediaSearchRequest mediaSearchRequest) {
        List<MediaSearchResponse> resultList = new ArrayList<>();

        List<MediaItemEntity> medias =
                mediaItemRepositoryCustom.getMediasWithCondition(pageable, mediaSearchRequest);

        for (MediaItemEntity entity : medias) {

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
                    dto = new MediaSearchResponse();
            }

            dto.setMediaItemId(entity.getMediaItemId());
            dto.setTitle(entity.getTitle());
            dto.setDescription(entity.getDescription());
            dto.setCountry(entity.getCountry());
            dto.setLanguage(entity.getLanguage());
            dto.setContentRating(entity.getContentRating());
            dto.setReleaseDate(entity.getReleaseDate());
            dto.setUrlItem(entity.getUrlItem());
            dto.setTypeName(type);

            if (entity.getGenres() != null) {
                List<String> genreNames = entity.getGenres()
                        .stream()
                        .map(g -> g.getGenreName())
                        .toList();

                dto.setGenre(genreNames);
            }

            resultList.add(dto);
        }

        long total = mediaItemRepositoryCustom
                .getMediasWithCondition(null, mediaSearchRequest)
                .size();

        return new PageImpl<>(resultList, pageable, total);
    }
    }

