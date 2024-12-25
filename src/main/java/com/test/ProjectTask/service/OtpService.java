package com.test.ProjectTask.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class OtpService {

    private String generatedOtp;

    public String generateOtp() {
        generatedOtp = String.format("%04d", new Random().nextInt(10000));
        return generatedOtp;
    }

    public boolean validateOtp(String otp) {
        return generatedOtp.equals(otp);
    }
}
