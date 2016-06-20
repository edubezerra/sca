package br.cefetrj.sca.infra.email;

import java.util.List;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

public class EmailService {
	private EmailConfiguration configuration = null;
	private Authenticator auth = null;

	public EmailService(EmailConfiguration configuration) {
		this.configuration = configuration;
		this.auth = this.buildSmtpAuthenticator();
	}

	private Authenticator buildSmtpAuthenticator() {
		String emailId = configuration.getProperty(EmailConfiguration.SMTP_AUTH_USER);
		String password = configuration.getProperty(EmailConfiguration.SMTP_AUTH_PWD);
		return new SMTPAuthenticator(emailId, password);
	}

	public void sendEmail(MensagemEmail email) {
		Session session = Session.getDefaultInstance(this.configuration.getProperties(), auth);
		boolean debug = Boolean.valueOf(this.configuration.getProperty(EmailConfiguration.DEBUG));
		session.setDebug(debug);

		try {
			Message msg = this.buildEmailMessage(session, email);
			Transport.send(msg);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	private Message buildEmailMessage(Session session, MensagemEmail email) throws MessagingException {
		Message msg = new MimeMessage(session);
		msg.setSubject(email.getSubject());
		this.addRecievers(msg, email);
		Multipart multipart = new MimeMultipart();
		this.addMessageBodyPart(multipart, email);
		this.addAttachments(multipart, email);
		msg.setContent(multipart);
		return msg;
	}

	private void addRecievers(Message msg, MensagemEmail email) throws MessagingException {
		InternetAddress from = new InternetAddress(email.getFrom());
		msg.setFrom(from);

		InternetAddress[] to = this.getInternetAddresses(email.getTo());
		msg.setRecipients(Message.RecipientType.TO, to);

		InternetAddress[] cc = this.getInternetAddresses(email.getCc());
		msg.setRecipients(Message.RecipientType.CC, cc);

		InternetAddress[] bcc = this.getInternetAddresses(email.getBcc());
		msg.setRecipients(Message.RecipientType.BCC, bcc);

	}

	private void addMessageBodyPart(Multipart multipart, MensagemEmail email) throws MessagingException {
		BodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setContent(email.getText(), email.getMimeType());
		multipart.addBodyPart(messageBodyPart);
	}

	private void addAttachments(Multipart multipart, MensagemEmail email) throws MessagingException {
		List<Attachment> attachments = email.getAttachments();
		if (attachments != null && attachments.size() > 0) {
			for (Attachment attachment : attachments) {
				BodyPart attachmentBodyPart = new MimeBodyPart();
				String filename = attachment.getFilename();
				DataSource source = new ByteArrayDataSource(attachment.getData(), attachment.getMimeType());
				attachmentBodyPart.setDataHandler(new DataHandler(source));
				attachmentBodyPart.setFileName(filename);
				multipart.addBodyPart(attachmentBodyPart);
			}
		}
	}

	private InternetAddress[] getInternetAddresses(String... addresses) throws AddressException {
		if (addresses == null || addresses.length == 0) {
			return null;
		}
		InternetAddress[] iAddresses = new InternetAddress[addresses.length];
		for (int i = 0; i < addresses.length; i++) {
			iAddresses[i] = new InternetAddress(addresses[i]);
		}
		return iAddresses;
	}
}

class SMTPAuthenticator extends javax.mail.Authenticator {
	private String username;
	private String password;

	public SMTPAuthenticator(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(username, password);
	}
}