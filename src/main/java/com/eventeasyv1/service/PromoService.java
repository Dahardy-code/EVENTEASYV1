package com.eventeasyv1.service;

import com.eventeasyv1.dto.PromoDto;
import com.eventeasyv1.dto.input.PromoCreateDto;
import java.util.List;

public interface PromoService {
    // Pour Admin
    PromoDto createPromo(PromoCreateDto promoCreateDto);
    PromoDto updatePromo(Long promoId, PromoCreateDto promoUpdateDto); // Peut utiliser le même DTO que create
    PromoDto activatePromo(Long promoId);
    PromoDto deactivatePromo(Long promoId);
    void deletePromo(Long promoId);
    List<PromoDto> getAllPromos(); // Pour Admin

    // Pour Client/Public
    PromoDto validatePromoCode(String codePromo); // Vérifie si un code est valide et actif
    List<PromoDto> getActivePublicPromos(); // Liste les promos visibles par les utilisateurs

    // Pour le Scheduler
    void deactivateExpiredPromos();
}
