package bsep.device.scheduler;

import bsep.device.enums.MessageType;
import bsep.device.model.PatientMessage;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.SerializationUtils;
import org.springframework.web.client.RestTemplate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.Random;

@Configuration
@EnableScheduling
public class Scheduling {

    private static Logger logger = LogManager.getLogger(Scheduling.class);

    public Scheduling() {
    }

   /* @Scheduled(fixedRate = 10000)
    public void test() {
        try {

            RestTemplate restTemplate = new RestTemplate();

            HttpEntity<String> request = new HttpEntity<>("");
            ResponseEntity<?> responseEntity = restTemplate.exchange("https://localhost:8085/rules", HttpMethod.POST, request, ResponseEntity.class);

            System.out.println(
                    "HearthBeat status code - " + responseEntity.getStatusCode());

        } catch (Exception e) {
            System.out.println(
                    "HearthBeat error - " + e.getMessage());
        }

    }*/

    @Scheduled(fixedRate = 10000, initialDelay = 10000)
    public void sendHearthBeat() {
        try {
            logger.info("Attempting to send heartbeat.");
            int randomId = getRandomID(1, 11);
            double randomValue = getRandomValue(30, 230);
            String message = "For patient with ID: " + randomId + ", hearth beat is: " + String.format("%.2f", randomValue) + ".";
            PatientMessage msg = new PatientMessage(randomId, LocalDateTime.now(), MessageType.HEARTH_BEAT, message);
            byte[] msgByte = SerializationUtils.serialize(msg);

            RestTemplate restTemplate = new RestTemplate();
            assert msgByte != null;
            HttpEntity<byte[]> request = new HttpEntity<>(msgByte);
            ResponseEntity<?> responseEntity = restTemplate.exchange("https://localhost:8085/device", HttpMethod.POST, request, ResponseEntity.class);

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                logger.info("Successfully sent heartbeat.");
            } else {
                logger.error("Failed to send heartbeat because something went wrong in the hospital.");
            }

        } catch (Exception e) {
            logger.error("Failed to send heartbeat because certificate is not valid.");
        }

    }



    @Scheduled(fixedRate = 10000, initialDelay = 10000)
    public void sendPressure() {
        try {
            logger.info("Attempting to send pressure.");
            int randomId = getRandomID(1, 11);
            double randomValue = getRandomValue(50, 200);
            String message = "For patient with ID: " + randomId + ", pressure is: " + String.format("%.2f", randomValue) + ".";
            PatientMessage msg = new PatientMessage(randomId, LocalDateTime.now(), MessageType.PRESSURE, message);
            byte[] msgByte = SerializationUtils.serialize(msg);

            RestTemplate restTemplate = new RestTemplate();
            assert msgByte != null;
            HttpEntity<byte[]> request = new HttpEntity<>(msgByte);
            ResponseEntity<?> responseEntity = restTemplate.exchange("https://localhost:8085/device", HttpMethod.POST, request, ResponseEntity.class);

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                logger.info("Successfully sent pressure.");
            } else {
                logger.error("Failed to send pressure because something went wrong in the hospital.");
            }

        } catch (Exception e) {
            logger.error("Failed to send pressure because certificate is not valid.");
        }

    }

    @Scheduled(fixedRate = 10000, initialDelay = 10000)
    public void sendTemperature() {
        try {
            logger.info("Attempting to send temperature.");
            int randomId = getRandomID(1, 11);
            double randomValue = getRandomValue(33, 43);
            String message = "For patient with ID: " + randomId + ", temperature is: " + String.format("%.2f", randomValue) + ".";
            PatientMessage msg = new PatientMessage(randomId, LocalDateTime.now(), MessageType.TEMPERATURE, message);
            byte[] msgByte = SerializationUtils.serialize(msg);

            RestTemplate restTemplate = new RestTemplate();
            assert msgByte != null;
            HttpEntity<byte[]> request = new HttpEntity<>(msgByte);
            ResponseEntity<?> responseEntity = restTemplate.exchange("https://localhost:8085/device", HttpMethod.POST, request, ResponseEntity.class);

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                logger.info("Successfully sent temperature.");
            } else {
                logger.error("Failed to send temperature because something went wrong in the hospital.");
            }
        } catch (Exception e) {
            logger.error("Failed to send temperature because certificate is not valid.");
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
