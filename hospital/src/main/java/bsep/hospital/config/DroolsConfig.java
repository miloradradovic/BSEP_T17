package bsep.hospital.config;

import bsep.hospital.logging.IPAddress;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@Configuration
public class DroolsConfig {

    private static Logger logger = LogManager.getLogger(DroolsConfig.class);

    @Bean
    public KieSession kieContainer() {
        logger.info("Creating kie session");
        KieContainer kieContainer = KieServices.Factory.get().getKieClasspathContainer();
        KieSession kieSession = kieContainer.newKieSession("rulesSession");

        File file = new File("src/main/resources/dangerous_ips.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if(!line.equals(""))
                    kieSession.insert(new IPAddress(line));
            }
        } catch (IOException e) {
            logger.error("Error reading dangerous ip-s from file.");
        }

        return kieSession;
    }

}
