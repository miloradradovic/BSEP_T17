package bsep.hospital.api;

import bsep.hospital.dto.PatientStatusDTO;
import bsep.hospital.model.PatientStatus;
import org.apache.maven.shared.invoker.*;
import org.drools.template.ObjectDataCompiler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@CrossOrigin(origins = "https://localhost:4205")
@RestController
@RequestMapping(value = "/rules", produces = MediaType.APPLICATION_JSON_VALUE)
public class RulesController {

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<List<PatientStatusDTO>> createDoctorRule() {
        InputStream template = null;
        try {
            template = new FileInputStream("../hospital/src/main/resources/rules/doctor-template.drt");

            List<IntervalDTO> arguments = new ArrayList<>();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyy HH:mm");
            arguments.add(new IntervalDTO("2021-06-05", "2021-06-07"));
            ObjectDataCompiler compiler = new ObjectDataCompiler();
            String drl = compiler.compile(arguments, template);

            FileOutputStream drlFile = new FileOutputStream(new File(
                    "../hospital/src/main/resources/rules/interval-report.drl"), false);
            drlFile.write(drl.getBytes());
            drlFile.close();

            InvocationRequest request = new DefaultInvocationRequest();
            //request.setInputStream(InputStream.nullInputStream());
            request.setPomFile(new File("../hospital/pom.xml"));
            request.setGoals(Arrays.asList("clean", "install"));

            Invoker invoker = new DefaultInvoker();
            invoker.setMavenHome(new File(System.getenv("M2_HOME")));
            invoker.execute(request);

        } catch (Exception e) {
            e.printStackTrace();
        }




    }
}
