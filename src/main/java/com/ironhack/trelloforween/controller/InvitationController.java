package com.ironhack.trelloforween.controller;

import com.ironhack.trelloforween.dto.InviteRequest;
import com.ironhack.trelloforween.entity.User;
import com.ironhack.trelloforween.service.InvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/invitations")
@RequiredArgsConstructor
@Tag(name = "Invitations", description = "Endpoints for managing board invitations")
public class InvitationController {

    private final InvitationService invitationService;

    @Operation(summary = "Send invitation", description = "Sends an email invitation to a user to join a project board.")
    @PostMapping("/send")
    public ResponseEntity<String> sendInvitation(@RequestBody InviteRequest request, @AuthenticationPrincipal User inviter) {
        invitationService.sendInvitation(request.getBoardId(), request.getInviteeEmail(), inviter);
        return ResponseEntity.ok("Invitation sent via email!");
    }

    @Operation(summary = "Accept invitation", description = "Accepts a board invitation using the token sent via email.")
    @GetMapping("/accept")
    public ResponseEntity<String> acceptInvitation(@RequestParam String token) {
        String result = invitationService.acceptInvitation(token);
        return ResponseEntity.ok(result);
    }
}
