package bsep.admin.service;

import bsep.admin.model.LogConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LogConfigService {

    private static Logger logger = LogManager.getLogger(LogConfigService.class);

    public boolean sendLogConfig(LogConfig logConfig) {
        logger.info("Attempting to send log config to another backend.");
        try{
            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<LogConfig> request = new HttpEntity<>(logConfig);
            ResponseEntity<?> responseEntity = restTemplate.exchange("https://localhost:8085/log-config/send-log-config", HttpMethod.POST, request, ResponseEntity.class);
            return responseEntity.getStatusCode() == HttpStatus.OK;
        } catch (Exception e ){
            logger.error("Failed to send log config to another backend.");
            return false;
        }
    }
}
