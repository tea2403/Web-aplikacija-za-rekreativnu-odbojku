package fer.zavrsni.Odbojka.service.impl;
import fer.zavrsni.Odbojka.domain.Osoba;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Set;
@Service
public class Email {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Environment env;

    @Async
    public void salji(String poruka, String naslov, Set<Osoba> primatelji) {
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(naslov);
        email.setText(poruka);
        email.setFrom(env.getProperty("support.email"));
        for (Osoba i : primatelji) {
            email.setTo(i.getEmail());
            mailSender.send(email);
        }
    }
}
