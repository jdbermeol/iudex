/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xtremeware.iudex.helper;

import javax.persistence.Persistence;
import org.xtremeware.iudex.businesslogic.facade.FacadeFactory;
import org.xtremeware.iudex.businesslogic.service.ServiceFactory;
import org.xtremeware.iudex.vo.MailingConfigVo;

/**
 *
 * @author saaperezru
 */
public class Config {

	public final static String CONFIGURATION_VARIABLES_PATH = "/../lib/iudex.properties";

	private String persistenceUnit;
	private static Config instance;
	private ServiceFactory serviceFactory;
        private FacadeFactory facadeFactory;

	private Config(String persistenceUnit) throws ExternalServiceConnectionException {
		MailingConfigVo mailingConf = new MailingConfigVo();
		mailingConf.setSender(ConfigurationVariablesHelper.getVariable(ConfigurationVariablesHelper.MAILING_SENDER_EMAIL_ADDRESS));
		mailingConf.setSmtpPassword(ConfigurationVariablesHelper.getVariable(ConfigurationVariablesHelper.MAILING_SMTP_PASSWORD));
		mailingConf.setSmtpServer(ConfigurationVariablesHelper.getVariable(ConfigurationVariablesHelper.MAILING_SMTP_SERVER));
		mailingConf.setSmtpServerPort(Integer.parseInt(ConfigurationVariablesHelper.getVariable(ConfigurationVariablesHelper.MAILING_SMTP_PORT)));
		mailingConf.setSmtpUser(ConfigurationVariablesHelper.getVariable(ConfigurationVariablesHelper.MAILING_SMTP_USER));
                facadeFactory = new FacadeFactory(serviceFactory, Persistence.createEntityManagerFactory(persistenceUnit));
	}

	public static Config getInstance() {
		while (instance == null) {
			try {
				instance = new Config("org.xtremeware.iudex_local");
			} catch (ExternalServiceConnectionException ex) {
				System.out.println("[FATAL ERROR] Configuration Variables file could not be found, this is a ");
			}
		}
		return instance;
	}

	public String getPersistenceUnit() {
		return persistenceUnit;
	}

	public ServiceFactory getServiceFactory() {
		return serviceFactory;
	}
        
        public FacadeFactory getFacadeFactory() {
        	return facadeFactory;
        }
}
