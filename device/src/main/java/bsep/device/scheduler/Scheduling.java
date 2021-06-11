package bsep.device.scheduler;

import bsep.device.enums.MessageType;
import bsep.device.model.PatientMessage;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.SerializationUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Random;

@Configuration
@EnableScheduling
public class Scheduling {

    public Scheduling() {
    }

    @Scheduled(fixedRate = 3000, initialDelay = 3000)
    public void sendHearthBeat() {
        try {

            int randomId = getRandomID(1, 11);
            double randomValue = getRandomValue(30, 230);
            String message = "For patient with ID: " + randomId + ", hearth beat is: " + randomValue + ".";
            PatientMessage msg = new PatientMessage(randomId, LocalDateTime.now(), MessageType.HEARTH_BEAT, message);
            byte[] msgByte = SerializationUtils.serialize(msg);

            RestTemplate restTemplate = new RestTemplate();
            assert msgByte != null;
            HttpEntity<byte[]> request = new HttpEntity<>(msgByte);
            ResponseEntity<?> responseEntity = restTemplate.exchange("https://localhost:8085/device", HttpMethod.POST, request, ResponseEntity.class);

            System.out.println(
                    "HearthBeat status code - " + responseEntity.getStatusCode());

        } catch (Exception e) {
            System.out.println(
                    "HearthBeat error - " + e.getMessage());
        }

    }

    @Scheduled(fixedRate = 3000, initialDelay = 3000)
    public void sendPressure() {
        try {

            int randomId = getRandomID(1, 11);
            double randomValue = getRandomValue(50, 200);
            String message = "For patient with ID: " + randomId + ", pressure is: " + randomValue + ".";
            PatientMessage msg = new PatientMessage(randomId, LocalDateTime.now(), MessageType.PRESSURE, message);
            byte[] msgByte = SerializationUtils.serialize(msg);

            RestTemplate restTemplate = new RestTemplate();
            assert msgByte != null;
            HttpEntity<byte[]> request = new HttpEntity<>(msgByte);
            ResponseEntity<?> responseEntity = restTemplate.exchange("https://localhost:8085/device", HttpMethod.POST, request, ResponseEntity.class);

            System.out.println(
                    "Pressure status code - " + responseEntity.getStatusCode());

        } catch (Exception e) {
            System.out.println(
                    "Pressure error - " + e.getMessage());
        }

    }

    @Scheduled(fixedRate = 3000, initialDelay = 3000)
    public void sendTemperature() {
        try {

            int randomId = getRandomID(1, 11);
            double randomValue = getRandomValue(33, 43);
            String message = "For patient with ID: " + randomId + ", temperature is: " + randomValue + ".";
            PatientMessage msg = new PatientMessage(randomId, LocalDateTime.now(), MessageType.TEMPERATURE, message);
            byte[] msgByte = SerializationUtils.serialize(msg);

            RestTemplate restTemplate = new RestTemplate();
            assert msgByte != null;
            HttpEntity<byte[]> request = new HttpEntity<>(msgByte);
            ResponseEntity<?> responseEntity = restTemplate.exchange("https://localhost:8085/device", HttpMethod.POST, request, ResponseEntity.class);

            System.out.println(
                    "Temperature status code - " + responseEntity.getStatusCode());
        } catch (Exception e) {
            System.out.println(
                    "Temperature error - " + e.getMessage());
        }


    }

    public int getRandomID(int min, int max) {
        Random random = new Random();
        return (int) ((Math.random() * (max - min)) + min);
    }

    public double getRandomValue(double min, double max) {
        return ((Math.random() * (max - min)) + min);
    }

}
