package org.xtremeware.iudex.businesslogic.service;

import java.util.ArrayList;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.InvalidVoException;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.entity.ProgramEntity;
import org.xtremeware.iudex.entity.UserEntity;
import org.xtremeware.iudex.helper.ConfigurationVariablesHelper;
import org.xtremeware.iudex.helper.ExternalServiceConnectionException;
import org.xtremeware.iudex.helper.SecurityHelper;
import org.xtremeware.iudex.vo.UserVo;

/**
 *
 * @author josebermeo
 */
public class UsersService extends CrudService<UserVo> {

	private final int MIN_USERNAME_LENGTH;
	private final int MAX_USERNAME_LENGTH;
	private final int MAX_USER_PASSWORD_LENGTH;
	private final int MIN_USER_PASSWORD_LENGTH;

	public UsersService(AbstractDaoFactory daoFactory) throws ExternalServiceConnectionException {
		super(daoFactory);
		MIN_USERNAME_LENGTH = Integer.parseInt(ConfigurationVariablesHelper.getVariable(ConfigurationVariablesHelper.MIN_USERNAME_LENGTH));
		MAX_USERNAME_LENGTH = Integer.parseInt(ConfigurationVariablesHelper.getVariable(ConfigurationVariablesHelper.MAX_USERNAME_LENGTH));
		MAX_USER_PASSWORD_LENGTH = Integer.parseInt(ConfigurationVariablesHelper.getVariable(ConfigurationVariablesHelper.MAX_USER_PASSWORD_LENGTH));
		MIN_USER_PASSWORD_LENGTH = Integer.parseInt(ConfigurationVariablesHelper.getVariable(ConfigurationVariablesHelper.MIN_USER_PASSWORD_LENGTH));

	}

	public void validateVo(EntityManager em, UserVo vo) throws InvalidVoException {
		if (vo == null) {
			throw new InvalidVoException("Null UserVo");
		}
		if (vo.getFirstName() == null) {
			throw new InvalidVoException("Null firstName in the provided UserVo");
		}
		if (vo.getLastName() == null) {
			throw new InvalidVoException("Null lastName in the provided UserVo");
		}
		if (vo.getUserName() == null) {
			throw new InvalidVoException("Null userName in the provided UserVo");
		}
		if (vo.getUserName().length() > MAX_USERNAME_LENGTH || vo.getUserName().length() < MIN_USERNAME_LENGTH) {
			throw new InvalidVoException("Invalid userName length in the provided UserVo");
		}
		if (vo.getPassword() == null) {
			throw new InvalidVoException("Null password in the provided UserVo");
		}
		if (vo.getPassword().length() > MAX_USER_PASSWORD_LENGTH|| vo.getUserName().length() < MIN_USER_PASSWORD_LENGTH) {
			throw new InvalidVoException("Invalid password length in the provided UserVo");
		}
		if (vo.getProgramsId() == null) {
			throw new InvalidVoException("Null programsId in the provided UserVo");
		}
		if (vo.getProgramsId().isEmpty()) {
			throw new InvalidVoException("programsId cannot be empity");
		}
		for (Long programId : vo.getProgramsId()) {
			if (programId == null) {
				throw new InvalidVoException("Any element in programsId cannot be null");
			}
			if (this.getDaoFactory().getProgramDao().getById(em, programId) == null) {
				throw new InvalidVoException("An element in programsId cannot be found");
			}
		}
		if (vo.getRol() == null) {
			throw new InvalidVoException("Rol cannot be null");
		}
	}

	public UserEntity voToEntity(EntityManager em, UserVo vo) throws InvalidVoException, ExternalServiceConnectionException {

		validateVo(em, vo);

		UserEntity userEntity = new UserEntity();
		userEntity.setId(vo.getId());
		userEntity.setFirstName(SecurityHelper.sanitizeHTML(vo.getFirstName()));
		userEntity.setLastName(SecurityHelper.sanitizeHTML(vo.getLastName()));
		userEntity.setUserName(SecurityHelper.sanitizeHTML(vo.getUserName()));
		userEntity.setPassword(vo.getPassword());
		userEntity.setRol(vo.getRol());
		userEntity.setActive(vo.isActive());

		ArrayList<ProgramEntity> arrayList = new ArrayList<ProgramEntity>();
		for (Long programId : vo.getProgramsId()) {
			arrayList.add(this.getDaoFactory().getProgramDao().getById(em, programId));
		}

		userEntity.setPrograms(arrayList);
		return userEntity;

	}

	public UserVo create(EntityManager em, UserVo user) {
		return null;
	}

	public UserVo authenticate(EntityManager em, String userName, String password) {
		return null;
	}

	public void activateAccount(EntityManager em, String confirmationKey) {
	}

	public UserVo getById(EntityManager em, Long id) {
		return this.getDaoFactory().getUserDao().getById(em, id).toVo();
	}

	public void update(EntityManager em, UserVo object) throws InvalidVoException, ExternalServiceConnectionException {
		this.getDaoFactory().getUserDao().merge(em, this.voToEntity(em, object));
	}

	public void remove(EntityManager em, UserVo user) {
	}
}