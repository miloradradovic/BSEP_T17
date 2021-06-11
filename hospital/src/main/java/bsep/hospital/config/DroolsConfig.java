package bsep.hospital.config;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DroolsConfig {

    @Bean
    public KieSession kieContainer() {
        KieContainer kieContainer = KieServices.Factory.get().getKieClasspathContainer();
        return kieContainer.newKieSession("rulesSession");
    }

}
