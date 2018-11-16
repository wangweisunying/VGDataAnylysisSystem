package model;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

public class EmailAndText {

    public static void sendEmail(String emailFrom , String pw ,String emailTo , String[] emailCC , String subject, String bodyMessage, String attachmentPath) throws AddressException, MessagingException {
        String host = "outlook.office365.com";  //gmail : smtp.gmail.com  vibrant : outlook.office365.com
        /*
         att   @txt.att.net    verizon  @vtext.com
         Sprint	[insert 10-digit number]@messaging.sprintpcs.com
         T-Mobile	[insert 10-digit number]@tmomail.net
         U.S. Cellular	[insert 10-digit number]@email.uscc.net
         Virgin Mobile	[insert 10-digit number]@vmobl.com
         Republic Wireless	[insert 10-digital number]@text.republicwireless.com
         
         */

 /*
         
         Carrier	SMS gateway domain	MMS gateway domain
Alltel	[insert 10-digit number]@message.alltel.com	[insert 10-digit number]@mms.alltelwireless.com
AT&T	[insert 10-digit number]@txt.att.net	[insert 10-digit number]@mms.att.net
Boost Mobile	[insert 10-digit number]@myboostmobile.com	[insert 10-digit number]@myboostmobile.com
Cricket Wireless		[insert 10-digit number]@mms.cricketwireless.net
Project Fi		[insert 10-digit number]@msg.fi.google.com
Sprint	[insert 10-digit number]@messaging.sprintpcs.com	[insert 10-digit number]@pm.sprint.com
T-Mobile	[insert 10-digit number]@tmomail.net	[insert 10-digit number]@tmomail.net
U.S. Cellular	[insert 10-digit number]@email.uscc.net	[insert 10-digit number]@mms.uscc.net
Verizon	[insert 10-digit number]@vtext.com	[insert 10-digit number]@vzwpix.com
Virgin Mobile	[insert 10-digit number]@vmobl.com	[insert 10-digit number]@vmpix.com
Republic Wireless	[insert 10-digital number]@text.republicwireless.com	
         
         */
        boolean sessionDebug = false;
        Properties props = System.getProperties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.required", "true");
        java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        Session mailSession = Session.getDefaultInstance(props, null);
        mailSession.setDebug(sessionDebug);
        Message msg = new MimeMessage(mailSession);
        msg.setFrom(new InternetAddress(emailFrom));
        InternetAddress[] address = {new InternetAddress(emailTo)};
        msg.setRecipients(Message.RecipientType.TO, address);
        if(emailCC.length != 0){
            InternetAddress[] addressCC = new InternetAddress[emailCC.length];
            for(int i = 0 ; i < emailCC.length  ; i ++){
                addressCC[i] = new InternetAddress(emailCC[i]);
            }
            msg.setRecipients(Message.RecipientType.CC, addressCC);
        }
        
        msg.setSubject(subject);
        msg.setSentDate(new Date());
        
        
        Multipart multipart = new MimeMultipart();
        // body part
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText(bodyMessage);
        multipart.addBodyPart(messageBodyPart);
        
        
//         attachment 
        BodyPart attachmentBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(attachmentPath);
        attachmentBodyPart.setDataHandler(new DataHandler(source));
        
        
        String[] arr = attachmentPath.split("\\\\");
        attachmentBodyPart.setFileName(arr[arr.length - 1]);
        
        multipart.addBodyPart(attachmentBodyPart);
        msg.setContent(multipart);
        Transport transport = mailSession.getTransport("smtp");
        transport.connect(host, emailFrom, pw);
        transport.sendMessage(msg, msg.getAllRecipients());
        transport.close();
        System.out.println("email send successfully");

    }

    public static void sendText(String emailFrom , String pw ,String toCellNumber , String subject, String bodyMessage) throws MessagingException {
        String host = "outlook.office365.com";  //gmail : smtp.gmail.com  vibrant : outlook.office365.com
        /*
         att   @txt.att.net    verizon  @vtext.com
         Sprint	[insert 10-digit number]@messaging.sprintpcs.com
         T-Mobile	[insert 10-digit number]@tmomail.net
         U.S. Cellular	[insert 10-digit number]@email.uscc.net
         Virgin Mobile	[insert 10-digit number]@vmobl.com
         Republic Wireless	[insert 10-digital number]@text.republicwireless.com
         
         */

 /*
         
         Carrier	SMS gateway domain	MMS gateway domain
Alltel	[insert 10-digit number]@message.alltel.com	[insert 10-digit number]@mms.alltelwireless.com
AT&T	[insert 10-digit number]@txt.att.net	[insert 10-digit number]@mms.att.net
Boost Mobile	[insert 10-digit number]@myboostmobile.com	[insert 10-digit number]@myboostmobile.com
Cricket Wireless		[insert 10-digit number]@mms.cricketwireless.net
Project Fi		[insert 10-digit number]@msg.fi.google.com
Sprint	[insert 10-digit number]@messaging.sprintpcs.com	[insert 10-digit number]@pm.sprint.com
T-Mobile	[insert 10-digit number]@tmomail.net	[insert 10-digit number]@tmomail.net
U.S. Cellular	[insert 10-digit number]@email.uscc.net	[insert 10-digit number]@mms.uscc.net
Verizon	[insert 10-digit number]@vtext.com	[insert 10-digit number]@vzwpix.com
Virgin Mobile	[insert 10-digit number]@vmobl.com	[insert 10-digit number]@vmpix.com
Republic Wireless	[insert 10-digital number]@text.republicwireless.com	
         
         */
        boolean sessionDebug = false;

        Properties props = System.getProperties();

        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.required", "true");

        java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

        Session mailSession = Session.getDefaultInstance(props, null);
        mailSession.setDebug(sessionDebug);
        Message msg = new MimeMessage(mailSession);
        msg.setFrom(new InternetAddress(emailFrom));
        InternetAddress[] address = {new InternetAddress(toCellNumber)};
        msg.setRecipients(Message.RecipientType.TO, address);
        msg.setSubject(subject);
        msg.setSentDate(new Date());
        msg.setText(bodyMessage);

        Transport transport = mailSession.getTransport("smtp");
        transport.connect(host, emailFrom, pw);
        transport.sendMessage(msg, msg.getAllRecipients());
        transport.close();
        System.out.println("message send successfully");

    }

}
