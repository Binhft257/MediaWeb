package com.javaweb.controllers;

import com.javaweb.model.response.UserHistoryResponse;
import com.javaweb.security.SecurityUtils;
import com.javaweb.service.UserHistoryService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/history")
public class UserHistoryController {

    private final UserHistoryService userHistoryService;

    public UserHistoryController(UserHistoryService userHistoryService) {
        this.userHistoryService = userHistoryService;
    }

    @PostMapping("/{mediaItemId}/view")
    public ResponseEntity<Void> recordView(@PathVariable Integer mediaItemId) {
        Integer me = SecurityUtils.getPrincipal().getId();
        userHistoryService.recordView(me, mediaItemId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<Page<UserHistoryResponse>> myHistory(
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Integer me = SecurityUtils.getPrincipal().getId();
        return ResponseEntity.ok(userHistoryService.getMyHistory(me, pageable));
    }

    @DeleteMapping("/{mediaItemId}")
    public ResponseEntity<Void> deleteOne(@PathVariable Integer mediaItemId) {
        Integer me = SecurityUtils.getPrincipal().getId();
        userHistoryService.deleteOne(me, mediaItemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> clearAll() {
        Integer me = SecurityUtils.getPrincipal().getId();
        userHistoryService.clearAll(me);
        return ResponseEntity.noContent().build();
    }
}
