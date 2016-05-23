package br.cefetrj.sca.infra.email;

public class EmailClient {
	public static void main(String[] args) {
		EmailConfiguration configuration = new EmailConfiguration();
		configuration.setProperty(EmailConfiguration.SMTP_HOST, "smtp.gmail.com");
		configuration.setProperty(EmailConfiguration.SMTP_AUTH, "true");
		configuration.setProperty(EmailConfiguration.SMTP_TLS_ENABLE, "true");
		configuration.setProperty(EmailConfiguration.SMTP_PORT, "587");

		configuration.setProperty(EmailConfiguration.SMTP_AUTH_USER, "NONONONONONO@gmail.com");
		configuration.setProperty(EmailConfiguration.SMTP_AUTH_PWD, "NONONONONONO");
		
		EmailService emailService = new EmailService(configuration);
		Email email = new Email();
		email.setFrom("NONONO-FROM@gmail.com");
		email.setTo("NONONO-TO@gmail.com");

		email.setSubject("Assunto da mensagem");
		email.setText("Olá, esse é um teste de envio de email");
		email.setMimeType("text/html");

		Attachment attachment1 = new Attachment("ABCDEFGH".getBytes(), "test1.txt", "text/plain");
		email.addAttachment(attachment1);

		Attachment attachment2 = new Attachment("XYZZZZZZ".getBytes(), "test2.txt", "text/plain");
		email.addAttachment(attachment2);

		emailService.sendEmail(email);

	}
}