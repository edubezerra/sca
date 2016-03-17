package br.cefetrj.sca.dominio.contas;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidadorEmail {
 
	private Pattern pattern;
	private Matcher matcher;
 
	private static final String EMAIL_PATTERN = 
		"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
		+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
 
	public ValidadorEmail() {
		pattern = Pattern.compile(EMAIL_PATTERN);
	}
 
	/**
	 * Validate hex with regular expression
	 * 
	 * @param hex
	 *            hex for validation
	 * @return true valid hex, false invalid hex
	 */
	public boolean validar(final String hex) {
 
		matcher = pattern.matcher(hex);
		return matcher.matches();
 
	}
}