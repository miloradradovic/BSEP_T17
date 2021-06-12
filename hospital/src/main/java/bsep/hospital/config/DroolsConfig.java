package bsep.hospital.config;

import bsep.hospital.api.CertificateRequestController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DroolsConfig {

    private static Logger logger = LogManager.getLogger(DroolsConfig.class);

    @Bean
    public KieSession kieContainer() {
        logger.info("Creating kie session");
        KieContainer kieContainer = KieServices.Factory.get().getKieClasspathContainer();
        return kieContainer.newKieSession("rulesSession");
    }

}
