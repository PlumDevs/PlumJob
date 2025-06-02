package com.plumdevs.plumjob.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import java.io.IOException;

@Controller
public class CvTemplatesController {

    @GetMapping("/CVBuilder/template1")
    public ResponseEntity<Resource> serveTemplate1() throws IOException {
        return ResponseEntity.ok()
                .body(new ClassPathResource("/CvTemplates/template1.html"));
    }

    @GetMapping("/CVBuilder/template2")
    public ResponseEntity<Resource> serveTemplate2() throws IOException {
        return ResponseEntity.ok()
                .body(new ClassPathResource("/CvTemplates/template2.html"));
    }

    @GetMapping("/CVBuilder/template3")
    public ResponseEntity<Resource> serveTemplate3() throws IOException {
        return ResponseEntity.ok()
                .body(new ClassPathResource("/CvTemplates/template3.html"));
    }
}


