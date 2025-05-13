package com.eventeasyv1.service.Impl; // Assurez-vous que le nom du package est correct (impl en minuscules)

import com.eventeasyv1.dao.PromoRepository;
import com.eventeasyv1.dto.PromoDto;
import com.eventeasyv1.dto.input.PromoCreateDto;
import com.eventeasyv1.entities.Promo;
import com.eventeasyv1.exception.BadRequestException;
import com.eventeasyv1.exception.ResourceNotFoundException;
import com.eventeasyv1.service.PromoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional; // Importer Optional
import java.util.stream.Collectors;

@Service
public class PromoServiceImpl implements PromoService {

    private static final Logger log = LoggerFactory.getLogger(PromoServiceImpl.class);

    private final PromoRepository promoRepository;

    @Autowired
    public PromoServiceImpl(PromoRepository promoRepository) {
        this.promoRepository = promoRepository;
    }

    @Override
    @Transactional
    public PromoDto createPromo(PromoCreateDto promoCreateDto) {
        // Utiliser la méthode findByCodePromo qui retourne Optional<Promo>
        if (promoRepository.findByCodePromo(promoCreateDto.getCodePromo().toUpperCase()).isPresent()) {
            throw new BadRequestException("Un code promo avec le nom '" + promoCreateDto.getCodePromo() + "' existe déjà.");
        }
        if (promoCreateDto.getDateFin().isBefore(promoCreateDto.getDateDebut())) {
            throw new BadRequestException("La date de fin ne peut pas être antérieure à la date de début.");
        }

        Promo promo = new Promo();
        promo.setCodePromo(promoCreateDto.getCodePromo().toUpperCase());
        promo.setDescription(promoCreateDto.getDescription());
        promo.setPourcentageReduction(promoCreateDto.getPourcentageReduction());
        promo.setDateDebut(promoCreateDto.getDateDebut());
        promo.setDateFin(promoCreateDto.getDateFin());
        promo.setEstActive(promoCreateDto.getEstActive() != null ? promoCreateDto.getEstActive() : true);

        Promo savedPromo = promoRepository.save(promo);
        log.info("Promo ID {} créée avec code {}", savedPromo.getId(), savedPromo.getCodePromo());
        return mapToDto(savedPromo);
    }

    @Override
    @Transactional
    public PromoDto updatePromo(Long promoId, PromoCreateDto promoUpdateDto) { // Utiliser PromoCreateDto est OK pour la mise à jour ici
        Promo promo = promoRepository.findById(promoId)
                .orElseThrow(() -> new ResourceNotFoundException("Promo", "id", promoId));

        // Vérifier si le nouveau code promo (s'il change) n'existe pas déjà pour une AUTRE promo
        if (promoUpdateDto.getCodePromo() != null && !promo.getCodePromo().equalsIgnoreCase(promoUpdateDto.getCodePromo())) {
            Optional<Promo> existingPromoWithNewCode = promoRepository.findByCodePromo(promoUpdateDto.getCodePromo().toUpperCase());
            // --- CORRECTION DE LA LOGIQUE ICI ---
            if (existingPromoWithNewCode.isPresent() && !existingPromoWithNewCode.get().getId().equals(promoId)) {
                throw new BadRequestException("Un autre code promo avec le nom '" + promoUpdateDto.getCodePromo() + "' existe déjà.");
            }
            // --- FIN CORRECTION ---
            promo.setCodePromo(promoUpdateDto.getCodePromo().toUpperCase());
        }

        if (promoUpdateDto.getDescription() != null) promo.setDescription(promoUpdateDto.getDescription());
        if (promoUpdateDto.getPourcentageReduction() != null) promo.setPourcentageReduction(promoUpdateDto.getPourcentageReduction());
        if (promoUpdateDto.getDateDebut() != null) promo.setDateDebut(promoUpdateDto.getDateDebut());
        if (promoUpdateDto.getDateFin() != null) promo.setDateFin(promoUpdateDto.getDateFin());
        if (promoUpdateDto.getEstActive() != null) promo.setEstActive(promoUpdateDto.getEstActive());

        if (promo.getDateFin().isBefore(promo.getDateDebut())) {
            throw new BadRequestException("La date de fin ne peut pas être antérieure à la date de début.");
        }

        Promo updatedPromo = promoRepository.save(promo);
        log.info("Promo ID {} mise à jour", updatedPromo.getId());
        return mapToDto(updatedPromo);
    }

    @Override
    @Transactional
    public PromoDto activatePromo(Long promoId) {
        Promo promo = promoRepository.findById(promoId)
                .orElseThrow(() -> new ResourceNotFoundException("Promo", "id", promoId));
        promo.setEstActive(true);
        log.info("Promo ID {} activée", promoId);
        return mapToDto(promoRepository.save(promo));
    }

    @Override
    @Transactional
    public PromoDto deactivatePromo(Long promoId) {
        Promo promo = promoRepository.findById(promoId)
                .orElseThrow(() -> new ResourceNotFoundException("Promo", "id", promoId));
        promo.setEstActive(false);
        log.info("Promo ID {} désactivée", promoId);
        return mapToDto(promoRepository.save(promo));
    }

    @Override
    @Transactional
    public void deletePromo(Long promoId) {
        if (!promoRepository.existsById(promoId)) {
            throw new ResourceNotFoundException("Promo", "id", promoId);
        }
        promoRepository.deleteById(promoId);
        log.info("Promo ID {} supprimée", promoId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PromoDto> getAllPromos() {
        return promoRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PromoDto validatePromoCode(String codePromo) {
        Promo promo = promoRepository.findByCodePromoAndEstActiveTrueAndDateFinAfter(codePromo.toUpperCase(), LocalDate.now())
                .orElseThrow(() -> new ResourceNotFoundException("Code promo invalide ou expiré: " + codePromo));
        return mapToDto(promo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PromoDto> getActivePublicPromos() {
        LocalDate today = LocalDate.now();
        List<Promo> promos = promoRepository.findByEstActiveTrueAndDateDebutLessThanEqualAndDateFinGreaterThanEqual(today, today);
        return promos.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deactivateExpiredPromos() {
        LocalDate today = LocalDate.now();
        List<Promo> expiredPromos = promoRepository.findByEstActiveTrueAndDateFinBefore(today);
        if (!expiredPromos.isEmpty()) {
            expiredPromos.forEach(promo -> promo.setEstActive(false));
            promoRepository.saveAll(expiredPromos);
            log.info("{} promos expirées ont été désactivées.", expiredPromos.size());
        } else {
            log.info("Aucune promo expirée à désactiver aujourd'hui.");
        }
    }

    private PromoDto mapToDto(Promo promo) {
        PromoDto dto = new PromoDto();
        dto.setId(promo.getId());
        dto.setCodePromo(promo.getCodePromo());
        dto.setDescription(promo.getDescription());
        dto.setPourcentageReduction(promo.getPourcentageReduction());
        dto.setDateDebut(promo.getDateDebut());
        dto.setDateFin(promo.getDateFin());
        dto.setEstActive(promo.isEstActive());
        return dto;
    }
}