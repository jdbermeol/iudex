package org.xtremeware.iudex.businesslogic.facade;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import org.xtremeware.iudex.businesslogic.InvalidVoException;
import org.xtremeware.iudex.businesslogic.service.InactiveUserException;
import org.xtremeware.iudex.businesslogic.service.ServiceFactory;
import org.xtremeware.iudex.vo.UserVo;

public class UsersFacade extends AbstractFacade {

	public UsersFacade(ServiceFactory serviceFactory, EntityManagerFactory emFactory) {
		super(serviceFactory, emFactory);
	}

	/**
	 *
	 *
	 * @param vo
	 * @return Returns the corresponding
	 * <code>UserVo</code> if and only if the user is active. otherwise
	 * returns null. Throws exceptions if there were problems with the
	 * database.
	 */
	public UserVo activateUser(long userId, String confirmationKey) throws Exception {
		EntityManager em = null;
		EntityTransaction tx = null;
		UserVo user = null;
		try {
			em = getEntityManagerFactory().createEntityManager();
			getServiceFactory().createUsersService().activateAccount(em, userId, confirmationKey);
			UserVo vo = getServiceFactory().createUsersService().getById(em, userId);
			if (vo.isActive()) {
				user = vo;
			}
		} catch (Exception e) {
			if (em != null && tx != null) {
				tx.rollback();
			}
			getServiceFactory().createLogService().error(e.getMessage(), e);
			throw e;
		} finally {
			if (em != null) {
				em.clear();
				em.close();
			}
		}
		return user;
	}

	public UserVo addUser(UserVo vo) throws InvalidVoException {
		EntityManager em = null;
		EntityTransaction tx = null;
		UserVo user = null;
		try {
			em = getEntityManagerFactory().createEntityManager();
			user = getServiceFactory().createUsersService().create(em, user);

		} catch (InvalidVoException ex) {
			throw ex;
		} catch (Exception e) {
			if (em != null && tx != null) {
				tx.rollback();
			}
			getServiceFactory().createLogService().error(e.getMessage(), e);
		} finally {
			if (em != null) {
				em.clear();
				em.close();
			}
		}
		return user;
	}

	/**
	 *
	 *
	 * @param vo
	 * @return Returns null if there is no user with the provided username
	 * and password and throws an exception if the user is still inactive or
	 * if there are problems with the database.
	 */
	public UserVo logIn(String username, String password) throws Exception {
		EntityManager em = null;
		EntityTransaction tx = null;
		UserVo user = null;
		try {
			em = getEntityManagerFactory().createEntityManager();
			user = getServiceFactory().createUsersService().authenticate(em, username, password);
		} catch (InactiveUserException e) {
			throw e;
		} catch (Exception e) {
			getServiceFactory().createLogService().error(e.getMessage(), e);
			throw e;
		} finally {
			if (em != null) {
				em.clear();
				em.close();
			}
		}
		return user;
	}

	/**
	 *
	 *
	 * @param vo
	 * @return Returns null if there is a problem while persisting (logs all
	 * errors) and throws an exception if data isn't valid.
	 */
	public UserVo editUser(UserVo vo) throws InvalidVoException {

		EntityManager em = null;
		EntityTransaction tx = null;
		UserVo user = null;
		try {
			em = getEntityManagerFactory().createEntityManager();
			tx = em.getTransaction();
			tx.begin();
			getServiceFactory().createUsersService().update(em, user);
			tx.commit();

		} catch (InvalidVoException ex) {
			throw ex;
		} catch (Exception e) {
			if (em != null && tx != null) {
				tx.rollback();
			}
			getServiceFactory().createLogService().error(e.getMessage(), e);
		} finally {
			if (em != null) {
				em.clear();
				em.close();
			}
		}
		return user;

	}
}
