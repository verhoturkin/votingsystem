package com.verhoturkin.votingsystem.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ApiDocsController {

    @GetMapping(value = "/")
    public String root() {
        return "redirect:/api-docs";
    }

    @GetMapping(value = "/api-docs")
    public String getDocs() {
        return "api-docs";
    }
}
