package com.techeer.f5.jmtmonster.domain.friend.controller;

import com.techeer.f5.jmtmonster.domain.friend.dto.mapper.FriendMapper;
import com.techeer.f5.jmtmonster.domain.friend.dto.request.FriendHangOutDto;
import com.techeer.f5.jmtmonster.domain.friend.dto.response.FriendResponseDto;
import com.techeer.f5.jmtmonster.domain.friend.service.FriendService;
import java.util.UUID;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/friends")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService service;
    private final FriendMapper mapper;

    @GetMapping("/{id}")
    public ResponseEntity<FriendResponseDto> getOne(@PathVariable UUID id) {
        return ResponseEntity
                .ok(mapper.toResponseDto(service.findFriendById(id)));
    }

    @GetMapping
    public ResponseEntity<Page<FriendResponseDto>> getList(
            @PageableDefault(size = 20, sort = "createdOn", direction = Direction.DESC) final Pageable pageable,
            @RequestParam(value = "from-user-id", required = false) UUID fromUserId,
            @RequestParam(value = "to-user-id", required = false) UUID toUserId,
            @RequestParam(value = "is-hanging-out", required = false) Boolean isHangingOut
    ) {
        return ResponseEntity
                .ok(service.findAllFriends(pageable, fromUserId, toUserId, isHangingOut)
                        .map(mapper::toResponseDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FriendResponseDto> hangOut(
            @PathVariable UUID id,
            @Valid @RequestBody FriendHangOutDto dto
    ) {
        return ResponseEntity
                .ok(mapper.toResponseDto(service.hangOutWithFriend(id, dto.getIsHangingOut())));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.deleteFriendById(id);
        return ResponseEntity
                .noContent()
                .build();
    }
}
