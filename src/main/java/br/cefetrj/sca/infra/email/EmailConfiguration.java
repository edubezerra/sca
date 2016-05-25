package br.cefetrj.sca.infra.email;

import java.util.Properties;

public class EmailConfiguration {
	private Properties properties = new Properties();

	public static final String SMTP_HOST = "mail.smtp.host";
	public static final String SMTP_AUTH = "mail.smtp.auth";
	public static final String SMTP_TLS_ENABLE = "mail.smtp.starttls.enable";
	public static final String SMTP_AUTH_USER = "smtp.auth.user";
	public static final String SMTP_AUTH_PWD = "smtp.auth.pwd";
	public static final String DEBUG = "debug";
	public static final String SMTP_PORT = "mail.smtp.port";

	public Properties getProperties() {
		return this.properties;
	}

	public void setProperty(String key, String value) {
		this.properties.put(key, value);
	}

	public void addProperties(Properties props) {
		this.properties.putAll(props);
	}

	public String getProperty(String key) {
		return this.properties.getProperty(key);
	}
}