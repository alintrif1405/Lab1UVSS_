package org.example.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @InjectMocks
    @Spy
    private EmailService emailService;

    @Mock
    private JavaMailSender mailSender;
    private final String filePath = "src/test/java/org/example/service/testFile.txt";

    @Test
    void readFile() {
        assertEquals("abcdE<html>body123something</html>", emailService.readFile(filePath));
    }

    @Test
    void readFileCatchesIOExceptionThenReturnsEmptyString() {
        assertEquals("", emailService.readFile("testFileDoesNotExist.txt"));
    }

    @Test
    void sendEmailFromTemplate() {

        doNothing().when(emailService).sendEmailFromTemplate("test@example.com",
                filePath, "test", "test");

        emailService.sendEmailFromTemplate("test@example.com", filePath, "test", "test");

        verify(emailService, times(1)).sendEmailFromTemplate("test@example.com", filePath, "test", "test");
    }

}
