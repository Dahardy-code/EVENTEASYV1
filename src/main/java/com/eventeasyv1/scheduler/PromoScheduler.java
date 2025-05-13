package com.eventeasyv1.scheduler;

import com.eventeasyv1.service.PromoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PromoScheduler {

    private static final Logger log = LoggerFactory.getLogger(PromoScheduler.class);

    private final PromoService promoService;

    @Autowired
    public PromoScheduler(PromoService promoService) {
        this.promoService = promoService;
    }

    // S'exécute tous les jours à 1h du matin, par exemple
    // Cron expression: seconde minute heure jour_du_mois mois jour_de_la_semaine
    @Scheduled(cron = "0 0 1 * * ?") // Tous les jours à 1h00 AM
    public void deactivateExpiredPromosJob() {
        log.info("Exécution du job de désactivation des promos expirées...");
        promoService.deactivateExpiredPromos();
        log.info("Job de désactivation des promos expirées terminé.");
    }
}