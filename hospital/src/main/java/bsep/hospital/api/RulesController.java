package bsep.hospital.api;

import bsep.hospital.dto.AdminRuleDTO;
import bsep.hospital.dto.DoctorRuleDTO;
import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.Invoker;
import org.drools.template.ObjectDataCompiler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    @RequestMapping(value = "doctor-rule", method = RequestMethod.POST)
    public ResponseEntity<?> createDoctorRule(@RequestBody DoctorRuleDTO ruleDTO) {
        try {
            //DoctorRuleDTO ruleDTO = new DoctorRuleDTO("testRule", 1, "TEMPERATURE", 20.0, "==");
            InputStream template = new FileInputStream("../hospital/src/main/resources/rules/doctor-template.drt");

            List<DoctorRuleDTO> arguments = new ArrayList<>();
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

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "admin-rule", method = RequestMethod.POST)
    public ResponseEntity<?> createAdminRule(@RequestBody AdminRuleDTO ruleDTO) {
        try {
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

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }


}
