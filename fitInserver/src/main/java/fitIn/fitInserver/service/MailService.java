package fitIn.fitInserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender sender;

    public boolean sendEmail(String toAddress, String subject, String body){
        System.out.println("보낼주소 " + toAddress);
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try{
            helper.setTo(toAddress);
            helper.setSubject(subject);
            helper.setText(body);

        }catch (MessagingException e){
            System.out.println(toAddress+"에게 메일전송실패");
            e.printStackTrace();
            return false;
        }
        try{
            sender.send(message);
            System.out.println(toAddress+"에게 메일전송");
            return true;
        }catch (MailException e){
            System.out.println("MailException 발생");
            e.printStackTrace();
            return false;
        }



    }
}
