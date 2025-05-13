package com.eventeasyv1.service;
import com.eventeasyv1.dto.InvitationDto;
import com.eventeasyv1.dto.input.InvitationCreateDto;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface InvitationService {
    InvitationDto sendInvitation(Long evenementId, InvitationCreateDto invitationDto, Authentication authentication);
    List<InvitationDto> getInvitationsForEvenement(Long evenementId, Authentication authentication);
    void cancelInvitation(Long evenementId, Long invitationId, Authentication authentication);
}