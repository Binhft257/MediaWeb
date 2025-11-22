package com.javaweb.service.impl;

import com.javaweb.entity.MediaItemEntity;
import com.javaweb.model.request.MediaSearchRequest;
import com.javaweb.model.response.MediaSearchResponse;
import com.javaweb.repository.MediaItemRepository;
import com.javaweb.repository.MediaItemRepositoryCustom;
import com.javaweb.service.MediaService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
            List<MediaSearchResponse> mediaSearchResponseList = new ArrayList<>();
            List<MediaItemEntity> medias = mediaItemRepositoryCustom.getMediasWithCondition(pageable, mediaSearchRequest);
            for (MediaItemEntity mediaItemEntity : medias) {
                MediaSearchResponse mediaSearchResponse = modelMapper.map(mediaItemEntity, MediaSearchResponse.class);
                if (mediaItemEntity.getGenres() != null) {
                    List<String> genreNames = mediaItemEntity.getGenres()
                            .stream()
                            .map(g -> g.getGenreName())
                            .toList();

                    mediaSearchResponse.setGenre(genreNames);
                }
                mediaSearchResponse.setTypeName(mediaItemEntity.getMediaType().getTypeName());
                mediaSearchResponseList.add(mediaSearchResponse);

            }
        long total = mediaItemRepositoryCustom.getMediasWithCondition(null, mediaSearchRequest).size();

        return new PageImpl<>(mediaSearchResponseList, pageable, total);
    }
}
