package org.xtremeware.iudex.businesslogic.facade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import org.xtremeware.iudex.businesslogic.InvalidVoException;
import org.xtremeware.iudex.businesslogic.service.ServiceFactory;
import org.xtremeware.iudex.presentation.vovw.CommentVoVwFull;
import org.xtremeware.iudex.presentation.vovw.UserVoVwSmall;
import org.xtremeware.iudex.vo.*;

public class CommentsFacade extends AbstractFacade {

	public CommentsFacade(ServiceFactory serviceFactory, EntityManagerFactory emFactory) {
		super(serviceFactory, emFactory);
	}

	public CommentVo addComment(CommentVo vo) throws InvalidVoException {
		CommentVo createdVo = null;
		EntityManager em = null;
		EntityTransaction tx = null;
		try {
			em = getEntityManagerFactory().createEntityManager();
			tx = em.getTransaction();
			tx.commit();
			createdVo = getServiceFactory().createCommentsService().create(em, vo);
			tx.commit();
		} catch (InvalidVoException e) {
			throw e;
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
		return createdVo;

	}

	public void removeComment(long commentId) throws Exception {
		EntityManager em = null;
		EntityTransaction tx = null;
		try {
			em = getEntityManagerFactory().createEntityManager();
			tx = em.getTransaction();
			tx.begin();
			getServiceFactory().createCommentsService().remove(em, commentId);
			tx.commit();
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
	}

	public List<CommentVoVwFull> getCommentsByCourseId(long courseId) throws Exception {
		EntityManager em = null;
		EntityTransaction tx = null;
		List<CommentVoVwFull> result = new ArrayList<CommentVoVwFull>();
		try {
			em = getEntityManagerFactory().createEntityManager();
			tx = em.getTransaction();
			tx.begin();
			List<CommentVo> comments = getServiceFactory().createCommentsService().getByCourseId(em, courseId);
			HashMap<Long, UserVoVwSmall> users = new HashMap<Long, UserVoVwSmall>();

			for (CommentVo c : comments) {
				if (!users.containsKey(c.getUserId())) {
					UserVo vo = getServiceFactory().createUsersService().getById(em, c.getUserId());
					UserVoVwSmall uservo = new UserVoVwSmall(courseId, vo.getFirstName() + " " + vo.getLastName(), vo.getUserName());
				}
				if (c.isAnonymous()) {
					result.add(new CommentVoVwFull(c, null));
				} else {

					result.add(new CommentVoVwFull(c, users.get(c.getId())));
				}
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
		return result;
	}

	public RatingSummaryVo getCommentRatingSummary(long commentId) throws Exception {
		EntityManager em = null;
		RatingSummaryVo summary = null;
		try {
			em = getEntityManagerFactory().createEntityManager();
			summary = getServiceFactory().createCommentRatingService().getSummary(em, commentId);

		} catch (Exception e) {
			getServiceFactory().createLogService().error(e.getMessage(), e);
			throw e;
		} finally {
			if (em != null) {
				em.clear();
				em.close();
			}
		}
		return summary;
	}

	public CommentRatingVo getCommentRatingByUserId(long commentId, long userId) throws Exception {
		EntityManager em = null;
		CommentRatingVo rating = null;
		try {
			em = getEntityManagerFactory().createEntityManager();
			rating = getServiceFactory().createCommentRatingService().getByCommentIdAndUserId(em, commentId, userId);

		} catch (Exception e) {
			getServiceFactory().createLogService().error(e.getMessage(), e);
			throw e;
		} finally {
			if (em != null) {
				em.clear();
				em.close();
			}
		}
		return rating;
	}

	public CommentRatingVo rateComment(long commentId, long userId, int value) throws InvalidVoException {
		EntityManager em = null;
		EntityTransaction tx = null;
		CommentRatingVo rating = null;
		try {
			CommentRatingVo vo = new CommentRatingVo();
			vo.setCommentId(commentId);
			vo.setUserId(userId);
			vo.setValue(value);

			em = getEntityManagerFactory().createEntityManager();
			tx = em.getTransaction();
			tx.begin();
			rating = getServiceFactory().createCommentRatingService().getByCommentIdAndUserId(em, commentId, userId);
			//If there is no existing record in the database, create it
			if (rating == null) {
				rating = getServiceFactory().createCommentRatingService().create(em, vo);
			} else {
				//Otherwise update the existing one
				//But first verify bussines rules
				getServiceFactory().createCommentRatingService().validateVo(em, vo);
				rating.setValue(value);
			}
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
		return rating;
	}
}
