package utils;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import pages.TestExecutor;

public class SendEmailUtils extends TestExecutor {

	private static Message message;
	public String smtpHost;
	public String smtpPort;
	public String fromAddress;
	public String toAddress;
	public String ccAddress;
	public String bccAddress;
	public String emailSubject;
	public String emailMessageHTML;

	public SendEmailUtils() {
		setEmailProperties();
		Properties props = new Properties();
		props.put("mail.smtp.auth", true);
		props.put("mail.smtp.starttls.enable", true);
		props.put("mail.smtp.host", smtpHost);
		props.put("mail.smtp.port", smtpPort);
		props.put("mail.smtp.ssl.trust", "*");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("", "");
			}
		});

		message = new MimeMessage(session);
	}

	private void setEmailProperties() {
		smtpHost = propertyReader.readPropertyEmail("SMTPHost");
		smtpPort = propertyReader.readPropertyEmail("SMTPPort");
		fromAddress = propertyReader.readPropertyEmail("FromEmail");
		toAddress = propertyReader.readPropertyEmail("ToEmail");
		ccAddress = propertyReader.readPropertyEmail("CcEmail");
		bccAddress = propertyReader.readPropertyEmail("BccEmail");
		emailSubject = propertyReader.readPropertyEmail("EmailSubject");
	}

	public void setFromAddress(String fromAddress) {
		try {
			message.setFrom(new InternetAddress(fromAddress));
			System.out.println("From Email Address: " + fromAddress);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	public void setToAddress(String toAddress) {
		toAddress = toAddress.replace(";", ",");
		try {
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddress));
			System.out.println("To Email Address: " + toAddress);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	public void setCCAddress(String ccAddress) {
		if (!ccAddress.isEmpty()) {
			ccAddress = ccAddress.replace(";", ",");
			try {
				message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(ccAddress));
				System.out.println("CC Email Address: " + ccAddress);
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
	}

	public void setBCCAddress(String bccAddress) {
		if (!bccAddress.isEmpty()) {
			bccAddress = bccAddress.replace(";", ",");
			try {
				message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(bccAddress));
				System.out.println("BCC Email Address: " + bccAddress);
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
	}

	public class SendEmail extends Thread {
		@Override
		public void run() {
			if (propertyReader.readPropertyEmail("TriggerEmail_EndOfRun").equals("Yes")) {
				sendEmail.sendEmail();
			}
		}
	}

	public void sendEmail() {
		try {
			setFromAddress(fromAddress);
			setToAddress(toAddress);
			setCCAddress(ccAddress);
			setBCCAddress(bccAddress);
			message.setSubject(emailSubject);
			message.setContent(emailGen.generateEmailBody(), "text/html");
			System.out.println("Sending");
			Transport.send(message);
			System.out.println("Done");
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	public void sendFinalEmail() {
		try {
			setFromAddress(fromAddress);
			setToAddress(toAddress);
			setCCAddress(ccAddress);
			setBCCAddress(bccAddress);
			message.setSubject(emailSubject);
			BodyPart messageBodyPart = new MimeBodyPart();
			Multipart multipart = new MimeMultipart();
			messageBodyPart.setContent(emailGen.getEmailHTMLReport(), "text/html");
			multipart.addBodyPart(messageBodyPart);
			messageBodyPart = new MimeBodyPart();
			String filename = userDir + "\\target\\extendedreports\\cucumber-results-feature-overview.html";
			DataSource source = new FileDataSource(filename);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName("cucumber-results-feature-overview.html");
			multipart.addBodyPart(messageBodyPart);
			message.setContent(multipart);
			System.out.println("Sending");
			Transport.send(message);
			System.out.println("Done");
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	public String getEmailMessageHTML() {
		return emailMessageHTML;
	}

	public void setEmailMessageHTML(String emailMessageHTML) {
		sendEmail.emailMessageHTML = emailMessageHTML;
	}

	public static void main(String[] args) {
	}
}
