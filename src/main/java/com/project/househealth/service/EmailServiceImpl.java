package com.project.househealth.service;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Value("${resend.api.key}")
    private String apiKey;

    private void sendEmail(
            String recipientEmail,
            String subject,
            String html
    ) {

        try {

            Resend resend = new Resend(apiKey);

            CreateEmailOptions params = CreateEmailOptions.builder()
                    .from("HouseHealth <onboarding@resend.dev>")
                    .to(recipientEmail)
                    .subject(subject)
                    .html(html)
                    .build();

            CreateEmailResponse response =
                    resend.emails().send(params);

            System.out.println(
                    "Email sent successfully: " +
                            response.getId()
            );

        } catch (ResendException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    @Override
    public void sendVerificationEmail(
            String recipientEmail,
            String verificationLink
    ) {

        String html = """
                    <html>
                    <body style="font-family: Arial, sans-serif; color:#333; line-height:1.6;">
                        <h2 style="color:#10b981;">Welcome to HouseHealth!</h2>
                    
                        <p>Thanks for creating your account.</p>
                    
                        <p>Please verify your email by clicking the button below:</p>
                    
                        <p>
                            <a href="%s"
                               style="background:#10b981;color:white;padding:12px 24px;text-decoration:none;border-radius:8px;display:inline-block;">
                                Verify Email
                            </a>
                        </p>
                    
                        <p>If the button doesn't work, copy and paste this link into your browser:</p>
                    
                        <p>%s</p>
                    
                        <hr>
                    
                        <p style="color:#777;font-size:13px;">
                            This link expires in 24 hours.
                        </p>
                    </body>
                    </html>
                    """.formatted(verificationLink, verificationLink);

                            sendEmail(
                                    recipientEmail,
                                    "Verify your HouseHealth account",
                                    html
                            );
    }

    @Override
    public void sendPasswordResetEmail(
            String recipientEmail,
            String resetLink
    ) {

        String html = """
                <html>
                <body style="font-family: Arial, sans-serif; color:#333; line-height:1.6;">
                    <h2 style="color:#10b981;">Reset Your Password</h2>
                
                    <p>We received a request to reset your HouseHealth password.</p>
                
                    <p>
                        <a href="%s"
                           style="background:#10b981;color:white;padding:12px 24px;text-decoration:none;border-radius:8px;display:inline-block;">
                            Reset Password
                        </a>
                    </p>
                
                    <p>If the button doesn't work, copy and paste this link into your browser:</p>
                
                    <p>%s</p>
                
                    <hr>
                
                    <p style="color:#777;font-size:13px;">
                        This link expires in 30 minutes.
                    </p>
                
                    <p style="color:#777;font-size:13px;">
                        If you didn't request a password reset, you can safely ignore this email.
                    </p>
                </body>
                </html>
                """.formatted(resetLink, resetLink);

                        sendEmail(
                                recipientEmail,
                                "Reset your HouseHealth password",
                                html
                        );
    }
}