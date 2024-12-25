package com.test.ProjectTask.controller;

import com.test.ProjectTask.googlesheet.GoogleSheetWriter;
import com.test.ProjectTask.model.User;

import com.test.ProjectTask.service.EmailService;
import com.test.ProjectTask.service.OtpService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Controller
public class UserController {

    @Autowired
    private GoogleSheetWriter googleSheetWriter;

    @Autowired
    private OtpService otpService;

    @Autowired
    private EmailService emailService;


    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/generate-otp")
    public String generateOtp(@RequestParam String name, @RequestParam String email, Model model, HttpSession session) {
        String otp = otpService.generateOtp();

        session.setAttribute("name", name);
        session.setAttribute("email", email);
        emailService.sendEmail(email, "Your OTP Code", "Your OTP is: " + otp);
       // model.addAttribute("name", name);
       // model.addAttribute("email", email);


        return "verify";
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam String name, @RequestParam String email, @RequestParam String otp, Model model, HttpSession session) throws GeneralSecurityException, IOException {
        String namedb = (String) session.getAttribute("name");
        String emaildb = (String) session.getAttribute("email");

        if (otpService.validateOtp(otp)) {

            User user = new User();

            user.setName(namedb);
            user.setEmail(emaildb);
            //user.setName((String) session.getAttribute("name")
           // userRepository.save(user);

            googleSheetWriter.writeDataIntoSheet(user);

            return "result";
        } else {
            model.addAttribute("error", "Invalid OTP");

            return "error";
        }
    }
}
