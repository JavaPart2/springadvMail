package be.hvwebsites.mail.mailing;

import be.hvwebsites.mail.domain.Lid;
import be.hvwebsites.mail.exceptions.KanMailNietZendenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

@Component
public class DefaultLidMailing implements LidMailing{
    private final JavaMailSender sender;
    private final HttpServletRequest request;
    private final String emailAdresWebMaster;

    public DefaultLidMailing(JavaMailSender sender, HttpServletRequest request,
                             @Value("${emailAdresWebMaster}") String emailAdresWebMaster) {
        this.sender = sender;
        this.request = request;
        this.emailAdresWebMaster = emailAdresWebMaster;
    }

    @Override
    @Async
    public void stuurMailNaRegistratie(Lid lid) {
        try {
            var message = sender.createMimeMessage();
            var helper = new MimeMessageHelper(message);
            var urlVanDePostRequest = request.getRequestURL().toString();
            var urlVanDeLidInfo = urlVanDePostRequest + "/" + lid.getId();
            var tekst = "<h1>Je bent nu lid.</h1>Je nummer is:" + lid.getId() +
                    "." + "Je ziet je info <a href='" + urlVanDeLidInfo + "'>hier</a>.";
            helper.setTo(lid.getEmailAdres());
            helper.setSubject("Geregistreerd");
            helper.setText(tekst, true);
            sender.send(message);
        } catch (MailException | MessagingException ex) {
            throw new KanMailNietZendenException(ex);
        }
    }

    @Override
    public void stuurMailMetAantalLeden(long aantalLeden) {
        try {
            var message = new SimpleMailMessage();
            message.setTo(emailAdresWebMaster);
            message.setSubject("Aantal Leden");
            message.setText(aantalLeden + " leden");
            sender.send(message);
        }catch (MailException ex){
            throw new KanMailNietZendenException(ex);
        }
    }
}
