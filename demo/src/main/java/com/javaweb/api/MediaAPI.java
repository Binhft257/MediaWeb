package com.javaweb.api;

import com.javaweb.model.request.MediaSearchRequest;
import com.javaweb.model.response.MediaSearchResponse;
import com.javaweb.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/medias")
public class MediaAPI {
    @Autowired
    private MediaService mediaService;
    @GetMapping
    public Page<MediaSearchResponse> searchBuildings(
            MediaSearchRequest request,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit
    ){
        PageRequest pageable = PageRequest.of(page - 1, limit);
        return mediaService.getMedias(pageable, request);
    }
}
