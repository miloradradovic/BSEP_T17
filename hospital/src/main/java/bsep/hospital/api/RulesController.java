package bsep.hospital.api;

import bsep.hospital.dto.AdminRuleDTO;
import bsep.hospital.dto.DoctorDroolsDTO;
import bsep.hospital.dto.DoctorRuleDTO;
import bsep.hospital.model.Patient;
import bsep.hospital.service.PatientService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.Invoker;
import org.drools.template.ObjectDataCompiler;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(value = "/rules", produces = MediaType.APPLICATION_JSON_VALUE)
public class RulesController {

    @Autowired
    PatientService patientService;

    private static Logger logger = LogManager.getLogger(RulesController.class);

    @RequestMapping(value = "doctor-rule", method = RequestMethod.POST)
    public ResponseEntity<?> createDoctorRule(@RequestBody DoctorRuleDTO ruleDTO) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            KeycloakPrincipal kcp = (KeycloakPrincipal) authentication.getPrincipal();

            KeycloakSecurityContext session = kcp.getKeycloakSecurityContext();
            AccessToken accessToken = session.getToken();
            logger.info("User with the email " + accessToken.getEmail() + " is attempting to create doctor rule.");
            //DoctorRuleDTO ruleDTO = new DoctorRuleDTO("testRule", 1, "TEMPERATURE", 20.0, "==");
            InputStream template = new FileInputStream("../hospital/src/main/resources/rules/doctor-template.drt");

            Patient patient = patientService.findByNameAndSurname(ruleDTO.getPatient().split(" ")[0], ruleDTO.getPatient().split(" ")[1]);

            List<DoctorDroolsDTO> arguments = new ArrayList<>();
            arguments.add(new DoctorDroolsDTO(ruleDTO.getRuleName(), patient.getId(), ruleDTO.getType(), ruleDTO.getValue(), ruleDTO.getOperation()));
            ObjectDataCompiler compiler = new ObjectDataCompiler();
            String drl = compiler.compile(arguments, template);

            FileOutputStream drlFile = new FileOutputStream("../hospital/src/main/resources/rules/" + ruleDTO.getRuleName() + ".drl", false);
            drlFile.write(drl.getBytes());
            drlFile.close();

            InvocationRequest request = new DefaultInvocationRequest();
            request.setPomFile(new File("../hospital/pom.xml"));
            request.setGoals(Arrays.asList("clean", "install"));

            Invoker invoker = new DefaultInvoker();
            invoker.setMavenHome(new File(System.getenv("M2_HOME")));
            invoker.execute(request);
            logger.info("Successfully created doctor rule.");

        } catch (Exception e) {
            logger.error("Failed to create doctor rule.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "admin-rule", method = RequestMethod.POST)
    public ResponseEntity<?> createAdminRule(@RequestBody AdminRuleDTO ruleDTO) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            KeycloakPrincipal kcp = (KeycloakPrincipal) authentication.getPrincipal();

            KeycloakSecurityContext session = kcp.getKeycloakSecurityContext();
            AccessToken accessToken = session.getToken();
            logger.info("User with the email " + accessToken.getEmail() + " is attempting to create admin rule.");
            InputStream template = new FileInputStream("../hospital/src/main/resources/rules/admin-template.drt");

            List<AdminRuleDTO> arguments = new ArrayList<>();
            arguments.add(ruleDTO);
            ObjectDataCompiler compiler = new ObjectDataCompiler();
            String drl = compiler.compile(arguments, template);

            FileOutputStream drlFile = new FileOutputStream("../hospital/src/main/resources/rules/" + ruleDTO.getRuleName() + ".drl", false);
            drlFile.write(drl.getBytes());
            drlFile.close();

            InvocationRequest request = new DefaultInvocationRequest();
            request.setPomFile(new File("../hospital/pom.xml"));
            request.setGoals(Arrays.asList("clean", "install"));

            Invoker invoker = new DefaultInvoker();
            invoker.setMavenHome(new File(System.getenv("M2_HOME")));
            invoker.execute(request);
            logger.info("Successfully created admin rule.");

        } catch (Exception e) {
            logger.error("Failed to create admin rule.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }


}
