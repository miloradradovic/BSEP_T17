package bsep.admin.service;

import bsep.admin.model.LogConfig;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LogConfigService {

    public boolean sendLogConfig(LogConfig logConfig) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<LogConfig> request = new HttpEntity<>(logConfig);
        ResponseEntity<?> responseEntity = restTemplate.exchange("https://localhost:8085/log-config/send-log-config", HttpMethod.POST, request, ResponseEntity.class);
        return responseEntity.getStatusCode() == HttpStatus.OK;
    }
}
