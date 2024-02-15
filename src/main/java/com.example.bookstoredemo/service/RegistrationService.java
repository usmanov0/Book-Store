package com.example.bookstoredemo.service;

import com.example.bookstoredemo.models.RegistrationRequest;
import com.example.bookstoredemo.models.User;
import com.example.bookstoredemo.models.UserRole;
import com.example.bookstoredemo.service.sender.EmailSender;
import com.example.bookstoredemo.service.token.ConfirmationToken;
import com.example.bookstoredemo.service.validator.EmailValidator;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {
    private final UserService userService;
    private final EmailValidator emailValidator;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSender emailSender;


    public String register(RegistrationRequest request, UserRole role){
        boolean isValidEmail = emailValidator.
                test(request.getEmail());

        if (!isValidEmail){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "email not found");
        }

        String token = userService.SignUp(
                new User(
                        request.getFirstName(),
                        request.getLastName(),
                        request.getEmail(),
                        request.getPassword(),
                        role
                )
        );

        String link = "http://localhost:8080/api/register/confirm?token=" + token;
        emailSender.send(
                request.getEmail(),
                buildEmail(request.getFirstName(), link)
        );

        return token;
    }

    @Transactional
    public String confirmToken(String token){
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(()->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "token not found"));

        if (confirmationToken.getConfirmedAt() != null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isAfter(LocalDateTime.now())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "token expired");
        }

        confirmationTokenService.setConfirmedAt(token);
        userService.enableAppUser(
                confirmationToken.getUser().getEmail());
        return "confirmed";
    }

    private String buildEmail(String name, String link) {

        return "<p>Hi "
                + name + ". Please confirm your email with <a href="
                + link + " taget=\"_blank\">link</a>.It will expire after 15 minutes. Hurry up!</p>";
    }
}


