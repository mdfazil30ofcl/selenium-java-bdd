package tests;

import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;

public class SendEmailCheck {
	public static void main(String[] args) {
		final String username = "";
		final String password = "";
		final String from = ""; // change to from email address
		final String to = ""; // change to to email address
		final String cc = ""; // change to cc email address
		final String bcc = ""; // change to bcc email address
		final String subject = "Test Email"; // change to your subject
		final String msg = "Test Email from Hello."; // change to your message

		Properties props = new Properties();
		props.put("mail.smtp.auth", false);
		props.put("mail.smtp.starttls.enable", false);
		props.put("mail.smtp.host", "");
		props.put("mail.smtp.port", "25");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			// below code only requires if your want cc email address
			message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
			// below code only requires if your want bcc email address
			message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(bcc));
			message.setSubject(subject);
			message.setText(msg);
			System.out.println("Sending");
			Transport.send(message);
			System.out.println("Done");
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}
