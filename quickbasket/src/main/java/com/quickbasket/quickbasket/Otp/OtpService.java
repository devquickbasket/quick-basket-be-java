package com.quickbasket.quickbasket.Otp;

import com.quickbasket.quickbasket.customs.services.EmailService;
import com.quickbasket.quickbasket.user.User;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class OtpService {
    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private EmailService emailService;

    public void generateAndSendOtp(String email) {
        String otp = String.format("%06d", new Random().nextInt(999999));

        Optional<Otp> existingOTP = otpRepository.findByEmail(email);

        Otp otpEntity;

        if (existingOTP.isPresent()) {
            otpEntity = existingOTP.get();
        }else{
            otpEntity = new Otp();
        }

        otpEntity.setEmail(email);
        otpEntity.setOtp(otp);
        otpEntity.setExpiryDate(LocalDateTime.now().plusMinutes(5));
        otpRepository.save(otpEntity);

        emailService.sendEmail(email, "Your OTP Code", "Your OTP is: " + otp);
    }

    public boolean verifyOtp(String email, String otp) {
        Optional<Otp> otpEntity = otpRepository.findByEmail(email);

        if (!otpEntity.isPresent()) {
            return false; // No OTP found for this email
        }

        Otp savedOtp = otpEntity.get();

        otpRepository.delete(savedOtp);

        return savedOtp.getOtp().equals(otp) &&
                savedOtp.getExpiryDate().isAfter(LocalDateTime.now());
    }

}
