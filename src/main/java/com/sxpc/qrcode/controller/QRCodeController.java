package com.sxpc.qrcode.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class QRCodeController {
    @GetMapping("/")
    public String index() {
        return "index";
    }
}