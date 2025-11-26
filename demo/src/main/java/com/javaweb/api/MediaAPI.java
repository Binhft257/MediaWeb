package com.javaweb.api;

import com.javaweb.model.request.MediaSearchRequest;
import com.javaweb.model.response.MediaSearchResponse;
import com.javaweb.security.SecurityUtils;
import com.javaweb.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}")
    public MediaSearchResponse getDetail(@PathVariable Integer id) {
        return mediaService.getMediasDetail(id);
    }
}
