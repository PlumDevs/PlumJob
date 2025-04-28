package com.plumdevs.plumjob.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

public class CvTemplatesController {
    @GetMapping("template1")
    public String GetTemplate1() {
        return "template1.html";
    }

    @GetMapping("template2")
    public String GetTemplate2() {
        return "template2.html";
    }

    @GetMapping("template3")
    public String GetTemplate3() { return "template3.html"; }
}



