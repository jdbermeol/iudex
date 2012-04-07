/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xtremeware.iudex.helper;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;
import org.owasp.validator.html.*;

/**
 *
 * @author saaperezru
 */
public class SecurityHelper {

	private Policy policy;
	private AntiSamy antiSamy;
	private static SecurityHelper instance;

	private SecurityHelper(String policyFile) throws ExternalServiceConnectionException {
		try {
			policy = Policy.getInstance(policyFile);
			antiSamy = new AntiSamy(policy);
		} catch (PolicyException ex) {
			throw new ExternalServiceConnectionException("The specified ANTISAMY policy file for the SecuirtyHelper cannot be accessed");
		}
	}

	private static SecurityHelper getInstance() throws ExternalServiceConnectionException {
		while (instance == null) {
			instance = new SecurityHelper(ConfigurationVariablesHelper.getVariable(ConfigurationVariablesHelper.ANTISAMY_POLICY_FILE));
		}
		return instance;
	}

	public static String sanitizeHTML(String input) throws ExternalServiceConnectionException {
		String result = null;
		try {
			CleanResults cr = getInstance().getAntiSamy().scan(input, getInstance().getPolicy());
			result = cr.getCleanHTML();
		} catch (Exception ex) {
			log(Level.SEVERE, null, ex);
			throw new ExternalServiceConnectionException("There was a problem while sanitizing.");
		}
		return result;
	}

	public static String hashPassword(String password) {
		String hash = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(password.getBytes(Charset.forName("UTF-8")));

			byte[] mdbytes = md.digest();

			//convert the byte to hex format method 1
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < mdbytes.length; i++) {
				sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			hash = sb.toString();
		} catch (NoSuchAlgorithmException ex) {
			///Es casi imposible que pase esto
			Logger.getLogger(SecurityHelper.class.getName()).log(Level.SEVERE, null, ex);
		}
		return hash;
	}

	public static boolean verifyCaptcha(HttpServletRequest request) {
		String remoteAddr = request.getRemoteAddr();
		ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
		reCaptcha.setPrivateKey("your_private_key");

		String challenge = request.getParameter("recaptcha_challenge_field");
		String uresponse = request.getParameter("recaptcha_response_field");
		ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAddr, challenge, uresponse);

		return (reCaptchaResponse.isValid());
	}

	public AntiSamy getAntiSamy() {
		return antiSamy;
	}

	public void setAntiSamy(AntiSamy antiSamy) {
		this.antiSamy = antiSamy;
	}

	public Policy getPolicy() {
		return policy;
	}

	public void setPolicy(Policy policy) {
		this.policy = policy;
	}
}
